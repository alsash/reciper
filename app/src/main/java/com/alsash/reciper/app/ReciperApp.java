package com.alsash.reciper.app;

import android.app.Application;

import com.alsash.reciper.BuildConfig;
import com.alsash.reciper.di.component.AppComponent;
import com.alsash.reciper.di.component.DaggerAppComponent;
import com.alsash.reciper.di.component.RecipeListComponent;
import com.alsash.reciper.di.component.StartComponent;
import com.alsash.reciper.di.module.AppContextModule;
import com.facebook.stetho.Stetho;

/**
 * RecipeR Application class
 */
public class ReciperApp extends Application {

    private StartComponent startComponent;
    private RecipeListComponent recipeListComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Google Dagger 2 initialization
        AppComponent appComponent = DaggerAppComponent.builder()
                .appContextModule(new AppContextModule(this))
                .build();
        startComponent = appComponent
                .getStartComponentBuilder()
                .build();
        recipeListComponent = appComponent
                .getRecipeListComponentBuilder()
                .build();

        // Facebook Stetho initialization
        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this);
    }

    public StartComponent getStartComponent() {
        return startComponent;
    }

    public RecipeListComponent getRecipeListComponent() {
        return recipeListComponent;
    }
}
