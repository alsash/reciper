package com.alsash.reciper.api.storage.local.database.table;

import com.alsash.reciper.api.storage.local.database.converter.GuidConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

import java.util.UUID;

/**
 * Local database table that holds relations between recipes and labels
 */
@Entity(nameInDb = "RECIPE_LABEL")
public class RecipeLabelTable {

    @Id
    private Long id;

    @Unique
    @Convert(converter = GuidConverter.class, columnType = String.class)
    private UUID uuid;

    @Index
    private Long recipeId;

    @Convert(converter = GuidConverter.class, columnType = String.class)
    private UUID recipeUuid;

    @Index
    private Long labelId;

    @Convert(converter = GuidConverter.class, columnType = String.class)
    private UUID labelUuid;

    @Generated(hash = 1263246042)
    public RecipeLabelTable(Long id, UUID uuid, Long recipeId, UUID recipeUuid,
                            Long labelId, UUID labelUuid) {
        this.id = id;
        this.uuid = uuid;
        this.recipeId = recipeId;
        this.recipeUuid = recipeUuid;
        this.labelId = labelId;
        this.labelUuid = labelUuid;
    }

    @Generated(hash = 2053410462)
    public RecipeLabelTable() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Long getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public UUID getRecipeUuid() {
        return this.recipeUuid;
    }

    public void setRecipeUuid(UUID recipeUuid) {
        this.recipeUuid = recipeUuid;
    }

    public Long getLabelId() {
        return this.labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public UUID getLabelUuid() {
        return this.labelUuid;
    }

    public void setLabelUuid(UUID labelUuid) {
        this.labelUuid = labelUuid;
    }

}
