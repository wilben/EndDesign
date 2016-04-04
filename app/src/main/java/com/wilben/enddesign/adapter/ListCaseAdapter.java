package com.wilben.enddesign.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wilben.enddesign.NoScrollGridView;
import com.wilben.enddesign.R;
import com.wilben.enddesign.activity.ImagePagerActivity;
import com.wilben.enddesign.entity.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页ListView的数据适配器
 *
 * @author Administrator
 */
public class ListCaseAdapter extends BaseAdapter {

    private Context mContext;
    private List<Project> items;

    public ListCaseAdapter(Context ctx, List<Project> items) {
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
            convertView = View.inflate(mContext, R.layout.item_list, null);
            holder.iv_avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
            holder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            holder.tv_description = (TextView) convertView.findViewById(R.id.tv_description);
            holder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Project itemEntity = items.get(position);
        holder.tv_username.setText(itemEntity.getUsername());
        holder.tv_description.setText(itemEntity.getDescription());
        // 使用ImageLoader加载网络图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .showImageOnLoading(R.mipmap.ic_launcher) // 加载中显示的默认图片
                .showImageOnFail(R.mipmap.ic_launcher) // 设置加载失败的默认图片
                .cacheInMemory(true) // 内存缓存
                .cacheOnDisk(true) // sdcard缓存
                .bitmapConfig(Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoader.getInstance().displayImage(itemEntity.getImage(), holder.iv_avatar, options);
        final ArrayList<String> imageUrls = itemEntity.getImageUrls();
        if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
            holder.gridview.setVisibility(View.GONE);
        } else {
            holder.gridview.setAdapter(new NoScrollGridAdapter(mContext, imageUrls));
        }
        // 点击回帖九宫格，查看大图
        holder.gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                imageBrower(position, imageUrls);
            }
        });
        return convertView;
    }

    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    /**
     * listview组件复用，防止“卡顿”
     *
     * @author Administrator
     */
    class ViewHolder {
        private ImageView iv_avatar;
        private TextView tv_username;
        private TextView tv_description;
        private NoScrollGridView gridview;
    }
}
