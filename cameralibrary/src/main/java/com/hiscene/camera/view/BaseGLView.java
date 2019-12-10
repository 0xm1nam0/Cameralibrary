package com.hiscene.camera.view;

import android.content.Context;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;

 /**
   * @author Minamo
   * @e-mail kleinminamo@gmail.com
   * @time   2019/12/10
   * @des    BaseGLView
   */
public class BaseGLView extends GLSurfaceView {
    protected ScreenShotListener screenShotListener;
    protected boolean needScreenShot = false;

    enum TouchEvent {
        TOUCH_PRESS,
        TOUCH_RELEASE,
        TOUCH_MOVE
    }

    public BaseGLView(Context context) {
        super(context);
    }

    public void setScreenShotListener(ScreenShotListener listener) {
        screenShotListener = listener;
    }

    public void screenShot() {
        needScreenShot = true;
    }

    public interface ScreenShotListener {
        void screenShot(ByteBuffer rgbaBuf, int width, int height, int type);
    }
}
