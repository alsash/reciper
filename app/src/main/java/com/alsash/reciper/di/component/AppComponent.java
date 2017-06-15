package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.AppContextModule;
import com.alsash.reciper.di.module.AppDataCloudModule;
import com.alsash.reciper.di.module.AppDataDbModule;
import com.alsash.reciper.di.module.AppGlideModule;
import com.alsash.reciper.di.module.AppHttpModule;
import com.alsash.reciper.di.module.AppLogicModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * DI Component that build bridge between Application and its components
 */
@Singleton // Scope
@Component(
        modules = {
                AppContextModule.class,
                AppDataCloudModule.class,
                AppDataDbModule.class,
                AppLogicModule.class,
                AppGlideModule.class,
                AppHttpModule.class
        }
)
public interface AppComponent {

    void inject(AppGlideModule appGlideModule);

    UiStartComponent.Builder getStartComponentBuilder();

    UiRecipeCollectionsComponent.Builder getRecipeCollectionsComponentBuilder();

    UiRecipeSearchComponent.Builder getRecipeSearchComponentBuilder();
}
