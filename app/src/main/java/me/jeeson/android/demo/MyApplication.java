package me.jeeson.android.demo;

import android.app.Application;

import me.jeeson.android.floattool.FloatManager;

/**
 * Created by Jeeson on 2018/5/18.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(FloatManager.getInstance());
    }
}
