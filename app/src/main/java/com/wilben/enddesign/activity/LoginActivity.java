package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wilben.enddesign.R;
import com.wilben.enddesign.util.HttpUtils;

//此为主activity的实现登录的。在android2.3以后，android规定了主activity不允许在主线程中做一些耗时较多的
//操作，包括网络的操作，主要是减少应用程序停止响应的问题。下面注释掉的部分是网上找到的方法，加上之后就可以在主线程中
//进行联网操作了，但是本人采用了线程的操作，未采用该方法
public class LoginActivity extends Activity {
    private Button login;
    private TextView tv_save;
    private EditText etusername;
    private EditText etpassword;
    private String username;
    private String password;
    private ProgressDialog p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        init();
        tv_save.setOnClickListener(new RegisterOnclick());
        login.setOnClickListener(new LoginOnclick());
    }

    private void init() {
        etusername = (EditText) findViewById(R.id.etusername);
        etpassword = (EditText) findViewById(R.id.etpassword);
        login = (Button) findViewById(R.id.login);
        tv_save = (TextView) findViewById(R.id.tv_save);
        p = new ProgressDialog(LoginActivity.this);
        p.setMessage("登录中...");
    }

    private class RegisterOnclick implements OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }

    }

    private class LoginOnclick implements OnClickListener {
        public void onClick(View arg0) {
            username = etusername.getText().toString().trim();
            if (username == null || username.length() <= 0) {
                etusername.requestFocus();
                etusername.setError("对不起，用户名不能为空");
                return;
            } else {
                username = etusername.getText().toString().trim();
            }
            password = etpassword.getText().toString().trim();
            if (password == null || password.length() <= 0) {
                etpassword.requestFocus();
                etpassword.setError("对不起，密码不能为空");
                return;
            } else {
                password = etpassword.getText().toString().trim();
            }
            p.show();
            new Thread(new Runnable() {

                public void run() {
                    String result = null;
                    try {
                        result = new HttpUtils().login("Login", username, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.obj = result;
                    handler.sendMessage(msg);
                }
            }).start();

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String string = (String) msg.obj;
            p.dismiss();
            Intent intent = new Intent();
            //用Bundle携带数据
            Bundle bundle = new Bundle();
            if (string.equals("0") || string.equals("1")) {
                if (string.equals("0")) {
                    bundle.putString("username", username);
                    bundle.putString("role","0");
                    intent.putExtras(bundle);

                    intent.setClass(LoginActivity.this, U_MainActivity.class);
                }
                if (string.equals("1")) {
                    bundle.putString("username", username);
                    bundle.putString("role","1");
                    intent.putExtras(bundle);
                    intent.setClass(LoginActivity.this, D_MainActivity.class);
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };

}