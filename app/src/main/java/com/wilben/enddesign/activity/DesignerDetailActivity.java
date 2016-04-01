package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.Designer;
import com.wilben.enddesign.operation.SearchService;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DesignerDetailActivity extends Activity {

    private TextView tv_username;
    private TextView tv_period;
    private TextView tv_concept;
    private TextView tv_motto;
    private TextView tv_style;
    private TextView tv_work;
    private TextView tv_area;
    private String username;
    private ImageView iv_avatar;
    private Designer designer;
    private Bitmap bm = null;
    private ProgressDialog p;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.designerdetail);
        init();
        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        p.show();
        new getDesignerDetail().execute("DesignerDetail",username);
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
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
    }

    class getDesignerDetail extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                designer = new SearchService().getDesignerDetail(params[0], params[1]);
                String avatar = designer.getAvatar();
                if (avatar != null || !avatar.equals("")) {
                    URL url = new URL(avatar);
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