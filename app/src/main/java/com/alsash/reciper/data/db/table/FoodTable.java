package com.alsash.reciper.data.db.table;

import com.alsash.reciper.api.storage.local.database.table.DaoSession;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * A model of the Food entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "FOOD",
        active = true,
        generateConstructors = false
)
public class FoodTable {
    @Id
    Long id;
    @Unique
    String uuid;
    @NotNull
    Long usdaNdbno;
    String name;
    @NotNull
    double protein;
    @NotNull
    double fat;
    @NotNull
    double carbs;
    @NotNull
    double weightUnit;
    @NotNull
    double energy;
    @NotNull
    double energyUnit;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 256830264)
    private transient FoodTableDao myDao;

    public FoodTable() {
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


    public Long getUsdaNdbno() {
        return this.usdaNdbno;
    }


    public void setUsdaNdbno(Long usdaNdbno) {
        this.usdaNdbno = usdaNdbno;
    }


    public String getName() {
        return this.name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public double getProtein() {
        return this.protein;
    }


    public void setProtein(double protein) {
        this.protein = protein;
    }


    public double getFat() {
        return this.fat;
    }


    public void setFat(double fat) {
        this.fat = fat;
    }


    public double getCarbs() {
        return this.carbs;
    }


    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }


    public double getWeightUnit() {
        return this.weightUnit;
    }


    public void setWeightUnit(double weightUnit) {
        this.weightUnit = weightUnit;
    }


    public double getEnergy() {
        return this.energy;
    }


    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public double getEnergyUnit() {
        return this.energyUnit;
    }

    public void setEnergyUnit(double energyUnit) {
        this.energyUnit = energyUnit;
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
    @Generated(hash = 975267863)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFoodTableDao() : null;
    }

}
