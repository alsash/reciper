package com.alsash.reciper.data.db.table;

import com.alsash.reciper.api.storage.local.database.table.DaoSession;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A to-many relation model of the Food entity and its id at USDA Food Composition Databases,
 * persists in local relational database table with the help of GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "FOOD_USDA",
        active = true,
        generateConstructors = false
)
public class FoodUsdaTable {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index(name = "food", unique = true)
    String foodUuid;
    String usdaNdbNo;
    @NotNull
    boolean fetched;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 531675553)
    private transient FoodUsdaTableDao myDao;

    public FoodUsdaTable() {
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

    public String getFoodUuid() {
        return this.foodUuid;
    }

    public void setFoodUuid(String foodUuid) {
        this.foodUuid = foodUuid;
    }

    public String getUsdaNdbNo() {
        return this.usdaNdbNo;
    }

    public void setUsdaNdbNo(String usdaNdbNo) {
        this.usdaNdbNo = usdaNdbNo;
    }

    public boolean getFetched() {
        return this.fetched;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
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
    @Generated(hash = 2079366477)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFoodUsdaTableDao() : null;
    }
}
