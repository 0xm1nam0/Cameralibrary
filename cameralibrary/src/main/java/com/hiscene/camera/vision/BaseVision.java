package com.hiscene.camera.vision;

import com.hiscene.camera.listener.ICameraEngine;
import com.hiscene.camera.renderer.RendererController;
import com.minamo.thread.LoopThread;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/11/1
 * @des 视觉模块基类
 */
public abstract class BaseVision extends LoopThread implements IVision, ICameraEngine.OnNewFrameListener {
    protected ByteBuffer recognizerBuffer = null;
    protected boolean bufferInit = false;
    protected ReentrantLock lock = new ReentrantLock();
    protected boolean need2Process = false;
    protected int frameWidth, frameHeight;
    protected int sleepTime = 0;
    protected boolean need2Recognize = false;

    enum State {
        RECOGNIZED,
        TRACKING,
        NONE
    }

    private State processState = State.NONE;


    @Override
    public void onNewFrame(byte[] data, int width, int height, int type, ICameraEngine.BufferCallback bufferCallback) {
        frameWidth = width;
        frameHeight = height;
        if (!bufferInit) {
            recognizerBuffer = ByteBuffer.allocate(data.length);
            bufferInit = true;
        }
        if (processState == State.NONE) {
            if (lock.tryLock()) {
                if (!need2Process) {
//                    LogUtils.d("onNewFrame put data");
                    recognizerBuffer.position(0);
                    recognizerBuffer.put(data);
                    need2Process = true;
                }
                lock.unlock();
            }
        } else if (processState == State.TRACKING) {
            tracking(data, width, height);
        }
        RendererController.Instance().onNewFrame(data, width, height, type, bufferCallback);
    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void setup() {
        init();
    }

    @Override
    public void loop() {
        if (processState == State.NONE && need2Process) {
            lock.lock();
            processState = State.NONE;
            need2Process = false;
//            LogUtils.d("loop");
            if (need2Recognize) {
//                LogUtils.d("recognize");
                recognize(recognizerBuffer.array(), frameWidth, frameHeight);
            }
            recognizerBuffer.clear();
            lock.unlock();
        }
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void over() {
        onDestroy();
    }

    @Override
    public synchronized void start() {
        super.start();
        startRecognize();
    }

    @Override
    public void startRecognize() {
        need2Recognize = true;
    }

    @Override
    public void stopRecognize() {
        need2Recognize = false;
    }

    @Override
    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    @Override
    public int getFrameWidth() {
        return frameWidth;
    }

    @Override
    public int getFrameHeight() {
        return frameHeight;
    }
}
