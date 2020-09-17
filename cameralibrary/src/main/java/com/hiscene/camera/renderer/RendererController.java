package com.hiscene.camera.renderer;

import com.hiscene.camera.listener.ICameraEngine;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/12/10
 * @des RendererController
 */
public class RendererController implements ICameraEngine.OnNewFrameListener {
    @Override
    public void onNewFrame(byte[] data, int width, int height, int imageFormat, ICameraEngine.BufferCallback bufferCallback) {
        if (yuvRenderer == null) return;
        synchronized (yuvRenderer) {
            yuvRenderer.setPictureSize(width, height);
            yuvRenderer.onNewFrame(data, width, height, imageFormat, bufferCallback);
        }
    }

    @Override
    public void onError(int error) {

    }

    private static class SingletonHolder {
        static final RendererController _instance = new RendererController();
    }

    RendererController() {

    }

    public void init() {
        yuvRenderer = new SourceYuvRenderer();
        if (orientation != -1) {
            yuvRenderer.configOrientation(orientation);
        }
    }

    private int orientation = -1;
    private SourceRenderer yuvRenderer;

    public boolean isTextureInit() {
        return yuvRenderer.isTextureInit();
    }

    public void release() {
        yuvRenderer.resetTextureTag();
    }

    public void updateCameraLabel() {
        yuvRenderer.updateCameraLabel();
    }

    public static RendererController Instance() {
        return SingletonHolder._instance;
    }

    public void configScreen(int width, int height) {
        synchronized (yuvRenderer) {
            yuvRenderer.configScreen(width, height);
        }
    }

    public void configOrientation(int ori) {
        orientation = ori;
        if (yuvRenderer != null) {
            yuvRenderer.configOrientation(ori);
        }
    }

    public void drawVideoBackground() {
        synchronized (yuvRenderer) {
            yuvRenderer.draw();
        }
    }
}
