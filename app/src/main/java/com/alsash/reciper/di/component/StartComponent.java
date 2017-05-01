package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.StartModule;
import com.alsash.reciper.di.scope.StartScope;
import com.alsash.reciper.ui.activity.StartActivity;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between StartActivity and its dependencies
 */
@StartScope
@Subcomponent(modules = {StartModule.class})
public interface StartComponent {

    void inject(StartActivity startActivity);

    @Subcomponent.Builder
    interface Builder {
        StartComponent build();
    }
}
