package com.wilben.enddesign.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.wilben.enddesign.R;


public class AboutActivity extends Activity {

    private ImageButton f_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        init();
    }

    public void init() {

        f_back = (ImageButton) findViewById(R.id.ib_back);
        f_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }


}

