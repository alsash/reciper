package com.alsash.reciper.ui.adapter.holder;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alsash.reciper.mvp.model.entity.BaseEntity;

/**
 * A BaseEntity view holder
 */
public abstract class BaseEntityHolder extends RecyclerView.ViewHolder {

    public BaseEntityHolder(ViewGroup parent, @LayoutRes int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public abstract void bindEntity(BaseEntity entity);

    public abstract void setListeners(Object... listeners);

    public abstract String[] getEditable();

    public abstract void setEditable(boolean editable);
}
