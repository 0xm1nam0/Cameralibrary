package com.hiscene.camera.vision;


import android.graphics.Rect;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.hiscene.camera.listener.OnZxingRecognizeListener;
import com.hiscene.camera.qr.DecodeFormatManager;
import com.hiscene.camera.qr.PlanarYUVLuminanceSource;
import com.minamo.utils.LoggerUtils;

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2019/6/20
 * @des  Zxing识别视觉模块
 */
public class ZxingVision extends BaseVision {
    //    private QRCodeReader qr = new QRCodeReader();
    private MultiFormatReader multiFormatReader;
    private Result result;
    Hashtable<DecodeHintType, Object> hints;

    @Override
    public void recognize(byte[] data, int width, int height) {
        //modify here 转竖屏
/*        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }
        int tmp = width; // Here we are swapping, that's the difference to #11
        width = height;
        height = tmp;*/

        PlanarYUVLuminanceSource source = buildLuminanceSource(data, width, height);
        if (source != null) {
//            isNeedQRRecognize = false;
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                result = multiFormatReader.decodeWithState(bitmap);
                LoggerUtils.i("====  qr result: " + result + "====  width: " + width + "====  height: " + height);

                if (mOnQrRecognizeListener != null) {
                    need2Recognize = mOnQrRecognizeListener.OnRecognize(result);
                } else {
                    need2Recognize = false;
                }
            } catch (ReaderException re) {
                // continue
            } finally {
                multiFormatReader.reset();
            }
        }
    }

    @Override
    public void tracking(byte[] data, int width, int height) {

    }

    @Override
    public void init() {
        multiFormatReader = new MultiFormatReader();
        hints = new Hashtable<DecodeHintType, Object>(3);
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
        // TODO: 2019/10/29
        decodeFormats.add(BarcodeFormat.CODE_128);
//        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
//        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
/*        if (characterSet != null) {
            hints.put(DecodeHintType.CHARACTER_SET, characterSet);
        }*/
//        hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, resultPointCallback);
        // TODO: 2019/10/29   // 容错质量

        multiFormatReader.setHints(hints);
    }

    @Override
    public void onDestroy() {

    }

    /**
     * 二维码回调接口
     */
    private OnZxingRecognizeListener mOnQrRecognizeListener;

    public void setOnQrRecognizeListener(OnZxingRecognizeListener onQrRecognizeListener) {
        mOnQrRecognizeListener = onQrRecognizeListener;
    }

    /**
     * CarPublicPraiseComment2 factory method to build the appropriate LuminanceSource object based on the format of the preview buffers, as described by Camera.Parameters.
     *
     * @param data   CarPublicPraiseComment2 preview frame.
     * @param width  The width of the image.
     * @param height The height of the image.
     * @return CarPublicPraiseComment2 PlanarYUVLuminanceSource instance.
     */
    public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data, int width, int height) {
        Rect rect = getFramingRectInPreview();
//        rect.set(0, 0, width, height);
        if (rect == null) {
            rect = new Rect();
//            rect.set(width/4, height*2/6, width*3/4, height*4/6);
            rect.set(0, 0, width, height);
        }
//        LoggerUtils.i("====  QR FramingRectInPreview: ====  left: " + rect.left + "====  top: " + rect.top+ "====  width: " + rect.width()+ "====  height: " + rect.height());
        // Go ahead and assume it's YUV rather than die.
        return new PlanarYUVLuminanceSource(data, width, height, rect.left, rect.top, rect.width(), rect.height());
    }

    private Rect framRect;

    private Rect getFramingRectInPreview() {
/*        if (framRect == null) {
            framRect = new Rect();
        }*/
        return framRect;
    }

    public void setFramingRectInPreview(Rect framRect) {
        this.framRect = framRect;
    }

}
