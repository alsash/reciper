package com.alsash.reciper.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.BaseEntity;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.ui.adapter.holder.BaseEntityHolder;
import com.alsash.reciper.ui.adapter.holder.EntityAuthorHolder;
import com.alsash.reciper.ui.adapter.holder.EntityCategoryHolder;
import com.alsash.reciper.ui.adapter.holder.EntityFoodHolder;
import com.alsash.reciper.ui.adapter.holder.EntityLabelHolder;
import com.alsash.reciper.ui.adapter.interaction.EntityListInteraction;
import com.alsash.reciper.ui.adapter.observer.AdapterPositionSetObserver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple adapter for base entity
 */
public class EntityListAdapter extends RecyclerView.Adapter<BaseEntityHolder> {

    private static final int VIEW_TYPE_CATEGORY = 7;
    private static final int VIEW_TYPE_LABEL = 14;
    private static final int VIEW_TYPE_FOOD = 21;
    private static final int VIEW_TYPE_AUTHOR = 28;

    private final EntityListInteraction interaction;
    private final List<BaseEntity> entityList;
    private final Set<Integer> editPositions;
    private final Set<Integer> expandPositions;
    private final int viewType;

    public EntityListAdapter(EntityListInteraction interaction,
                             List<BaseEntity> entityList,
                             Class<?> entityClass) {
        this.interaction = interaction;
        this.entityList = entityList;
        this.editPositions = new HashSet<>();
        this.expandPositions = new HashSet<>();
        registerAdapterDataObserver(new AdapterPositionSetObserver(editPositions, expandPositions));

        if (entityClass.equals(Category.class)) {
            viewType = VIEW_TYPE_CATEGORY;
        } else if (entityClass.equals(Label.class)) {
            viewType = VIEW_TYPE_LABEL;
        } else if (entityClass.equals(Food.class)) {
            viewType = VIEW_TYPE_FOOD;
        } else if (entityClass.equals(Author.class)) {
            viewType = VIEW_TYPE_AUTHOR;
        } else {
            throw new ClassCastException("unknown entity class: " + entityClass.toString());
        }
    }

    @Override
    public BaseEntityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_CATEGORY:
                return new EntityCategoryHolder(parent, R.layout.item_category_entity);
            case VIEW_TYPE_LABEL:
                return new EntityLabelHolder(parent, R.layout.item_label_entity);
            case VIEW_TYPE_FOOD:
                return new EntityFoodHolder(parent, R.layout.item_food_entity);
            case VIEW_TYPE_AUTHOR:
                return new EntityAuthorHolder(parent, R.layout.item_author_entity);
            default:
                return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public void onBindViewHolder(final BaseEntityHolder holder, int position) {
        holder.bindEntity(entityList.get(position));
        holder.setEditable(editPositions.contains(position));

        View.OnClickListener valuesEditListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                boolean editable = !editPositions.remove(position) && editPositions.add(position);
                holder.setEditable(editable);
                if (!editable)
                    interaction.onEditValues(entityList.get(position), holder.getEditable());
            }
        };
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_CATEGORY:
            case VIEW_TYPE_AUTHOR:
                holder.setListeners(valuesEditListener,
                        // Photo edit listener
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int position = holder.getAdapterPosition();
                                interaction.onEditPhoto(entityList.get(position));
                            }
                        }
                );
                break;
            case VIEW_TYPE_LABEL:
                holder.setListeners(valuesEditListener);
                break;
            case VIEW_TYPE_FOOD:
                ((EntityFoodHolder) holder).setExpanded(expandPositions.contains(position), false);
                holder.setListeners(valuesEditListener,
                        // ExpandListener
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int position = holder.getAdapterPosition();
                                boolean expanded = !expandPositions.remove(position)
                                        && expandPositions.add(position);
                                ((EntityFoodHolder) holder).setExpanded(expanded, true);
                            }
                        }
                );
                break;
        }

        // Open Listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (editPositions.contains(position)) return;
                interaction.onOpen(entityList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return entityList.size();
    }
}
