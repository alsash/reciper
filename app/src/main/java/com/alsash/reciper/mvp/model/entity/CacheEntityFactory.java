package com.alsash.reciper.mvp.model.entity;

import android.util.LruCache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Singleton;

/**
 * An Entity Factory with cache for controlling the usage of entities and prevent NPE on UI
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

    private int maxCacheSizeByte;
    private boolean createNewEntities;

    public CacheEntityFactory(int maxCacheSizeByte) {
        this.maxCacheSizeByte = maxCacheSizeByte;
        this.categoriesCache = new LruEntityByteCache<>((int) (maxCacheSizeByte * 0.05D));
        this.recipesCache = new LruEntityByteCache<>((int) (maxCacheSizeByte * 0.05D));
        this.labelsCache = new LruEntityByteCache<>((int) (maxCacheSizeByte * 0.05D));
        this.photosCache = new LruEntityByteCache<>((int) (maxCacheSizeByte * 0.85D));
    }

    public void setCreateNewEntities(boolean createNewEntities) {
        this.createNewEntities = createNewEntities;
    }

    public int getMaxCacheSizeByte() {
        return maxCacheSizeByte;
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
        return (entity == null)
                || (entity.getId() == null)
                || (entity.getUuid() == null)
                || entity.getId().equals(INITIAL_ID)
                || entity.getUuid().equals(INITIAL_UUID);
    }

    public Category getCategory() {
        return getCategory(null, null, null, null, null, null);
    }

    public Category getCategory(Long id,
                                UUID uuid,
                                String name,
                                Date creationDate,
                                Date changeDate,
                                Photo photo) {

        Category category = (uuid == null) ? null : categoriesCache.get(uuid);
        if (category == null) category = new Category();

        getBaseEntity(category, id, uuid, name, creationDate, changeDate);
        category.photo = (photo == null) ? getPhoto() : photo;

        categoriesCache.put(category.getUuid(), category);
        return category;
    }

    public Label getLabel() {
        return getLabel(null, null, null, null, null);
    }

    public Label getLabel(Long id,
                          UUID uuid,
                          String name,
                          Date creationDate,
                          Date changeDate) {

        Label label = (uuid == null) ? null : labelsCache.get(uuid);
        if (label == null) label = new Label();

        getBaseEntity(label, id, uuid, name, creationDate, changeDate);

        labelsCache.put(label.getUuid(), label);
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

        Recipe recipe = (uuid == null) ? null : recipesCache.get(uuid);
        if (recipe == null) recipe = new Recipe();

        getBaseEntity(recipe, id, uuid, name, creationDate, changeDate);
        recipe.category = (category == null) ? getCategory() : category;
        recipe.labels = (labels == null) ? new ArrayList<Label>() : labels;

        recipesCache.put(recipe.getUuid(), recipe);
        return recipe;
    }

    public Photo getPhoto() {
        return getPhoto(null, null, null, null, null, null, null);
    }

    public Photo getPhoto(Long id,
                          UUID uuid,
                          String name,
                          Date creationDate,
                          Date changeDate,
                          String url,
                          String path) {

        Photo photo = (uuid == null) ? null : photosCache.get(uuid);
        if (photo == null) photo = new Photo();

        getBaseEntity(photo, id, uuid, name, creationDate, changeDate);
        photo.url = (url == null) ? INITIAL_STRING : url;
        photo.path = (path == null) ? INITIAL_STRING : path;

        photosCache.put(photo.getUuid(), photo);
        return photo;
    }

    private void getBaseEntity(BaseEntity entity,
                               Long id,
                               UUID uuid,
                               String name,
                               Date creationDate,
                               Date changeDate) {
        if (createNewEntities) {
            entity.id = (id == null) ? 0 : id;
            entity.uuid = (uuid == null) ? UUID.randomUUID() : uuid;
        } else {
            entity.id = (id == null) ? INITIAL_ID : id;
            entity.uuid = (uuid == null) ? INITIAL_UUID : uuid;
        }
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
