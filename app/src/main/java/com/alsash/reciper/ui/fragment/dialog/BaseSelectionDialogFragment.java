package com.alsash.reciper.ui.fragment.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.presenter.BaseSelectionDialogPresenter;
import com.alsash.reciper.mvp.view.BaseSelectionDialogView;
import com.alsash.reciper.ui.adapter.EntitySelectionAdapter;
import com.alsash.reciper.ui.adapter.interaction.EntitySelectionInteraction;

import java.util.List;
import java.util.Set;

/**
 * Complex item selection dialog
 */
public abstract class BaseSelectionDialogFragment
        <M extends BaseEntity, V extends BaseSelectionDialogView<M>>
        extends BaseDialogListFragment<M, V>
        implements BaseSelectionDialogView<M>, EntitySelectionInteraction {

    private BaseSelectionDialogPresenter<M, V> presenter;
    private EntitySelectionAdapter adapter;

    @Override
    public void onSelect(BaseEntity entity) {
        presenter.select(entity.getUuid());
    }

    @Override
    public void setSelection(boolean multi, Set<String> uuid) {
        if (adapter != null) adapter.setSelection(multi, uuid);
    }

    @Override
    public void finishView() {
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter = getThisPresenter();
    }

    protected abstract Class<?> getEntityClass();

    @Override
    protected RecyclerView.Adapter getAdapter(List<M> container) {
        adapter = new EntitySelectionAdapter(this, container, getEntityClass());
        return adapter;
    }

    protected void clickPositive() {
        presenter.approve(getThisView());
    }

    protected void clickNegative() {
        dismiss();
    }

}
