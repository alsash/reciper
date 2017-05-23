package com.alsash.reciper.api.storage.local.database.table;

import com.alsash.reciper.api.storage.local.database.converter.UuidConverter;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Local relational database table that represents the Label entity
 */
@Entity(nameInDb = "LABEL")
public class LabelTable {
    @Id
    private Long id;
    @Unique
    @Convert(converter = UuidConverter.class, columnType = String.class)
    private UUID uuid;
    private String name;
    private Date creationDate;
    private Date changeDate;
    @ToMany
    @JoinEntity(
            entity = RecipeLabelTable.class,
            sourceProperty = "labelId",
            targetProperty = "recipeId"
    )
    private List<RecipeTable> recipes;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2034547187)
    private transient LabelTableDao myDao;


    @Generated(hash = 1487967186)
    public LabelTable(Long id, UUID uuid, String name, Date creationDate,
                      Date changeDate) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.creationDate = creationDate;
        this.changeDate = changeDate;
    }
    @Generated(hash = 893165010)
    public LabelTable() {
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
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Date getCreationDate() {
        return this.creationDate;
    }
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    public Date getChangeDate() {
        return this.changeDate;
    }
    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1456745068)
    public List<RecipeTable> getRecipes() {
        if (recipes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeTableDao targetDao = daoSession.getRecipeTableDao();
            List<RecipeTable> recipesNew = targetDao._queryLabelTable_Recipes(id);
            synchronized (this) {
                if (recipes == null) {
                    recipes = recipesNew;
                }
            }
        }
        return recipes;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 2094593178)
    public synchronized void resetRecipes() {
        recipes = null;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 111975173)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLabelTableDao() : null;
    }
}
