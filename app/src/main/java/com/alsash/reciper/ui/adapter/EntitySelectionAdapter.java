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
import com.alsash.reciper.ui.adapter.holder.BaseEntitySelectionHolder;
import com.alsash.reciper.ui.adapter.holder.EntityAuthorSelectionHolder;
import com.alsash.reciper.ui.adapter.holder.EntityCategorySelectionHolder;
import com.alsash.reciper.ui.adapter.holder.EntityFoodSelectionHolder;
import com.alsash.reciper.ui.adapter.holder.EntityLabelSelectionHolder;
import com.alsash.reciper.ui.adapter.interaction.EntitySelectionInteraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple entity selection adapter
 */
public class EntitySelectionAdapter extends RecyclerView.Adapter<BaseEntitySelectionHolder> {

    private static final int VIEW_TYPE_CATEGORY = 7;
    private static final int VIEW_TYPE_LABEL = 14;
    private static final int VIEW_TYPE_FOOD = 21;
    private static final int VIEW_TYPE_AUTHOR = 28;

    private final EntitySelectionInteraction interaction;
    private final List<? extends BaseEntity> entities;
    private final int viewType;
    private boolean multiSelect;
    private Set<String> selectedUuid;
    private Set<String> expandedUuid;

    public EntitySelectionAdapter(EntitySelectionInteraction interaction,
                                  List<? extends BaseEntity> entities,
                                  Class<?> entityClass) {
        this.interaction = interaction;
        this.entities = entities;
        this.multiSelect = false;
        this.selectedUuid = new HashSet<>();
        this.expandedUuid = new HashSet<>();
        if (entityClass.equals(Category.class)) {
            viewType = VIEW_TYPE_CATEGORY;
        } else if (entityClass.equals(Author.class)) {
            viewType = VIEW_TYPE_AUTHOR;
        } else if (entityClass.equals(Label.class)) {
            viewType = VIEW_TYPE_LABEL;
        } else if (entityClass.equals(Food.class)) {
            viewType = VIEW_TYPE_FOOD;
        } else {
            throw new ClassCastException("unknown entity class: " + entityClass.toString());
        }
    }

    public void setSelection(boolean multiSelect, Set<String> uuid) {
        this.multiSelect = multiSelect;
        uuid.addAll(selectedUuid);
        this.selectedUuid = uuid;
        notifyDataSetChanged();
    }

    @Override
    public BaseEntitySelectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_CATEGORY:
                return new EntityCategorySelectionHolder(parent, R.layout.item_category_selection);
            case VIEW_TYPE_AUTHOR:
                return new EntityAuthorSelectionHolder(parent, R.layout.item_author_selection);
            case VIEW_TYPE_LABEL:
                return new EntityLabelSelectionHolder(parent, R.layout.item_label_selection);
            case VIEW_TYPE_FOOD:
                return new EntityFoodSelectionHolder(parent, R.layout.item_food_entity);
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
        BaseEntity entity = entities.get(position);
        holder.bindEntity(entity);
        holder.setChecked(selectedUuid.contains(entity.getUuid()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                BaseEntity entity = entities.get(position);
                String uuid = entity.getUuid();
                checkSelectedPositions(uuid);
                if (multiSelect) {
                    holder.setChecked(selectedUuid.contains(uuid));
                } else {
                    notifyDataSetChanged();
                }
                interaction.onSelect(entity);
            }
        });
        if (holder.getItemViewType() == VIEW_TYPE_FOOD) {
            final EntityFoodSelectionHolder foodHolder = (EntityFoodSelectionHolder) holder;
            foodHolder.setExpanded(expandedUuid.contains(entity.getUuid()), false);
            foodHolder.setExpandListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String uuid = entities.get(foodHolder.getAdapterPosition()).getUuid();
                    boolean expanded = !expandedUuid.remove(uuid)
                            && expandedUuid.add(uuid);
                    foodHolder.setExpanded(expanded, true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    private void checkSelectedPositions(String uuid) {
        if (!multiSelect) selectedUuid.clear();
        if (multiSelect && selectedUuid.contains(uuid)) {
            selectedUuid.remove(uuid);
        } else {
            selectedUuid.add(uuid);
        }
    }
}
