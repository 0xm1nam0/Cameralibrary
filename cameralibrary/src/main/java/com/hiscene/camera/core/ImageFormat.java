package com.hiscene.camera.core;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2020/9/4
 * @des  库不用枚举，使用int以便于引用的时候方便扩展
 */
public class ImageFormat {
    public final static int RGBA = 0;
    public final static int RGB = 1;
    public final static int NV21 = 2;
    public final static int NV12 = 3;
    public final static int I420 = 4;
    public final static int YV12 = 5;
}
