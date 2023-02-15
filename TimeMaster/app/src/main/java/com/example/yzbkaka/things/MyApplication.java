package com.example.yzbkaka.things;

import android.app.Application;

import org.litepal.LitePalApplication;

import jackmego.com.jieba_android.JiebaSegmenter;

public class MyApplication extends LitePalApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        // 异步初始化
        JiebaSegmenter.init(getApplicationContext());
    }
}
