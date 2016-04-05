package com.wilben.enddesign.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.activity.AboutActivity;
import com.wilben.enddesign.activity.ChangePwdActivity;
import com.wilben.enddesign.activity.LoginActivity;
import com.wilben.enddesign.activity.StyleActivity;
import com.wilben.enddesign.activity.U_infoActivity;
import com.wilben.enddesign.util.HttpUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class U_MyFragment extends Fragment implements View.OnClickListener {

    private Button btn_exit;
    private RelativeLayout rl_info, rl_style, rl_resetpwd, rl_about;
    private TextView tv_username;
    private ImageView iv_avatar;
    private String username;
    private Bitmap bm = null;
    private ProgressDialog p;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.u_myfragment, container, false);
        init(view);
        //从activity传过来的Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
            tv_username.setText(username);
        }
        p = new ProgressDialog(getActivity());
        p.setMessage("加载中...");
        p.show();
        //获取用户头像
        new AvatarAsyncTask().execute("Avatar", username);
        return view;
    }

    private void init(View view) {
        btn_exit = (Button) view.findViewById(R.id.btn_exit);
        rl_info = (RelativeLayout) view.findViewById(R.id.rl_info);
        rl_style = (RelativeLayout) view.findViewById(R.id.rl_style);
        rl_resetpwd = (RelativeLayout) view.findViewById(R.id.rl_resetpwd);
        rl_about = (RelativeLayout) view.findViewById(R.id.rl_about);
        tv_username = (TextView) view.findViewById(R.id.tv_username);
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        btn_exit.setOnClickListener(this);
        rl_info.setOnClickListener(this);
        rl_style.setOnClickListener(this);
        rl_resetpwd.setOnClickListener(this);
        rl_about.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_exit:
                new AlertDialog.Builder(getActivity())
                        .setTitle("确认退出吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 点击“确认”后的操作
                                        Intent intent = new Intent();
                                        intent.setClass(getActivity(), LoginActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();

                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // 点击“返回”后的操作,这里不设置没有任何操作
                                    }
                                }).show();
                break;
            case R.id.rl_info:
                intent.setClass(getActivity(), U_infoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rl_resetpwd:
                intent.setClass(getActivity(), ChangePwdActivity.class);
                bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rl_style:
                intent.setClass(getActivity(), StyleActivity.class);
                bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rl_about:
                intent.setClass(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            default:
                break;

        }
    }


    class AvatarAsyncTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {
            try {
                String avatarUrl = new HttpUtils().getAvatar(params[0], params[1]);
                if (avatarUrl != null || !avatarUrl.equals("")) {
                    URL url = new URL(avatarUrl);
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
        }
    }
}
