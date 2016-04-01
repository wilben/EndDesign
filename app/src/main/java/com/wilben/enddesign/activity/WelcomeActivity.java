package com.wilben.enddesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.wilben.enddesign.R;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message arg0) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                return false;
            }

        }).sendEmptyMessageDelayed(0, 1500);
    }
}
