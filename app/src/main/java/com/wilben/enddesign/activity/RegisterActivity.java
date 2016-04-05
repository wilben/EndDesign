package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.User;
import com.wilben.enddesign.util.HttpUtils;
import com.wilben.enddesign.util.WriteJson;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends Activity {
    TextView tv_save;
    EditText etusername;
    EditText etpassword;
    RadioButton ckman;
    RadioButton ckwoman;
    EditText etage;
    EditText etrepassword;
    EditText etrealname;
    String str;
    String jsonString = null;
    String username = null;
    String password = null;
    String sex = null;
    String age = null;
    String repassword = null;
    String realname = null;
    String avatar = null;
    String style = null;
    int role = 0;
    private ProgressDialog p;
    private ImageButton f_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        init();
        etusername.setOnFocusChangeListener(new EtusernameOnFocusChange());
        tv_save.setOnClickListener(new SubmitOnclick());
    }

    private void init() {
        tv_save = (TextView) findViewById(R.id.tv_save);
        etusername = (EditText) findViewById(R.id.etusername);
        etpassword = (EditText) findViewById(R.id.etpassword);
        ckman = (RadioButton) findViewById(R.id.ckman);
        ckwoman = (RadioButton) findViewById(R.id.ckwoman);
        etage = (EditText) findViewById(R.id.etage);
        etrealname = (EditText) findViewById(R.id.etrealname);
        etrepassword = (EditText) findViewById(R.id.etrepassword);
        f_back = (ImageButton) findViewById(R.id.ib_back);
        f_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        p = new ProgressDialog(RegisterActivity.this);
        p.setMessage("注册中...");
    }

    private class EtusernameOnFocusChange implements OnFocusChangeListener {
        public void onFocusChange(View v, boolean hasFocus) {
            if (!etusername.hasFocus()) {
                str = etusername.getText().toString().trim();
                if (str == null || str.length() <= 0) {
                    // etusername.setError("用户名不能为空");
                    return;
                } else {
                    new Thread(new Runnable() {
                        // 如果用户名不为空，那么将用户名提交到服务器上进行验证，看用户名是否存在，就像JavaEE中利用
                        // ajax一样，虽然你看不到但是它却偷偷摸摸做了很多
                        public void run() {
                            String result = new HttpUtils()
                                    .checkusername("Check", str);
                            System.out.println("result:" + result);
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);
                        }
                    }).start();
                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String msgobj = msg.obj.toString();
            System.out.println(msgobj);
            System.out.println(msgobj.length());

            if (msgobj.equals("t")) {
                etusername.requestFocus();
                etusername.setError("用户名[" + str + "]已存在");
            }
            super.handleMessage(msg);
        }
    };


    private class SubmitOnclick implements OnClickListener {
        public void onClick(View v) {
            username = etusername.getText().toString().trim();
            password = etpassword.getText().toString().trim();
            repassword = etrepassword.getText().toString().trim();
            realname = etrealname.getText().toString().trim();
            age = etage.getText().toString().trim();
            avatar = "";
            style = "";


            if (username == null || username.length() <= 0) {
                etusername.setError("用户名不能为空");
                etusername.requestFocus();
                return;
            }

            if (username.length() < 6 || username.length() > 16) {
                etusername.setError("用户名长度不符");
                etusername.requestFocus();
                return;
            }

            if (password == null || password.length() <= 0) {
                etpassword.setError("密码不能为空");
                etpassword.requestFocus();
                return;
            }

            if (password.length() < 6 || password.length() > 16) {
                etpassword.setError("密码长度不符");
                etpassword.requestFocus();
                return;
            }

            if (repassword == null || repassword.length() <= 0) {
                etrepassword.setError("确认密码不能为空");
                etrepassword.requestFocus();
                return;
            }

            if (!repassword.equals(password)) {
                etrepassword.setError("密码不一致");
                etrepassword.requestFocus();
                return;
            }

            if (ckman.isChecked()) {
                sex = "男";
            } else {
                sex = "女";
            }

            p.show();
            new Thread(new Runnable() {

                public void run() {

                    User user = new User(username, password, sex, realname, age, avatar, role, style);
                    // 构造一个user对象
                    List<User> list = new ArrayList<User>();
                    list.add(user);
                    WriteJson writeJson = new WriteJson();
                    // 将user对象写出json形式字符串
                    jsonString = writeJson.getJsonData(list);
                    System.out.println(jsonString);
                    String result = null;
                    try {
                        result = new HttpUtils().register("Register", jsonString);
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
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
            super.handleMessage(msg);
        }
    };
}
