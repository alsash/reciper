package com.alsash.reciper.app;

import android.app.Application;

import com.alsash.reciper.BuildConfig;
import com.alsash.reciper.di.component.AppComponent;
import com.alsash.reciper.di.component.DaggerAppComponent;
import com.alsash.reciper.di.component.UiEntityListComponent;
import com.alsash.reciper.di.component.UiRecipeCollectionsComponent;
import com.alsash.reciper.di.component.UiRecipeCreationComponent;
import com.alsash.reciper.di.component.UiRecipeDetailsComponent;
import com.alsash.reciper.di.component.UiRecipeRestrictListComponent;
import com.alsash.reciper.di.component.UiStartComponent;
import com.alsash.reciper.di.module.AppContextModule;
import com.facebook.stetho.Stetho;

/**
 * RecipeR Application class
 */
public class ReciperApp extends Application {

    private AppComponent appComponent;
    private UiStartComponent uiStartComponent;
    private UiEntityListComponent uiEntityListComponent;
    private UiRecipeCollectionsComponent uiRecipeCollectionsComponent;
    private UiRecipeRestrictListComponent uiRecipeRestrictListComponent;
    private UiRecipeDetailsComponent uiRecipeDetailsComponent;
    private UiRecipeCreationComponent uiRecipeCreationComponent;

    /**
     * Initializing all components while splash (StartActivity) is shown.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Google Dagger 2 initialization.
        appComponent = DaggerAppComponent.builder()
                .appContextModule(new AppContextModule(this))
                .build();
        uiStartComponent = appComponent
                .getStartComponentBuilder()
                .build();
        uiEntityListComponent = appComponent
                .getEntityListComponentBuilder()
                .build();
        uiRecipeCollectionsComponent = appComponent
                .getRecipeCollectionsComponentBuilder()
                .build();
        uiRecipeRestrictListComponent = appComponent
                .getRecipeRestrictListComponentBuilder()
                .build();
        uiRecipeDetailsComponent = appComponent
                .getRecipeDetailsComponentBuilder()
                .build();
        uiRecipeCreationComponent = appComponent
                .getUiRecipeCreationComponentBuilder()
                .build();

        // Facebook Stetho initialization
        if (BuildConfig.DEBUG) Stetho.initializeWithDefaults(this);

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public UiStartComponent getUiStartComponent() {
        return uiStartComponent;
    }

    public UiEntityListComponent getUiEntityListComponent() {
        return uiEntityListComponent;
    }

    public UiRecipeCollectionsComponent getUiRecipeCollectionsComponent() {
        return uiRecipeCollectionsComponent;
    }

    public UiRecipeRestrictListComponent getUiRecipeRestrictListComponent() {
        return uiRecipeRestrictListComponent;
    }

    public UiRecipeDetailsComponent getUiRecipeDetailsComponent() {
        return uiRecipeDetailsComponent;
    }

    public UiRecipeCreationComponent getUiRecipeCreationComponent() {
        return uiRecipeCreationComponent;
    }
}
