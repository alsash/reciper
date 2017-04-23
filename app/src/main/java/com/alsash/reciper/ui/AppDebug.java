package com.alsash.reciper.ui;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Debugging tools. Don't forget remove this class from manifest on develops finish
 * Created by alsash on 4/23/17.
 */

public class AppDebug extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
