package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.User;
import com.wilben.enddesign.operation.SearchService;
import com.wilben.enddesign.util.HttpUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 用户--个人信息界面
 */
public class U_InfoActivity extends Activity {

    private ImageButton f_back;
    private User user;
    private TextView tv_username, tv_realname, tv_age, tv_sex, tv_edit;
    private ImageView iv_avatar;
    private Bitmap bm = null;
    private Bundle bundle;
    private String username;
    private ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.u_info);
        bundle = this.getIntent().getExtras();
        username = bundle.getString("username");
        init();
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
        p.show();
        new getInfoAsyncTask().execute("U_Info", username);
    }

    public void init() {

        f_back = (ImageButton) findViewById(R.id.ib_back);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_realname = (TextView) findViewById(R.id.tv_realname);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_edit = (TextView) findViewById(R.id.tv_edit);
        f_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                intent.setClass(U_InfoActivity.this, U_EditinfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    class getInfoAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
                user = new User();
                user = new SearchService().getU_Info(params[0], params[1]);
                String avatarUrl = user.getAvatar();
                if (avatarUrl != null || !avatarUrl.equals("")) {
                    URL url = new URL(HttpUtils.URLVAR+avatarUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bm = BitmapFactory.decodeStream(is);
                    is.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 100;
        }

        @Override
        protected void onPostExecute(Integer a) {
            super.onPostExecute(a);
            p.dismiss();
            if (bm != null)
                iv_avatar.setImageBitmap(bm);
            tv_username.setText(user.getUsername());
            tv_realname.setText(user.getRealname());
            tv_sex.setText(user.getSex());
            tv_age.setText(user.getAge());
        }
    }

}

