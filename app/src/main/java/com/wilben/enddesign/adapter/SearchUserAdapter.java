package com.wilben.enddesign.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wilben.enddesign.entity.Bomb_User;
import com.wilben.enddesign.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索好友列表适配器
 * @author :
 * @project:SearchUserAdapter
 * @date :2016-01-22-14:18
 */
public class SearchUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Bomb_User> users = new ArrayList<>();

    public SearchUserAdapter() {
    }

    public void setDatas(List<Bomb_User> list) {
        users.clear();
        if (null != list) {
            users.addAll(list);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SearchUserHolder(parent.getContext(), parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).bindData(users.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
