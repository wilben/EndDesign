package com.wilben.enddesign.model.i;

import com.wilben.enddesign.entity.Bomb_User;
import com.wilben.enddesign.entity.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.BmobListener;

/**
 * @author :smile
 * @project:QueryUserListener
 * @date :2016-02-01-16:23
 */
public abstract class QueryUserListener extends BmobListener<Bomb_User> {
    public abstract void done(Bomb_User s, BmobException e);

    @Override
    protected void postDone(Bomb_User o, BmobException e) {
        done(o, e);
    }
}
