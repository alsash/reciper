package com.alsash.reciper.di.module;

import com.alsash.reciper.di.scope.EntityListScope;
import com.alsash.reciper.logic.BusinessLogic;
import com.alsash.reciper.logic.StorageLogic;
import com.alsash.reciper.mvp.presenter.EntityListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * DI Module that provide presenters for views, that represent list of entities (except recipes).
 */
@Module
public abstract class UiEntityListModule {

    @Provides
    @EntityListScope
    static EntityListPresenter provideEntityListPresenter(StorageLogic storageLogic,
                                                          BusinessLogic businessLogic) {
        return new EntityListPresenter(storageLogic, businessLogic);
    }

}
