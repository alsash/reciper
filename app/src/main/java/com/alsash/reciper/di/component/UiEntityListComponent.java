package com.alsash.reciper.di.component;

import com.alsash.reciper.di.module.UiEntityListModule;
import com.alsash.reciper.di.scope.EntityListScope;
import com.alsash.reciper.ui.fragment.EntityListFragment;

import dagger.Subcomponent;

/**
 * DI Component that build bridge between EntityListFragment and its dependencies
 */
@EntityListScope
@Subcomponent(
        modules = {
                UiEntityListModule.class
        }
)
public interface UiEntityListComponent {

    void inject(EntityListFragment entityListFragment);

    @Subcomponent.Builder
    interface Builder {
        UiEntityListComponent build();
    }
}
