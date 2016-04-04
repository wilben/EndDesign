package com.wilben.enddesign.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wilben.enddesign.R;
import com.wilben.enddesign.util.HttpUtils;


public class StyleActivity extends Activity {

    private ImageButton f_back;
    private TextView tv_style, tv_save;
    private Bundle bundle;
    private String username;
    private ProgressDialog p;
    private LinearLayout ll_style;
    private boolean[] flags = new boolean[]{false, false, false};//初始复选情况
    private String[] items = new String[]{"现代简约", "地中海", "欧式"};
    private String result = "";
    String style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mystyle);
        bundle = this.getIntent().getExtras();
        username = bundle.getString("username");
        init();
        p = new ProgressDialog(this);
        p.setMessage("加载中...");
        p.show();
        new getStyleAsyncTask().execute("Style", username);
    }

    public void init() {

        f_back = (ImageButton) findViewById(R.id.ib_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_style = (TextView) findViewById(R.id.tv_style);
        ll_style = (LinearLayout) findViewById(R.id.ll_style);
        f_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        ll_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(StyleActivity.this)
                        .setTitle("风格")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setMultiChoiceItems(items, flags, new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                flags[which] = isChecked;
                            }
                        })
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        for (int i = 0; i < flags.length; i++) {
                                            if (flags[i]) {
                                                result = result + items[i] + "、";
                                            }
                                        }
                                        if (result.length() > 0) {
                                            tv_style.setText(result.substring(0, result.length() - 1));
                                        } else
                                            tv_style.setText("");
                                        result = "";
                                    }
                                })
                        .setNegativeButton("取消",
                                null).show();
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.setMessage("修改中...");
                p.show();
                new Thread(new Runnable() {

                    public void run() {
                        String result = null;
                        try {
                            style = tv_style.getText().toString().trim();
                            result = new HttpUtils().saveStyle("SaveStyle", username, style);
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
                        Toast.makeText(StyleActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(StyleActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    super.handleMessage(msg);
                }
            };
        });
    }


    //获取个人信息
    class getStyleAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
                style = new HttpUtils().getStyle(params[0], params[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 100;
        }

        @Override
        protected void onPostExecute(Integer a) {
            super.onPostExecute(a);
            p.dismiss();
            if (style != null)
                tv_style.setText(style);
        }
    }


}

