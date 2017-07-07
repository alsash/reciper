package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.alsash.reciper.mvp.model.entity.BaseEntity;

/**
 * A Base Entity selection view holder
 */
public abstract class BaseEntitySelectionHolder extends RecyclerView.ViewHolder {

    private CheckBox checkBox;

    public BaseEntitySelectionHolder(ViewGroup parent,
                                     @LayoutRes int layoutId,
                                     @Nullable @IdRes Integer checkboxId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        if (checkboxId != null) checkBox = (CheckBox) itemView.findViewById(checkboxId);
    }

    public abstract void bindEntity(BaseEntity entity);

    public void setChecked(boolean checked) {
        if (checkBox != null) checkBox.setChecked(checked);
    }
}
