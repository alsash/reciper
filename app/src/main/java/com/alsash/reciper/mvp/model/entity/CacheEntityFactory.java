package com.alsash.reciper.mvp.model.entity;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.ArrayList;
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

    private final LruEntityByteCache<Category> categoriesCache;
    private final LruEntityByteCache<Label> labelsCache;
    private final LruEntityByteCache<Recipe> recipesCache;
    private final LruEntityByteCache<Photo> photosCache;

    public CacheEntityFactory(int cacheSizeByte) {
        this.categoriesCache = new LruEntityByteCache<>((int) (cacheSizeByte * 0.05D));
        this.recipesCache = new LruEntityByteCache<>((int) (cacheSizeByte * 0.05D));
        this.labelsCache = new LruEntityByteCache<>((int) (cacheSizeByte * 0.05D));
        this.photosCache = new LruEntityByteCache<>((int) (cacheSizeByte * 0.85D));
    }


    public int getSize() {
        return categoriesCache.size()
                + labelsCache.size()
                + recipesCache.size()
                + photosCache.size();
    }

    public void clearCache() {
        categoriesCache.evictAll();
        recipesCache.evictAll();
        labelsCache.evictAll();
        photosCache.evictAll();
    }

    public boolean isInitial(BaseEntity entity) {
        return (entity == null) || isInitial(entity.getId(), entity.getUuid());
    }

    public boolean isInitial(Long id, UUID uuid) {
        return (id == null)
                || (uuid == null)
                || id.equals(INITIAL_ID)
                || uuid.equals(INITIAL_UUID);
    }

    public Category getCategory() {
        return getCategory(null, null, null, null, null, null);
    }

    public Category getCategory(Long id,
                                UUID uuid,
                                String name,
                                Date creationDate,
                                Date changeDate,
                                List<Recipe> recipes) {
        Category category;
        if (isInitial(id, uuid)) {
            category = new Category();
        } else {
            synchronized (categoriesCache) {
                category = categoriesCache.get(uuid);
                if (category == null) {
                    category = new Category();
                    categoriesCache.put(uuid, category);
                }
            }
        }
        getBaseRecipeGroup(category, id, uuid, name, creationDate, changeDate, recipes);
        return category;
    }

    public Label getLabel() {
        return getLabel(null, null, null, null, null, null);
    }

    public Label getLabel(Long id,
                          UUID uuid,
                          String name,
                          Date creationDate,
                          Date changeDate,
                          List<Recipe> recipes) {
        Label label;

        if (isInitial(id, uuid)) {
            label = new Label();
        } else {
            synchronized (labelsCache) {
                label = labelsCache.get(uuid);
                if (label == null) {
                    label = new Label();
                    labelsCache.put(uuid, label);
                }
            }
        }
        getBaseRecipeGroup(label, id, uuid, name, creationDate, changeDate, recipes);
        return label;
    }

    public Recipe getRecipe() {
        return getRecipe(null, null, null, null, null, null, null);
    }

    public Recipe getRecipe(Long id,
                            UUID uuid,
                            String name,
                            Date creationDate,
                            Date changeDate,
                            Category category,
                            List<Label> labels) {
        Recipe recipe;
        if (isInitial(id, uuid)) {
            recipe = new Recipe();
        } else {
            synchronized (recipesCache) {
                recipe = recipesCache.get(uuid);
                if (recipe == null) {
                    recipe = new Recipe();
                    recipesCache.put(uuid, recipe);
                }
            }
        }
        getBaseEntity(recipe, id, uuid, name, creationDate, changeDate);
        recipe.category = (category == null) ? getCategory() : category;
        recipe.labels = (labels == null) ? new ArrayList<Label>() : labels;
        return recipe;
    }

    public Photo getPhoto() {
        return getPhoto(null, null, null, null, null, null, null, null);
    }

    public Photo getPhoto(Long id,
                          UUID uuid,
                          String name,
                          Date creationDate,
                          Date changeDate,
                          String url,
                          String path,
                          Bitmap data) {
        Photo photo;
        if (isInitial(id, uuid)) {
            photo = new Photo();
        } else {
            synchronized (photosCache) {
                photo = photosCache.get(uuid);
                if (photo == null) {
                    photo = new Photo();
                    photosCache.put(uuid, photo);
                }
            }
        }
        getBaseEntity(photo, id, uuid, name, creationDate, changeDate);
        photo.url = (url == null) ? INITIAL_STRING : url;
        photo.path = (path == null) ? INITIAL_STRING : path;
        photo.data = data;
        return photo;
    }

    private void getBaseRecipeGroup(BaseRecipeGroup group,
                                    Long id,
                                    UUID uuid,
                                    String name,
                                    Date creationDate,
                                    Date changeDate,
                                    List<Recipe> recipes) {
        getBaseEntity(group, id, uuid, name, creationDate, changeDate);
        group.recipes = (recipes == null) ? new ArrayList<Recipe>() : recipes;
    }

    private void getBaseEntity(BaseEntity entity,
                               Long id,
                               UUID uuid,
                               String name,
                               Date creationDate,
                               Date changeDate) {
        entity.id = (id == null) ? INITIAL_ID : id;
        entity.uuid = (uuid == null) ? INITIAL_UUID : uuid;
        entity.name = (name == null) ? INITIAL_STRING : name;
        entity.creationDate = (creationDate == null) ? (Date) INITIAL_DATE.clone() : creationDate;
        entity.changeDate = (changeDate == null) ? (Date) INITIAL_DATE.clone() : changeDate;
    }

    private static class LruEntityByteCache<V extends BaseEntity> extends LruCache<UUID, V> {

        public LruEntityByteCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(UUID key, V value) {
            return (2 * (Long.SIZE / Byte.SIZE)) + (value.getSize() / Byte.SIZE);
        }
    }
}
