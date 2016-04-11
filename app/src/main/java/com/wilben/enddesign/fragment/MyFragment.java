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
import android.widget.Toast;

import com.wilben.enddesign.R;
import com.wilben.enddesign.activity.AboutActivity;
import com.wilben.enddesign.activity.ChangePwdActivity;
import com.wilben.enddesign.activity.D_InfoActivity;
import com.wilben.enddesign.activity.LoginActivity;
import com.wilben.enddesign.activity.Main2Activity;
import com.wilben.enddesign.activity.StyleActivity;
import com.wilben.enddesign.activity.U_InfoActivity;
import com.wilben.enddesign.entity.Bomb_User;
import com.wilben.enddesign.model.UserModel;
import com.wilben.enddesign.util.HttpUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * 我的界面
 */
public class MyFragment extends Fragment implements View.OnClickListener {

    private Button btn_exit;
    private RelativeLayout rl_info, rl_style, rl_resetpwd, rl_about;
    private TextView tv_username;
    private ImageView iv_avatar, iv_chat;
    private String username;
    private Bitmap bm = null;
    private ProgressDialog p;
    private String role;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myfragment, container, false);
        init(view);
        //从activity传过来的Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            username = bundle.getString("username");
            role = bundle.getString("role");
            tv_username.setText(username);
        }
        p = new ProgressDialog(getActivity());
        p.setMessage("加载中...");
        p.show();
        //获取用户头像
        new AvatarAsyncTask().execute("Avatar", username, role);
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
        iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
        btn_exit.setOnClickListener(this);
        rl_info.setOnClickListener(this);
        rl_style.setOnClickListener(this);
        rl_resetpwd.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
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
                                        UserModel.getInstance().logout();
                                        //可断开连接
                                        BmobIM.getInstance().disConnect();
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
                if (role.equals("0")) {
                    intent.setClass(getActivity(), U_InfoActivity.class);
                } else {
                    intent.setClass(getActivity(), D_InfoActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rl_resetpwd:
                intent.setClass(getActivity(), ChangePwdActivity.class);
                bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("role", role);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rl_style:
                intent.setClass(getActivity(), StyleActivity.class);
                bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("role", role);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.rl_about:
                intent.setClass(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_chat:

                Bomb_User user = UserModel.getInstance().getCurrentUser();
                if (user == null) {
                    UserModel.getInstance().login(username, "123456", new LogInListener() {

                        @Override
                        public void done(Object o, BmobException e) {
                            if (e == null) {
                                Bomb_User user = (Bomb_User) o;
                                //更新当前用户资料
                                BmobIM.getInstance().updateUserInfo(new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar()));
                                Intent intent1 = new Intent();
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("object", "0");
                                intent1.putExtras(bundle1);
                                intent1.setClass(getActivity(), Main2Activity.class);
                                startActivity(intent1);
                            } else {
                                Toast.makeText(getActivity(), e.getMessage() + "(" + e.getErrorCode() + ")", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    intent.setClass(getActivity(), Main2Activity.class);
                    bundle = new Bundle();
                    bundle.putString("object", "0");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            default:
                break;

        }
    }


    class AvatarAsyncTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... params) {
            try {
                String avatarUrl = new HttpUtils().getAvatar(params[0], params[1], params[2]);
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
