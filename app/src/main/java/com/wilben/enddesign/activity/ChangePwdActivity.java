package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wilben.enddesign.R;
import com.wilben.enddesign.util.HttpUtils;

/**
 * 修改密码界面
 */

public class ChangePwdActivity extends Activity {

    private ImageButton f_back;
    private EditText et_password, et_repassword;
    private TextView tv_save;
    String password, repassword, username;
    private ProgressDialog p;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepwd);
        Bundle bundle = this.getIntent().getExtras();
        username = bundle.getString("username");
        role = bundle.getString("role");
        p = new ProgressDialog(this);
        p.setMessage("修改中...");
        init();
    }

    public void init() {

        f_back = (ImageButton) findViewById(R.id.ib_back);
        et_password = (EditText) findViewById(R.id.et_password);
        et_repassword = (EditText) findViewById(R.id.et_repassword);
        tv_save = (TextView) findViewById(R.id.tv_save);
        f_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = et_password.getText().toString().trim();
                repassword = et_repassword.getText().toString().trim();
                if (password == null || password.length() <= 0) {
                    et_password.setError("新密码不能为空");
                    et_password.requestFocus();
                    return;
                }

                if (password.length() < 6 || password.length() > 16) {
                    et_password.setError("新密码长度不符");
                    et_password.requestFocus();
                    return;
                }

                if (repassword == null || repassword.length() <= 0) {
                    et_repassword.setError("确认密码不能为空");
                    et_repassword.requestFocus();
                    return;
                }

                if (!repassword.equals(password)) {
                    et_repassword.setError("密码不一致");
                    et_repassword.requestFocus();
                    return;
                }
                p.show();
                new changepwdAsyncTask().execute("ChangePwd", username, password, role);
            }
        });


    }


    class changepwdAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = new HttpUtils().changePwd(params[0], params[1], params[2], params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            p.dismiss();
            if (result.equals("t")) {
                Intent intent = new Intent();
                intent.setClass(ChangePwdActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(ChangePwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChangePwdActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

}

