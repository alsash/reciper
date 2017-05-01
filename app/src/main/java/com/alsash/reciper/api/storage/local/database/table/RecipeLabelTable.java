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
    private UUID guid;

    @Index
    private Long recipeId;

    @Convert(converter = GuidConverter.class, columnType = String.class)
    private UUID recipeGuid;

    @Index
    private Long labelId;

    @Convert(converter = GuidConverter.class, columnType = String.class)
    private UUID labelGuid;

    @Generated(hash = 735888301)
    public RecipeLabelTable(Long id, UUID guid, Long recipeId, UUID recipeGuid,
                            Long labelId, UUID labelGuid) {
        this.id = id;
        this.guid = guid;
        this.recipeId = recipeId;
        this.recipeGuid = recipeGuid;
        this.labelId = labelId;
        this.labelGuid = labelGuid;
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

    public UUID getGuid() {
        return this.guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public Long getRecipeId() {
        return this.recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public UUID getRecipeGuid() {
        return this.recipeGuid;
    }

    public void setRecipeGuid(UUID recipeGuid) {
        this.recipeGuid = recipeGuid;
    }

    public Long getLabelId() {
        return this.labelId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public UUID getLabelGuid() {
        return this.labelGuid;
    }

    public void setLabelGuid(UUID labelGuid) {
        this.labelGuid = labelGuid;
    }
}
