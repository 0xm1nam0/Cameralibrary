package com.minamo.camerasample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hiscene.camera.core.CameraEngine;
import com.hiscene.camera.listener.OnZxingRecognizeListener;
import com.hiscene.camera.view.CameraView;
import com.hiscene.camera.vision.ZxingVision;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    FrameLayout cameraLayout;
    View parent;
    TextView resultText;

    CameraView cameraView;
    ZxingVision vision;
    CameraEngine cameraEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraLayout = findViewById(R.id.cameraLayout);
        parent = findViewById(R.id.parent);
        resultText = findViewById(R.id.result);
        cameraEngine = new CameraEngine(this,false);
        cameraView = new CameraView(this,cameraEngine);
//        if(C.IS_G200){
//            cameraView.setBack(false);
//        }else {
        cameraView.setCameraDirection(true);
//        }
        cameraLayout.addView(cameraView);

        vision = new ZxingVision();
        vision.setOnQrRecognizeListener(result -> {
            Log.d(TAG, "OnRecognize: "+result.getText());
            runOnUiThread(()->{
                resultText.setText(result.getText());
            });
            return false;
        });
        vision.start();
        cameraView.setVision(vision);
    }
}
