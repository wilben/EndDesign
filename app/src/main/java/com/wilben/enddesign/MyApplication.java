package com.wilben.enddesign;

import android.app.Application;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orhanobut.logger.Logger;

import cn.bmob.newim.BmobIM;

public class MyApplication extends Application {

    private static MyApplication INSTANCE;
    public static MyApplication INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(MyApplication app) {
        setMyApplication(app);
    }
    private static void setMyApplication(MyApplication a) {
        MyApplication.INSTANCE = a;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.mipmap.ic_launcher) //
                .showImageOnFail(R.mipmap.ic_launcher) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)//
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)// 缓存一百张图片
                .writeDebugLogs()//
                .build();//
        ImageLoader.getInstance().init(config);

        setInstance(this);
        //初始化
        Logger.init("smile");
        //im初始化
        BmobIM.init(this);
        //注册消息接收器
        BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
    }
}
