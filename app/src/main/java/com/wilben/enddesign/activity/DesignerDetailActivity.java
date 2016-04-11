package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.Bomb_User;
import com.wilben.enddesign.entity.Designer;
import com.wilben.enddesign.model.UserModel;
import com.wilben.enddesign.operation.SearchService;
import com.wilben.enddesign.util.HttpUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * 设计师详情界面
 */
public class DesignerDetailActivity extends Activity {

    private TextView tv_username;
    private TextView tv_period;
    private TextView tv_concept;
    private TextView tv_motto;
    private TextView tv_style;
    private TextView tv_work;
    private TextView tv_area;
    private String username;
    private String user;
    private ImageView iv_avatar, iv_chat;
    private Designer designer;
    private Bitmap bm = null;
    private ProgressDialog p;
    private Button btn_work;
    private Button btn_launch;
    private Bundle bundle;
    private Intent intent;
    private ImageButton f_back;
    private String role;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.designerdetail);
        init();
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        user = bundle.getString("user");
        role = bundle.getString("role");
        p.show();
        new getDesignerDetail().execute("DesignerDetail", username);
    }

    private void init() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_period = (TextView) findViewById(R.id.tv_period);
        tv_concept = (TextView) findViewById(R.id.tv_concept);
        tv_motto = (TextView) findViewById(R.id.tv_motto);
        tv_style = (TextView) findViewById(R.id.tv_style);
        tv_work = (TextView) findViewById(R.id.tv_work);
        tv_area = (TextView) findViewById(R.id.tv_area);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        iv_chat = (ImageView) findViewById(R.id.iv_chat);
        btn_work = (Button) findViewById(R.id.btn_work);
        btn_launch = (Button) findViewById(R.id.btn_launch);
        f_back = (ImageButton) findViewById(R.id.ib_back);
        f_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
        btn_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("username", designer.getUsername());
                bundle.putString("role", role);
                intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(DesignerDetailActivity.this, WorkActivity.class);
                startActivity(intent);
            }
        });
        btn_launch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("user", user);
                intent = new Intent(DesignerDetailActivity.this, LaunchProjectActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        iv_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bomb_User bomb_user = UserModel.getInstance().getCurrentUser();
                if (bomb_user == null) {
                    UserModel.getInstance().login(user, "123456", new LogInListener() {

                        @Override
                        public void done(Object o, BmobException e) {
                            if (e == null) {
                                Bomb_User user = (Bomb_User) o;
                                //更新当前用户资料
                                BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                                Intent intent1 = new Intent(DesignerDetailActivity.this, Main2Activity.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("object", "1");
                                bundle1.putString("name",username);
                                intent1.putExtras(bundle1);
                                startActivity(intent1);
                            } else {
                                Toast.makeText(DesignerDetailActivity.this, e.getMessage() + "(" + e.getErrorCode() + ")", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    bundle = new Bundle();
                    bundle.putString("object", "1");
                    bundle.putString("name",username);
                    intent = new Intent(DesignerDetailActivity.this, Main2Activity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    class getDesignerDetail extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                designer = new SearchService().getDesignerDetail(params[0], params[1]);
                String avatar = designer.getAvatar();
                if (avatar != null || !avatar.equals("")) {
                    URL url = new URL(HttpUtils.URLVAR+avatar);
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            tv_username.setText(designer.getUsername());
            tv_period.setText(designer.getPeriod());
            tv_concept.setText(designer.getConcept());
            tv_motto.setText(designer.getMotto());
            tv_style.setText(designer.getStyle());
            tv_work.setText(designer.getWork());
            tv_area.setText(designer.getArea());
            if (bm != null)
                iv_avatar.setImageBitmap(bm);
        }
    }


}