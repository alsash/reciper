package com.alsash.reciper.mvp.presenter;

import com.alsash.reciper.database.entity.DaoSession;
import com.alsash.reciper.mvp.view.StartView;

/**
 * Created by alsash on 4/30/17.
 */

public class StartPresenter extends BasePresenter<StartView> {

    private final DaoSession daoSession;

    public StartPresenter(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    @Override
    public void initView() {

    }

    @Override
    public void completeView() {

    }
}
