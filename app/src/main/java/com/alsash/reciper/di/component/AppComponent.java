package com.alsash.reciper.di.component;

import android.content.Context;

import com.alsash.reciper.database.ApiDatabase;
import com.alsash.reciper.di.module.AppContextModule;
import com.alsash.reciper.di.module.AppDataModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * DI Component that build bridge between Application and its dependencies
 */
@Singleton // Scope
@Component(modules = {AppContextModule.class, AppDataModule.class})
public interface AppComponent {

    StartComponent.Builder getStartComponentBuilder();

    Context getAppContext();

    ApiDatabase getApiDatabase();
}
