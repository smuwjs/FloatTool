package me.jeeson.android.floattool;

import android.app.Application;

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
