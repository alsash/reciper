package com.alsash.reciper.api.storage.local.database.table;

import com.alsash.reciper.api.storage.local.database.converter.GuidConverter;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Local relational database table that represents the Recipe entity
 */
@Entity(nameInDb = "RECIPE")
public class RecipeTable {

    @Id
    private Long id;

    @Unique
    @Convert(converter = GuidConverter.class, columnType = String.class)
    private UUID guid;

    private String name;

    private Date creationDate;

    private Date changeDate;

    private Long categoryId;

    @ToOne(joinProperty = "categoryId")
    private CategoryTable category;

    @ToMany
    @JoinEntity(
            entity = RecipeLabelTable.class,
            sourceProperty = "recipeId",
            targetProperty = "labelId"
    )
    private List<LabelTable> labels;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1210201788)
    private transient RecipeTableDao myDao;
    @Generated(hash = 1372501278)
    private transient Long category__resolvedKey;

    @Generated(hash = 1364035313)
    public RecipeTable(Long id, UUID guid, String name, Date creationDate,
                       Date changeDate, Long categoryId) {
        this.id = id;
        this.guid = guid;
        this.name = name;
        this.creationDate = creationDate;
        this.changeDate = changeDate;
        this.categoryId = categoryId;
    }

    @Generated(hash = 1656289545)
    public RecipeTable() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getGuid() {
        return this.guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getChangeDate() {
        return this.changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 803105998)
    public CategoryTable getCategory() {
        Long __key = this.categoryId;
        if (category__resolvedKey == null || !category__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CategoryTableDao targetDao = daoSession.getCategoryTableDao();
            CategoryTable categoryNew = targetDao.load(__key);
            synchronized (this) {
                category = categoryNew;
                category__resolvedKey = __key;
            }
        }
        return category;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1888877147)
    public void setCategory(CategoryTable category) {
        synchronized (this) {
            this.category = category;
            categoryId = category == null ? null : category.getId();
            category__resolvedKey = categoryId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 4833725)
    public List<LabelTable> getLabels() {
        if (labels == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelTableDao targetDao = daoSession.getLabelTableDao();
            List<LabelTable> labelsNew = targetDao._queryRecipeTable_Labels(id);
            synchronized (this) {
                if (labels == null) {
                    labels = labelsNew;
                }
            }
        }
        return labels;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 173120018)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecipeTableDao() : null;
    }
}
