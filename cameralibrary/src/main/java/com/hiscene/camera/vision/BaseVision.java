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
    protected ReentrantLock lock = new ReentrantLock();
    protected boolean need2Process = false;
    protected int frameWidth, frameHeight;
    protected int sleepTime = 0;
    protected boolean need2Recognize = false;
    protected int type;


    private VisionState processState = VisionState.NONE;


    @Override
    public void onNewFrame(byte[] data, int width, int height, int type, ICameraEngine.BufferCallback bufferCallback) {
        frameWidth = width;
        frameHeight = height;
        if (recognizerBuffer == null) {
            recognizerBuffer = ByteBuffer.allocate(data.length);
        }
        if (need2Recognize && lock.tryLock()) {
            if (processState == VisionState.NONE) {
//                    LogUtils.d("onNewFrame put data");
                recognizerBuffer.position(0);
                recognizerBuffer.put(data);
                this.type = type;
                need2Process = true;
                processState = VisionState.RECOGNIZED;
            } else if (processState == VisionState.TRACKING) {
                processState = tracking(data, width, height, type);
            }
            lock.unlock();
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
        if (processState == VisionState.RECOGNIZED && need2Process) {
            lock.lock();
            need2Process = false;
//            LogUtils.d("loop");
            if (need2Recognize) {
//                LogUtils.d("recognize");
                processState = recognize(recognizerBuffer.array(), frameWidth, frameHeight, type);
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
