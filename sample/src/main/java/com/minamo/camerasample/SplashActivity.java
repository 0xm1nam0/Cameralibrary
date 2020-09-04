package com.minamo.camerasample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import com.minamo.utils.AssetsUtils;

import java.io.File;


/**
 * @author Minamo
 * @e-mail kleinminamo@gmail.com
 * @time 2020/1/7
 * @des
 */
public class SplashActivity extends EasyPermissionsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onDestroy() {
        handler.removeMessages(0);
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getHome();
            super.handleMessage(msg);
        }
    };

    public void getHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startMainActivity() {
        handler.sendEmptyMessageDelayed(0, 500);
    }

}
