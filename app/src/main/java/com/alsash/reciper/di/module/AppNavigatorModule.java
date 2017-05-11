package com.alsash.reciper.di.module;

import android.content.Context;

import com.alsash.reciper.app.AppNavigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide the Navigation class for navigate between application views
 */
@Module
public class AppNavigatorModule {

    @Provides
    @Singleton
    static AppNavigator provideAppNavigator(Context context) {
        return new AppNavigator(context);
    }
}
