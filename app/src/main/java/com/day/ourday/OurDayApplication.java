package com.day.ourday;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


/**
 * Created by LimerenceT on 19-8-1
 */
public class OurDayApplication extends Application {
    private static OurDayApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    public static OurDayApplication getInstance() {
        return instance;
    }
}
