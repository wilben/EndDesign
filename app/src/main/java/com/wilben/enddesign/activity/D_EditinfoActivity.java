package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.Designer;
import com.wilben.enddesign.operation.SearchService;
import com.wilben.enddesign.util.HttpUtils;
import com.wilben.enddesign.util.WriteJson;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class D_EditinfoActivity extends Activity {

    private ImageButton f_back;
    private Designer designer;
    private TextView tv_username, tv_save;
    private EditText et_realname, et_age, et_sex, et_concept, et_motto, et_work, et_area;
    private ImageView iv_avatar;
    private Bitmap bm = null;
    private Bundle bundle;
    private String username, realname, avatar = "", sex, age, concept, motto, work, area;
    private final int IMAGE_OPEN = 1; // 打开图片标记
    private String imagePath; // 选择图片路径
    boolean uploadImg = false;
    private ProgressDialog p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_editinfo);
        bundle = this.getIntent().getExtras();
        username = bundle.getString("username");
        init();
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
        p.show();
        new getInfoAsyncTask().execute("D_Info", username);
    }

    public void init() {

        tv_username = (TextView) findViewById(R.id.tv_username);
        et_realname = (EditText) findViewById(R.id.et_realname);
        et_age = (EditText) findViewById(R.id.et_age);
        et_sex = (EditText) findViewById(R.id.et_sex);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_concept = (EditText) findViewById(R.id.et_concept);
        et_motto = (EditText) findViewById(R.id.et_motto);
        et_work = (EditText) findViewById(R.id.et_work);
        et_area = (EditText) findViewById(R.id.et_area);
        f_back = (ImageButton) findViewById(R.id.ib_back);
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
                realname = et_realname.getText().toString().trim();
                sex = et_sex.getText().toString().trim();
                age = et_age.getText().toString().trim();
                concept = et_concept.getText().toString().trim();
                motto = et_motto.getText().toString().trim();
                work = et_work.getText().toString().trim();
                area = et_area.getText().toString().trim();
                p.setMessage("修改中...");
                p.show();
                new Thread(new Runnable() {

                    public void run() {
                        //上传修改头像
                        if (uploadImg == true) {
                            if (imagePath != null && imagePath.length() > 0) {
                                File file = new File(imagePath);
                                String result = new HttpUtils().uploadFile(file, "FileInOutputStream");
                                if (!"-1".equals(result)) {
                                    avatar = result;
                                }
                                uploadImg = false;
                            }
                        }
                        designer = new Designer(username, "", sex, realname, age, avatar, 1, concept, motto, "", work, "", area);
                        List<Designer> list = new ArrayList<Designer>();
                        list.add(designer);
                        WriteJson writeJson = new WriteJson();
                        // 将user对象写出json形式字符串
                        String jsonString = writeJson.getJsonData(list);
                        String result = null;
                        try {
                            result = new HttpUtils().saveInfo("D_SaveInfo", jsonString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message msg = new Message();
                        msg.obj = result;
                        handler1.sendMessage(msg);
                    }
                }).start();
            }

            Handler handler1 = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String msgobj = msg.obj.toString();
                    p.dismiss();
                    if (msgobj.equals("t")) {
                        Toast.makeText(D_EditinfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(D_EditinfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    super.handleMessage(msg);
                }
            };
        });

        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_OPEN);
            }
        });
    }

    // 获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 打开图片
        if (resultCode == RESULT_OK && requestCode == IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                // 查询选择图片
                Cursor cursor = getContentResolver().query(uri,
                        new String[]{MediaStore.Images.Media.DATA}, null,
                        null, null);
                // 返回 没找到选择图片
                if (null == cursor) {
                    return;
                }
                // 光标移动至开头 获取图片路径
                cursor.moveToFirst();
                imagePath = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                iv_avatar.setImageBitmap(bitmap);
                uploadImg = true;
            }
        }

    }


    //获取个人信息
    class getInfoAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
                designer = new Designer();
                designer = new SearchService().getD_Info(params[0], params[1]);
                String avatarUrl = designer.getAvatar();
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
            tv_username.setText(designer.getUsername());
            et_realname.setText(designer.getRealname());
            et_sex.setText(designer.getSex());
            et_age.setText(designer.getAge());
            et_concept.setText(designer.getConcept());
            et_motto.setText(designer.getMotto());
            et_work.setText(designer.getWork());
            et_area.setText(designer.getArea());
        }
    }


}

