package com.alsash.reciper.data.db.table;

import com.alsash.reciper.api.storage.local.database.table.DaoSession;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A to-many relation model of the Recipe and the Photo entities,
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "RECIPE_PHOTO",
        active = true,
        generateConstructors = false
)
public class RecipePhotoTable {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index(name = "recipe")
    String recipeUuid;
    @Index(name = "photo")
    String photoUuid;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1230626793)
    private transient RecipePhotoTableDao myDao;

    public RecipePhotoTable() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRecipeUuid() {
        return this.recipeUuid;
    }

    public void setRecipeUuid(String recipeUuid) {
        this.recipeUuid = recipeUuid;
    }

    public String getPhotoUuid() {
        return this.photoUuid;
    }

    public void setPhotoUuid(String photoUuid) {
        this.photoUuid = photoUuid;
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
    @Generated(hash = 1610733880)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecipePhotoTableDao() : null;
    }
}
