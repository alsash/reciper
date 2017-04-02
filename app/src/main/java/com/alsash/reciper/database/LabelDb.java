package com.alsash.reciper.database;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Local database table that holds labels of recipes
 */
@Entity(nameInDb = "label",
        indexes = {
                @Index(value = "name", unique = true)
        })
public class LabelDb {
    @Id
    private long id;
    private String name;
    @ToMany
    @JoinEntity(
            entity = RecipeLabelJoinDb.class,
            sourceProperty = "labelId",
            targetProperty = "recipeId"
    )
    private List<RecipeDb> recipes;
// Next will be generated sources
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1661656670)
    private transient LabelDbDao myDao;

    @Generated(hash = 273813516)
    public LabelDb(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 2045590666)
    public LabelDb() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1981545151)
    public List<RecipeDb> getRecipes() {
        if (recipes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeDbDao targetDao = daoSession.getRecipeDbDao();
            List<RecipeDb> recipesNew = targetDao._queryLabelDb_Recipes(id);
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
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1087470678)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLabelDbDao() : null;
    }
}
