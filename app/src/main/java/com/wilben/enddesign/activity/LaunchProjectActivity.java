package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.Project;
import com.wilben.enddesign.util.HttpUtils;
import com.wilben.enddesign.util.WriteJson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 发起项目界面
 * Created by wilben on 2016/4/3.
 */
public class LaunchProjectActivity extends Activity implements View.OnClickListener {

    private String username;
    private String user;
    private ProgressDialog p;
    private TextView tv_username;
    private EditText et_title;
    private TextView tv_save;
    private String title = null;
    private String time;
    private ImageButton f_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launchproject);
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        user = bundle.getString("user");
        init();
        tv_username.setText(username);
    }

    private void init() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        et_title = (EditText) findViewById(R.id.et_title);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);
        f_back = (ImageButton) findViewById(R.id.ib_back);
        f_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        p = new ProgressDialog(this);
        p.setMessage("提交中...");
    }

    @Override
    public void onClick(View v) {
        title = et_title.getText().toString().trim();
        if (title == null || title.length() <= 0) {
            et_title.setError("项目名称不能为空");
            et_title.requestFocus();
            return;
        } else {
            p.show();
            new Thread(new Runnable() {

                public void run() {
                    Calendar c = Calendar.getInstance();
                    time = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
                    Project project = new Project(null, title, time, user, 0, null, null, 0, username, "");
                    List<Project> list = new ArrayList<Project>();
                    list.add(project);
                    WriteJson writeJson = new WriteJson();
                    // 将user对象写出json形式字符串
                    String jsonString = writeJson.getJsonData(list);
                    String result = null;
                    try {
                        result = new HttpUtils().launchProject("LaunchProject", jsonString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.obj = result;
                    handler1.sendMessage(msg);
                }
            }).start();

        }
    }


    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String msgobj = msg.obj.toString();
            p.dismiss();
            if (msgobj.equals("t")) {
                Toast.makeText(LaunchProjectActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(LaunchProjectActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };
}




