package com.wilben.enddesign.adapter;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wilben.enddesign.R;
import com.wilben.enddesign.util.HttpUtils;

import java.util.ArrayList;

/**
 * 格子图片适配器
 */
public class NoScrollGridAdapter extends BaseAdapter {

    /**
     * 上下文
     */
    private Context ctx;
    /**
     * 图片Url集合
     */
    private ArrayList<String> imageUrls;

    public NoScrollGridAdapter(Context ctx, ArrayList<String> urls) {
        this.ctx = ctx;
        this.imageUrls = urls;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageUrls == null ? 0 : imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(ctx, R.layout.item_gridview, null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 200);
        view.setLayoutParams(params);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
        DisplayImageOptions options = new DisplayImageOptions.Builder()//
                .cacheInMemory(true)//
                .cacheOnDisk(true)//
                .bitmapConfig(Config.RGB_565)//
                .build();
        ImageLoader.getInstance().displayImage(HttpUtils.URLVAR+imageUrls.get(position), imageView, options);
        return view;
    }

}
