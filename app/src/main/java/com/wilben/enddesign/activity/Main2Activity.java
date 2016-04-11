package com.wilben.enddesign.activity;

import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.Bomb_User;
import com.wilben.enddesign.fragment.ConversationFragment;
import com.wilben.enddesign.model.BaseModel;
import com.wilben.enddesign.model.UserModel;
import com.wilben.enddesign.operation.ChatEvent;
import com.wilben.enddesign.util.IMMLeaks;

import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.ObseverListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author :
 * @project:MainActivity
 * @date :2016-01-15-18:23
 */
public class Main2Activity extends BaseActivity implements ObseverListener {


    private ConversationFragment conversationFragment;
    private String flag;
    private String name;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getExtras();
        flag = bundle.getString("object");
        setContentView(R.layout.activity_main);
        //连接服务器
        BmobUser user = UserModel.getInstance().getCurrentUser();
        BmobIM.connect(user.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Logger.i("connect success");
                } else {
                    Logger.e(e.getErrorCode() + "/" + e.getMessage());
                }
            }
        });
        //监听连接状态，也可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
        BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
            @Override
            public void onChange(ConnectionStatus status) {
                toast("" + status.getMsg());
            }
        });
        //解决leancanary提示InputMethodManager内存泄露的问题
        IMMLeaks.fixFocusedViewLeak(getApplication());

        if (flag.equals("1")) {
            name = bundle.getString("name");
            UserModel.getInstance().queryUsers(name, BaseModel.DEFAULT_LIMIT, new FindListener<Bomb_User>() {
                @Override
                public void onSuccess(List<Bomb_User> list) {
                    Bomb_User user = list.get(0);
                    BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                    onEventMainThread(new ChatEvent(info));
                }

                @Override
                public void onError(int i, String s) {
                    toast(s + "(" + i + ")");
                }
            });
        }
    }

    @Override
    protected void initView() {
        super.initView();
        initTab();
    }

    private void initTab() {
        conversationFragment = new ConversationFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationFragment).
                show(conversationFragment).commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //添加观察者-用于是否显示通知消息
        BmobNotificationManager.getInstance(this).addObserver(this);
        //进入应用后，通知栏应取消
        BmobNotificationManager.getInstance(this).cancelNotification();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //移除观察者
        BmobNotificationManager.getInstance(this).removeObserver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清理导致内存泄露的资源
        BmobIM.getInstance().clear();
        //完全退出应用时需调用clearObserver来清除观察者
        BmobNotificationManager.getInstance(this).clearObserver();
    }

    public void onEventMainThread(ChatEvent event) {
        BmobIMUserInfo info = event.info;
        //如果需要更新用户资料，开发者只需要传新的info进去就可以了
        Logger.i("" + info.getName() + "," + info.getAvatar() + "," + info.getUserId());
        BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
            @Override
            public void done(BmobIMConversation c, BmobException e) {
                if (e == null) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("c", c);
                    startActivity(ChatActivity.class, bundle, false);
                } else {
                    toast(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }


}
