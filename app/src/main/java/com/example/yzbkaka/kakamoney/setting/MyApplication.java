package com.example.yzbkaka.kakamoney.setting;

import android.app.Application;
import android.content.Context;

/**
 * Created by yzbkaka on 20-4-19.
 */

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
