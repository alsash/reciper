package com.alsash.reciper.data.db.table;

import com.alsash.reciper.mvp.model.entity.BaseEntity;

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
 * A to-many relation model of the Recipe and the Label entities,
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "RECIPE_LABEL",
        active = true
)
public final class RecipeLabelTable implements Table, BaseEntity {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index
    Date changedAt;
    @Index(name = "RECIPE_TO_LABEL")
    String recipeUuid;
    @Index(name = "LABEL_TO_RECIPE")
    String labelUuid;
    @ToMany(joinProperties = {@JoinProperty(name = "labelUuid", referencedName = "uuid")})
    List<LabelTable> labelTables;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 377029229)
    private transient RecipeLabelTableDao myDao;

    @Generated(hash = 1666755772)
    public RecipeLabelTable(Long id, String uuid, Date changedAt, String recipeUuid,
                            String labelUuid) {
        this.id = id;
        this.uuid = uuid;
        this.changedAt = changedAt;
        this.recipeUuid = recipeUuid;
        this.labelUuid = labelUuid;
    }

    @Generated(hash = 2053410462)
    public RecipeLabelTable() {
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

    public String getRecipeUuid() {
        return recipeUuid;
    }

    public void setRecipeUuid(String recipeUuid) {
        this.recipeUuid = recipeUuid;
    }

    public String getLabelUuid() {
        return labelUuid;
    }

    public void setLabelUuid(String labelUuid) {
        this.labelUuid = labelUuid;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1625591711)
    public List<LabelTable> getLabelTables() {
        if (labelTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelTableDao targetDao = daoSession.getLabelTableDao();
            List<LabelTable> labelTablesNew = targetDao
                    ._queryRecipeLabelTable_LabelTables(labelUuid);
            synchronized (this) {
                if (labelTables == null) {
                    labelTables = labelTablesNew;
                }
            }
        }
        return labelTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1691759982)
    public synchronized void resetLabelTables() {
        labelTables = null;
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
    @Generated(hash = 1330679234)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecipeLabelTableDao() : null;
    }
}