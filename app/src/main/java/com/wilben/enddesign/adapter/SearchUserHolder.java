package com.wilben.enddesign.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.Bomb_User;
import com.wilben.enddesign.operation.ChatEvent;
import com.wilben.enddesign.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import cn.bmob.newim.bean.BmobIMUserInfo;

public class SearchUserHolder extends BaseViewHolder {

    @Bind(R.id.avatar)
    public ImageView avatar;
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.btn_chat)
    public Button btn_chat;

    public SearchUserHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_search_user, null);
    }

    @Override
    public void bindData(Object o) {
        final Bomb_User user = (Bomb_User) o;
        ViewUtil.setAvatar(user.getAvatar(), R.mipmap.cute_head, avatar);
        name.setText(user.getUsername());
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobIMUserInfo info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(), user.getAvatar());
                EventBus.getDefault().post(new ChatEvent(info));
            }
        });
    }
}