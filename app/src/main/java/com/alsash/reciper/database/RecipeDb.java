package com.alsash.reciper.database;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

/**
 * Local database table that holds recipes
 */
@Entity(nameInDb = "RECIPE",
        indexes = {
                @Index(value = "name", unique = true)
        })
public class RecipeDb {
    @Id
    private long id;
    private String name;
    private long categoryId;
    @ToOne(joinProperty = "categoryId")
    private CategoryDb category;
    @ToMany
    @JoinEntity(
            entity = RecipeLabelJoinDb.class,
            sourceProperty = "recipeId",
            targetProperty = "labelId"
    )
    private List<LabelDb> labels;
// Next will be generated sources
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 261850341)
    private transient RecipeDbDao myDao;
    @Generated(hash = 1372501278)
    private transient Long category__resolvedKey;

    @Generated(hash = 1019593814)
    public RecipeDb(long id, String name, long categoryId) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
    }

    @Generated(hash = 1146901377)
    public RecipeDb() {
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

    public long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 715424162)
    public CategoryDb getCategory() {
        long __key = this.categoryId;
        if (category__resolvedKey == null || !category__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CategoryDbDao targetDao = daoSession.getCategoryDbDao();
            CategoryDb categoryNew = targetDao.load(__key);
            synchronized (this) {
                category = categoryNew;
                category__resolvedKey = __key;
            }
        }
        return category;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 413251678)
    public void setCategory(@NotNull CategoryDb category) {
        if (category == null) {
            throw new DaoException(
                    "To-one property 'categoryId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.category = category;
            categoryId = category.getId();
            category__resolvedKey = categoryId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 998110413)
    public List<LabelDb> getLabels() {
        if (labels == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelDbDao targetDao = daoSession.getLabelDbDao();
            List<LabelDb> labelsNew = targetDao._queryRecipeDb_Labels(id);
            synchronized (this) {
                if (labels == null) {
                    labels = labelsNew;
                }
            }
        }
        return labels;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 902294403)
    public synchronized void resetLabels() {
        labels = null;
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
    @Generated(hash = 115976254)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecipeDbDao() : null;
    }
}
