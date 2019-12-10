package com.hiscene.camera.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;


import com.minamo.utils.LoggerUtils;
import com.minamo.utils.OpenglUtil;
import com.hiscene.camera.core.CameraSource;
import com.hiscene.camera.renderer.RendererController;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

 /**
   * @author Minamo
   * @e-mail kleinminamo@gmail.com
   * @time   2019/12/10
   * @des    CameraView
   */
public class CameraView extends BaseGLView {

    boolean isBack = false;

    public CameraView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        CameraRenderer mRenderer = new CameraRenderer();
        setEGLConfigChooser(8, 8, 8, 8, 24, 8);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }


    public void pause() {
        CameraSource.Instance().stopPreview();
        CameraSource.Instance().closeCamera();
        LoggerUtils.i("CameraSource.pause");
    }

    public void swtich() {
        if(isBack){
            CameraSource.Instance().openCamera(CameraSource.CAMERA_DIRECTION_FRONT);
            LoggerUtils.i("CAMERA_DIRECTION_FRONT swtich:"+isBack);
            isBack = false;
        }else {
            CameraSource.Instance().openCamera(CameraSource.CAMERA_DIRECTION_BACK);
            LoggerUtils.i("CAMERA_DIRECTION_BACK swtich:"+isBack);
            isBack = true;
        }
        int startPreview = CameraSource.Instance().startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        if(isBack){
            CameraSource.Instance().openCamera(CameraSource.CAMERA_DIRECTION_BACK);
        }else {
            CameraSource.Instance().openCamera(CameraSource.CAMERA_DIRECTION_FRONT);
        }
        LoggerUtils.i("CameraView surfaceCreated");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        CameraSource.Instance().stopPreview();
        CameraSource.Instance().closeCamera();
        LoggerUtils.i("CameraView surfaceDestroyed");
    }


    class CameraRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            LoggerUtils.i("CameraView onSurfaceCreated");
            RendererController.Instance().init();
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            LoggerUtils.i("CameraView onSurfaceChanged");
            GLES20.glViewport(0, 0, width, height);
            RendererController.Instance().configScreen(width, height);
//            CameraSource.Instance().setPreviewSize(size.width, size.height);
            int startPreview = CameraSource.Instance().startPreview();
            LoggerUtils.i("Camera startPreview : "+startPreview);
//            CameraSource.Instance().setFocusMode(0);
//            GLES20.glDisable(GLES20.GL_CULL_FACE);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            RendererController.Instance().drawVideoBackground();
            if (needScreenShot && screenShotListener != null) {
                //FileUtil.getGLPixels(getWidth(),getHeight(),path+File.separator+"car.png");
                LoggerUtils.i("   time  camera start ==========  " + System.currentTimeMillis());
                screenShotListener.screenShot(OpenglUtil.getGLPixels(getWidth(), getHeight()), getWidth(), getHeight(), 1);
                needScreenShot = false;
            }
        }
    }

    public void setBack(boolean back) {
        isBack = back;
    }
}
