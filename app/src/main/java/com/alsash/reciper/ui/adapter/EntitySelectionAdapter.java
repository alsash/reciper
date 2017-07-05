package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.ui.adapter.holder.BaseEntitySelectionHolder;
import com.alsash.reciper.ui.adapter.holder.EntityAuthorSelectionHolder;
import com.alsash.reciper.ui.adapter.holder.EntityCategorySelectionHolder;
import com.alsash.reciper.ui.adapter.interaction.EntitySelectionInteraction;
import com.alsash.reciper.ui.adapter.observer.AdapterPositionSetObserver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple entity selection adapter
 */
public class EntitySelectionAdapter extends RecyclerView.Adapter<BaseEntitySelectionHolder> {

    private static final int VIEW_TYPE_CATEGORY = 7;
    private static final int VIEW_TYPE_AUTHOR = 28;

    public final EntitySelectionInteraction interaction;
    public final List<? extends BaseEntity> entities;
    public final Set<Integer> selectedPositions;
    private final boolean multiSelect;
    private final int viewType;

    private String selectedEntityUuid;

    public EntitySelectionAdapter(EntitySelectionInteraction interaction,
                                  List<? extends BaseEntity> entities,
                                  Class<?> entityClass,
                                  boolean multiSelect) {
        this.interaction = interaction;
        this.entities = entities;
        this.multiSelect = multiSelect;
        this.selectedPositions = new HashSet<>();
        registerAdapterDataObserver(new AdapterPositionSetObserver(selectedPositions));
        if (entityClass.equals(Category.class)) {
            viewType = VIEW_TYPE_CATEGORY;
        } else if (entityClass.equals(Author.class)) {
            viewType = VIEW_TYPE_AUTHOR;
        } else {
            throw new ClassCastException("unknown entity class: " + entityClass.toString());
        }
    }

    public EntitySelectionAdapter(EntitySelectionInteraction interaction,
                                  List<? extends BaseEntity> entities,
                                  Class<?> entityClass) {
        this(interaction, entities, entityClass, false);
    }

    public void setSelectedEntity(String entityUuid) {
        selectedEntityUuid = entityUuid;
        for (BaseEntity entity : entities) {
            if (entity.getUuid().equals(selectedEntityUuid)) {
                notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public BaseEntitySelectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_CATEGORY:
                return new EntityCategorySelectionHolder(parent, R.layout.item_category_selection);
            case VIEW_TYPE_AUTHOR:
                return new EntityAuthorSelectionHolder(parent, R.layout.item_author_selection);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public void onBindViewHolder(final BaseEntitySelectionHolder holder, int position) {
        checkSelectionSet(position);
        holder.bindEntity(entities.get(position));
        holder.setChecked(selectedPositions.contains(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (selectedPositions.contains(position)) return;
                if (!multiSelect) selectedPositions.clear();
                selectedPositions.add(position);
                holder.setChecked(true);
                if (!multiSelect) notifyDataSetChanged();
                interaction.onSelect(entities.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    private void checkSelectionSet(int position) {
        if (selectedEntityUuid == null) return;
        if (entities.get(position).getUuid().equals(selectedEntityUuid)) {
            if (!multiSelect) selectedPositions.clear();
            selectedPositions.add(position);
            selectedEntityUuid = null;
        }
    }
}
