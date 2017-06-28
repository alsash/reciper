package com.alsash.reciper.data.db.table;

import android.support.annotation.Nullable;

import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Measure;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;

/**
 * A model of the Food entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "FOOD",
        generateGettersSetters = false,
        active = true
)
public final class FoodTable implements Table, Food {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index
    Date changedAt;
    String name;
    @NotNull
    double protein;
    @NotNull
    double fat;
    @NotNull
    double carbs;
    String weightUnit;
    @NotNull
    double energy;
    String energyUnit;
    @ToMany(joinProperties = {@JoinProperty(name = "uuid", referencedName = "foodUuid")})
    List<FoodMeasureTable> foodMeasureTables;
    @ToMany(joinProperties = {@JoinProperty(name = "uuid", referencedName = "foodUuid")})
    List<FoodUsdaTable> foodUsdaTables;
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

    @Generated(hash = 602744997)
    public FoodTable(Long id, String uuid, Date changedAt, String name, double protein,
                     double fat, double carbs, String weightUnit, double energy, String energyUnit) {
        this.id = id;
        this.uuid = uuid;
        this.changedAt = changedAt;
        this.name = name;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.weightUnit = weightUnit;
        this.energy = energy;
        this.energyUnit = energyUnit;
    }

    @Generated(hash = 466144808)
    public FoodTable() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public Date getChangedAt() {
        return changedAt;
    }

    @Override
    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    @Override
    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    @Override
    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    @Override
    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    @Override
    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    @Override
    public String getEnergyUnit() {
        return energyUnit;
    }

    public void setEnergyUnit(String energyUnit) {
        this.energyUnit = energyUnit;
    }

    @Nullable
    @Override
    public Measure getMeasure() {
        return getFoodMeasureTables().size() > 0 ? getFoodMeasureTables().get(0) : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1102525603)
    public List<FoodMeasureTable> getFoodMeasureTables() {
        if (foodMeasureTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FoodMeasureTableDao targetDao = daoSession.getFoodMeasureTableDao();
            List<FoodMeasureTable> foodMeasureTablesNew = targetDao
                    ._queryFoodTable_FoodMeasureTables(uuid);
            synchronized (this) {
                if (foodMeasureTables == null) {
                    foodMeasureTables = foodMeasureTablesNew;
                }
            }
        }
        return foodMeasureTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 807362054)
    public synchronized void resetFoodMeasureTables() {
        foodMeasureTables = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 889381843)
    public List<FoodUsdaTable> getFoodUsdaTables() {
        if (foodUsdaTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FoodUsdaTableDao targetDao = daoSession.getFoodUsdaTableDao();
            List<FoodUsdaTable> foodUsdaTablesNew = targetDao
                    ._queryFoodTable_FoodUsdaTables(uuid);
            synchronized (this) {
                if (foodUsdaTables == null) {
                    foodUsdaTables = foodUsdaTablesNew;
                }
            }
        }
        return foodUsdaTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 920509767)
    public synchronized void resetFoodUsdaTables() {
        foodUsdaTables = null;
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