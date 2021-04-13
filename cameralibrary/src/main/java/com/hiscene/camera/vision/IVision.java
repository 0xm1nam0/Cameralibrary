package com.hiscene.camera.vision;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des  视觉模块接口
 */
public interface IVision {

    VisionState recognize(byte[] data, int width, int height, int type);

    VisionState tracking(byte[] data, int width, int height, int type);

    void startRecognize();

    void stopRecognize();

    void init();

    void onDestroy();

    void setSleepTime(int sleepTime);

    int getFrameWidth();

    int getFrameHeight();
}
