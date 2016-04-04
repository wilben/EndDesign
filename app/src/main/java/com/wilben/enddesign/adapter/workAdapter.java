package com.wilben.enddesign.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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

public class WorkAdapter extends BaseAdapter {

    private List<Project> list;
    private Context context;

    public WorkAdapter(Context context, List<Project> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.work_entity, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Project workEntity = list.get(position);
        holder.tv_title.setText(workEntity.getTitle());
        // 使用ImageLoader加载网络图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .showImageOnLoading(R.mipmap.ic_launcher) // 加载中显示的默认图片
                .showImageOnFail(R.mipmap.ic_launcher) // 设置加载失败的默认图片
                .cacheInMemory(true) // 内存缓存
                .cacheOnDisk(true) // sdcard缓存
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoader.getInstance().displayImage(workEntity.getImage(), holder.iv_image, options);
        return convertView;
    }

    class ViewHolder {
        private ImageView iv_image;
        private TextView tv_title;
    }
}
