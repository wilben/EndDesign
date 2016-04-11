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
import com.wilben.enddesign.entity.Designer;
import com.wilben.enddesign.util.HttpUtils;

import java.util.List;

/**
 * 设计师列表数据适配器
 *
 * @author Administrator
 */
public class ListDesignerAdapter extends BaseAdapter {

    private Context mContext;
    private List<Designer> items;

    public ListDesignerAdapter(Context ctx, List<Designer> items) {
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
            convertView = View.inflate(mContext, R.layout.designer_list, null);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_area = (TextView) convertView.findViewById(R.id.tv_area);
            holder.tv_style = (TextView) convertView.findViewById(R.id.tv_style);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Designer designerEntity = items.get(position);
        holder.tv_username.setText(designerEntity.getUsername());
        holder.tv_area.setText(designerEntity.getArea());
        holder.tv_style.setText(designerEntity.getStyle());
        // 使用ImageLoader加载网络图片
        if (designerEntity.getAvatar().equals(""))
            designerEntity.setAvatar("1");
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .showImageOnLoading(R.mipmap.default_avatar) // 加载中显示的默认图片
                .showImageOnFail(R.mipmap.default_avatar) // 设置加载失败的默认图片
                .cacheInMemory(true) // 内存缓存
                .cacheOnDisk(true) // sdcard缓存
                .bitmapConfig(Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoader.getInstance().displayImage(HttpUtils.URLVAR+designerEntity.getAvatar(), holder.iv_avatar, options);
        return convertView;
    }


    /**
     * listview组件复用，防止“卡顿”
     *
     * @author Administrator
     */
    class ViewHolder {
        private ImageView iv_avatar;
        private TextView tv_username;
        private TextView tv_area;
        private TextView tv_style;
    }
}
