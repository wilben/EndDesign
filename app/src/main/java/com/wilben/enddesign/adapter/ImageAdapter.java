package com.wilben.enddesign.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wilben.enddesign.R;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends BaseAdapter {


    private Context context;
    private List<Integer> imageList;
    private List<String> titleList;

    public ImageAdapter(Context context) {
        super();
        this.context = context;

        imageList = new ArrayList<Integer>();
        titleList = new ArrayList<String>();
        imageList.add(R.mipmap.a1);
        imageList.add(R.mipmap.a2);
        imageList.add(R.mipmap.a3);
        imageList.add(R.mipmap.a4);
        titleList.add("全部");
        titleList.add("待设计");
        titleList.add("设计中");
        titleList.add("已完成");

    }

    public int getCount() {
        return imageList.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int arg0, View arg1, ViewGroup arg2) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View twoEditTextLayoutRef = inflater.inflate(R.layout.image_entity,
                null);
        ImageView ivRef = (ImageView) twoEditTextLayoutRef
                .findViewById(R.id.iv_image);
        TextView tvRef = (TextView) twoEditTextLayoutRef
                .findViewById(R.id.tv_title);

        ivRef.setImageResource(imageList.get(arg0));
        tvRef.setText(titleList.get(arg0));
        return twoEditTextLayoutRef;
    }

}
