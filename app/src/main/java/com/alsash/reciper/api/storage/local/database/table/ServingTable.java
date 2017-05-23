package com.alsash.reciper.api.storage.local.database.table;


import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;

/**
 * Local relational database table that represents the Serving entity
 */
@Entity(nameInDb = "SERVING",
        active = true,
        generateConstructors = false
)
public class ServingTable {
    @Id
    private Long id;
    @Unique
    @Property(nameInDb = "uuid")
    @SerializedName("uuid")
    private String _uuid;
    @Unique
    private String recipeUuid;
    @NotNull
    private int quantity;
    @NotNull
    private Date time;
    @ToMany(joinProperties = {
            @JoinProperty(name = "recipeUuid", referencedName = "uuid")}
    )
    private List<RecipeTable> recipeTables;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1824469895)
    private transient ServingTableDao myDao;

    public ServingTable() {
    }

    public ServingTable(Long id, String _uuid, String recipeUuid, int quantity, Date time, List<RecipeTable> recipeTables) {
        this.id = id;
        this._uuid = _uuid;
        this.recipeUuid = recipeUuid;
        this.quantity = quantity;
        this.time = time;
        this.recipeTables = recipeTables;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String get_uuid() {
        return _uuid;
    }

    public void set_uuid(String _uuid) {
        this._uuid = _uuid;
    }

    public String getRecipeUuid() {
        return recipeUuid;
    }

    public void setRecipeUuid(String recipeUuid) {
        this.recipeUuid = recipeUuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1725695046)
    public List<RecipeTable> getRecipeTables() {
        if (recipeTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeTableDao targetDao = daoSession.getRecipeTableDao();
            List<RecipeTable> recipeTablesNew = targetDao._queryServingTable_RecipeTables(recipeUuid);
            synchronized (this) {
                if (recipeTables == null) {
                    recipeTables = recipeTablesNew;
                }
            }
        }
        return recipeTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1823856706)
    public synchronized void resetRecipeTables() {
        recipeTables = null;
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
    @Generated(hash = 1800329538)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getServingTableDao() : null;
    }

}
