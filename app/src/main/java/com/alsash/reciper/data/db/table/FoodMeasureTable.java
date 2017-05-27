package com.alsash.reciper.data.db.table;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A to-many relation model of the Food entity and its custom measures,
 * persists in local relational database table with the help of GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "FOOD_MEASURE",
        active = true
)
public final class FoodMeasureTable {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index(name = "food", unique = true)
    String foodUuid;
    String unit;
    @NotNull
    double weight;
    String weightUnit;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 853739489)
    private transient FoodMeasureTableDao myDao;

    @Generated(hash = 169106229)
    public FoodMeasureTable(Long id, String uuid, String foodUuid, String unit,
                            double weight, String weightUnit) {
        this.id = id;
        this.uuid = uuid;
        this.foodUuid = foodUuid;
        this.unit = unit;
        this.weight = weight;
        this.weightUnit = weightUnit;
    }

    @Generated(hash = 285372152)
    public FoodMeasureTable() {
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

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return this.weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
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
    @Generated(hash = 750727052)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFoodMeasureTableDao() : null;
}
}
