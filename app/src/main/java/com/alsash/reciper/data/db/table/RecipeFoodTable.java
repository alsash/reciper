package com.alsash.reciper.data.db.table;

import com.alsash.reciper.mvp.model.entity.Food;
import com.alsash.reciper.mvp.model.entity.Ingredient;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;

/**
 * A model of the Ingredient entity
 * represented by a to-many relation model of the Recipe and the Food entities,
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "RECIPE_FOOD",
        generateGettersSetters = false,
        active = true
)
public final class RecipeFoodTable implements Table, Ingredient {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index
    Date changedAt;
    String name;
    double weight;
    String weightUnit;
    @Index(name = "RECIPE_TO_FOOD")
    String recipeUuid;
    @Index(name = "FOOD_TO_RECIPE")
    String foodUuid;
    @ToMany(joinProperties = {@JoinProperty(name = "foodUuid", referencedName = "uuid")})
    List<FoodTable> foodTables;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 805032546)
    private transient RecipeFoodTableDao myDao;

    @Generated(hash = 1549919066)
    public RecipeFoodTable(Long id, String uuid, Date changedAt, String name, double weight,
                           String weightUnit, String recipeUuid, String foodUuid) {
        this.id = id;
        this.uuid = uuid;
        this.changedAt = changedAt;
        this.name = name;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.recipeUuid = recipeUuid;
        this.foodUuid = foodUuid;
    }

    @Generated(hash = 74732698)
    public RecipeFoodTable() {
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
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getRecipeUuid() {
        return recipeUuid;
    }

    public void setRecipeUuid(String recipeUuid) {
        this.recipeUuid = recipeUuid;
    }

    public String getFoodUuid() {
        return foodUuid;
    }

    public void setFoodUuid(String foodUuid) {
        this.foodUuid = foodUuid;
    }

    @Override
    public Food getFood() {
        return getFoodTables().size() > 0 ? getFoodTables().get(0) : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 217659463)
    public List<FoodTable> getFoodTables() {
        if (foodTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FoodTableDao targetDao = daoSession.getFoodTableDao();
            List<FoodTable> foodTablesNew = targetDao
                    ._queryRecipeFoodTable_FoodTables(foodUuid);
            synchronized (this) {
                if (foodTables == null) {
                    foodTables = foodTablesNew;
                }
            }
        }
        return foodTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1011280790)
    public synchronized void resetFoodTables() {
        foodTables = null;
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
    @Generated(hash = 2007437163)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecipeFoodTableDao() : null;
    }
}