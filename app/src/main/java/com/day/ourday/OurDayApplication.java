package com.day.ourday;

import android.app.Application;

/**
 * Created by LimerenceT on 19-8-1
 */
public class OurDayApplication extends Application {
    private static OurDayApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static OurDayApplication getInstance() {
        return instance;
    }
}
