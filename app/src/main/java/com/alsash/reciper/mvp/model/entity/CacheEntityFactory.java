package com.alsash.reciper.mvp.model.entity;

import android.util.LruCache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

/**
 * An Entity Factory with cache in entity units for controlling the usage of entities
 */
@Singleton
public class CacheEntityFactory {

    private static final Long INITIAL_ID = -1L;
    private static final UUID INITIAL_UUID = new UUID(0, 0);
    private static final String INITIAL_STRING = "";
    private static final Date INITIAL_DATE = new Date(0);
    private static final Category INITIAL_CATEGORY;
    private static final Label INITIAL_LABEL;
    private static final Recipe INITIAL_RECIPE;

    static {
        INITIAL_CATEGORY = new Category(
                INITIAL_ID,
                INITIAL_UUID,
                INITIAL_STRING,
                INITIAL_DATE,
                INITIAL_DATE,
                Collections.<Recipe>emptyList());
        INITIAL_LABEL = new Label(
                INITIAL_ID,
                INITIAL_UUID,
                INITIAL_STRING,
                INITIAL_DATE,
                INITIAL_DATE,
                Collections.<Recipe>emptyList());
        INITIAL_RECIPE = new Recipe(
                INITIAL_ID,
                INITIAL_UUID,
                INITIAL_STRING,
                INITIAL_DATE,
                INITIAL_DATE,
                INITIAL_CATEGORY,
                Collections.<Label>emptyList());
    }

    private final LruCache<UUID, Category> categoriesCache;
    private final LruCache<UUID, Recipe> recipesCache;
    private final LruCache<UUID, Label> labelsCache;

    public CacheEntityFactory(int relativeCacheSize) {
        this.categoriesCache = new LruCache<>((int) (relativeCacheSize * 0.25D));
        this.recipesCache = new LruCache<>((int) (relativeCacheSize * 0.50D));
        this.labelsCache = new LruCache<>((int) (relativeCacheSize * 0.25D));
    }

    public int getRelativeCacheSize() {
        return categoriesCache.size() + recipesCache.size() + labelsCache.size();
    }

    public void clearCache() {
        categoriesCache.evictAll();
        recipesCache.evictAll();
        labelsCache.evictAll();
    }

    public Category getCategory(Long id,
                                UUID uuid,
                                String name,
                                Date creationDate,
                                Date changeDate,
                                List<Recipe> recipes) {

        if (isInitialKey(id, uuid)) return INITIAL_CATEGORY;

        synchronized (categoriesCache) {
            Category category = categoriesCache.get(uuid);
            if (category == null) {
                category = new Category();
                categoriesCache.put(uuid, category);
            }
            createBaseRecipeGroup(category, id, uuid, name, creationDate, changeDate, recipes);
            return category;
        }
    }

    public Label getLabel(Long id,
                          UUID uuid,
                          String name,
                          Date creationDate,
                          Date changeDate,
                          List<Recipe> recipes) {

        if (isInitialKey(id, uuid)) return INITIAL_LABEL;

        synchronized (labelsCache) {
            Label label = labelsCache.get(uuid);
            if (label == null) {
                label = new Label();
                labelsCache.put(uuid, label);
            }
            createBaseRecipeGroup(label, id, uuid, name, creationDate, changeDate, recipes);
            return label;
        }
    }

    public Recipe getRecipe(Long id,
                            UUID uuid,
                            String name,
                            Date creationDate,
                            Date changeDate,
                            Category category,
                            List<Label> labels) {

        if (isInitialKey(id, uuid)) return INITIAL_RECIPE;

        synchronized (recipesCache) {
            Recipe recipe = recipesCache.get(uuid);
            if (recipe == null) {
                recipe = new Recipe();
                recipesCache.put(uuid, recipe);
            }
            createBaseEntity(recipe, id, uuid, name, creationDate, changeDate);
            recipe.category = (category == null) ? INITIAL_CATEGORY : category;
            recipe.labels = (labels == null) ? new ArrayList<Label>() : labels;
            return recipe;
        }
    }

    private boolean isInitialKey(Long id, UUID uuid) {
        if ((id == null)
                || (uuid == null)
                || id.equals(INITIAL_ID)
                || uuid.equals(INITIAL_UUID)) {
            return true;
        } else {
            return false;
        }
    }

    private void createBaseRecipeGroup(BaseRecipeGroup group,
                                       Long id,
                                       UUID uuid,
                                       String name,
                                       Date creationDate,
                                       Date changeDate,
                                       List<Recipe> recipes) {
        createBaseEntity(group, id, uuid, name, creationDate, changeDate);
        group.recipes = (recipes == null) ? new ArrayList<Recipe>() : recipes;
    }

    private void createBaseEntity(BaseEntity entity,
                                  Long id,
                                  UUID uuid,
                                  String name,
                                  Date creationDate,
                                  Date changeDate) {
        entity.id = (id == null) ? INITIAL_ID : id;
        entity.uuid = (uuid == null) ? INITIAL_UUID : uuid;
        entity.name = (name == null) ? INITIAL_STRING : name;
        entity.creationDate = (creationDate == null) ? INITIAL_DATE : creationDate;
        entity.changeDate = (changeDate == null) ? INITIAL_DATE : changeDate;
    }
}
