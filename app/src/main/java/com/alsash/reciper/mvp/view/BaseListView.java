package com.alsash.reciper.mvp.view;

import java.util.List;

/**
 * A BaseListView interface, that represents Model of data,
 * which can be attached to BaseListPresenter.
 */
public interface BaseListView<M> extends BaseView {

    void setContainer(List<M> container);

    void setPagination(boolean needPagination);

    void showLoading(boolean loading);

    void showInsert(int position);

    void showInsert(int insertPosition, int insertCount);

    void showUpdate(int position);

    void showUpdate();

    void showDelete(int position);
}
