package com.hiscene.camera.renderer;

import android.hardware.Camera;

import com.hiscene.camera.listener.ICameraEngine;

/**
   * @author Minamo
   * @e-mail kleinminamo@gmail.com
   * @time   2019/12/10
   * @des    RendererController
   */
public class RendererController implements ICameraEngine.OnNewFrameListener {
    @Override
    public void onNewFrame(byte[] data, int width, int height, int imageFormat, ICameraEngine.BufferCallback bufferCallback) {

        synchronized (nv21Renderer){
            nv21Renderer.setPictureSize(width,height);
            nv21Renderer.onNewFrame(data,width,height,imageFormat,bufferCallback);
        }
    }

    @Override
    public void onError(int error) {

    }

    private static class SingletonHolder {
        static final RendererController _instance = new RendererController();
    }

    RendererController(){

    }

    public void init(){
        nv21Renderer = new SourceNV21Renderer();
        if(orientation != -1){
            nv21Renderer.configOrientation(orientation);
        }
    }

    private int orientation = -1;
    private SourceRenderer nv21Renderer;

    public boolean isTextureInit() {
        return nv21Renderer.isTextureInit();
    }
    public void release(){
        nv21Renderer.resetTextureTag();
    }

    public void updateCameraLabel(){
        nv21Renderer.updateCameraLabel();
    }

    public static RendererController Instance(){
        return SingletonHolder._instance;
    }

    public void configScreen(int width,int height){
        synchronized (nv21Renderer){
            nv21Renderer.configScreen(width,height);
        }
    }

    public void configOrientation(int ori){
        orientation = ori;
        if(nv21Renderer!=null){
            nv21Renderer.configOrientation(ori);
        }
    }

    public void drawVideoBackground(){
        synchronized (nv21Renderer){
            nv21Renderer.draw();
        }
    }
}
