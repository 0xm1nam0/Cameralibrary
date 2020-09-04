package com.hiscene.camera.listener;

import android.content.Context;
import android.graphics.SurfaceTexture;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2020/9/4
 * @des
 */
public interface ICameraEngine {

    int openCamera(boolean isFront);

    int openCamera(boolean isFront, SurfaceTexture surfaceTexture);

    int closeCamera();

    int startPreview();

    int stopPreview();

    int switchCamera(SurfaceTexture surfaceTexture);


    int setPreviewSize(int width, int height);

    int getPreViewWidth();

    int getPreViewHeight();

    String getInfo();

    boolean inUse();

    void setOnNewFrameListener(ICameraEngine.OnNewFrameListener newFrameListener);

    boolean isFrontCamera();

    void handleZoom(float ratio);

    void handleFocusMetering(int centerX, int centerY, ICameraEngine.FocusCallback focusCallback);

    boolean switchFlashLight(Context context);

    void closeFlashLight(Context context);

    public interface FocusCallback {
        void onComplete(boolean success);
    }

    public interface OnNewFrameListener {
        void onNewFrame(byte[] data, int width, int height,int imageFormat, BufferCallback bufferCallback);

        void onError(int error);
    }

    public interface BufferCallback {
        void addCallbackBuffer(byte[] data);
    }
}
