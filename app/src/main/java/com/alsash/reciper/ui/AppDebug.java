package com.alsash.reciper.ui;

import android.app.Application;

import com.alsash.reciper.BuildConfig;
import com.facebook.stetho.Stetho;

import javax.inject.Inject;

/**
 * Debugging tools. Don't forget remove this class from manifest on develops finish
 */
public class AppDebug extends Application {

    @Inject
    public AppDebug() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
