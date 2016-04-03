package com.wilben.enddesign.adapter;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wilben.enddesign.R;
import com.wilben.enddesign.entity.Project;

import java.util.List;

/**
 * 首页ListView的数据适配器
 *
 * @author Administrator
 */
public class ProjectAdapter extends BaseAdapter {

    private Context mContext;
    private List<Project> items;

    public ProjectAdapter(Context ctx, List<Project> items) {
        this.mContext = ctx;
        this.items = items;
    }


    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.project_list, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Project itemEntity = items.get(position);
        holder.tv_username.setText(itemEntity.getUsername());
        holder.tv_title.setText(itemEntity.getTitle());
        holder.tv_time.setText(itemEntity.getTime());
        switch (itemEntity.getState()) {
            case 0:
                holder.tv_state.setText("待设计");
                break;
            case 1:
                holder.tv_state.setText("设计中");
                break;
            case 2:
                holder.tv_state.setText("已完成");
                break;
            default:
                break;
        }
        // 使用ImageLoader加载网络图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .showImageOnLoading(R.mipmap.ic_launcher) // 加载中显示的默认图片
                .showImageOnFail(R.mipmap.ic_launcher) // 设置加载失败的默认图片
                .cacheInMemory(true) // 内存缓存
                .cacheOnDisk(true) // sdcard缓存
                .bitmapConfig(Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoader.getInstance().displayImage(itemEntity.getImage(), holder.iv_image, options);
        return convertView;
    }


    /**
     * listview组件复用，防止“卡顿”
     *
     * @author Administrator
     */
    class ViewHolder {
        private ImageView iv_image;
        private TextView tv_title;
        private TextView tv_username;
        private TextView tv_time;
        private TextView tv_state;
    }
}
