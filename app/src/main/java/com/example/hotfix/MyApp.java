package com.example.hotfix;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApp extends Application {

    private static final String TAG = "MyApp";
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.e(TAG,"classLoader:"+this.getClassLoader());
        HotFixManager.installFixedDex(this);
    }
}
