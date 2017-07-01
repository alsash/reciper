package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiStartModule;
import com.alsash.reciper.di.scope.StartScope;
import com.alsash.reciper.ui.activity.FragmentCollectionActivity;
import com.alsash.reciper.ui.activity.FragmentSingleActivity;
import com.alsash.reciper.ui.activity.StartActivity;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between StartActivity and its dependencies
 */
@StartScope
@Subcomponent(
        modules = {
                UiStartModule.class
        }
)
public interface UiStartComponent {

    void inject(StartActivity startActivity);

    void inject(FragmentCollectionActivity fragmentCollectionActivity);

    void inject(FragmentSingleActivity fragmentSingleActivity);

    @Subcomponent.Builder
    interface Builder {
        UiStartComponent build();
    }
}
