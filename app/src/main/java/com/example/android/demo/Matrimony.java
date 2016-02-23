package com.example.android.demo;

import android.app.Application;
import android.content.Context;

public class Matrimony extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        Matrimony.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return Matrimony.context;
    }
}