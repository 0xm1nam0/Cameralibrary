package com.hiscene.camera.vision;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des  视觉模块接口
 */
public interface IVision {

    void recognize(byte[] data, int width, int height);

    void tracking(byte[] data, int width, int height);

    void startRecognize();

    void stopRecognize();

    void init();

    void onDestroy();

    void setSleepTime(int sleepTime);

    int getFrameWidth();

    int getFrameHeight();
}
