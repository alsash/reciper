package com.alsash.reciper.data.db.table;

import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Photo;

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
 * A model of the Category entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "CATEGORY",
        generateGettersSetters = false,
        active = true
)
public final class CategoryTable implements Table, Category {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index
    Date changedAt;
    String name;
    @Index(name = "PHOTO_TO_CATEGORY", unique = true)
    String photoUuid;
    @ToMany(joinProperties = {@JoinProperty(name = "photoUuid", referencedName = "uuid")})
    List<PhotoTable> photoTables;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 940999369)
    private transient CategoryTableDao myDao;

    @Generated(hash = 106282483)
    public CategoryTable(Long id, String uuid, Date changedAt, String name,
                         String photoUuid) {
        this.id = id;
        this.uuid = uuid;
        this.changedAt = changedAt;
        this.name = name;
        this.photoUuid = photoUuid;
    }

    @Generated(hash = 1679078959)
    public CategoryTable() {
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

    public String getPhotoUuid() {
        return photoUuid;
    }

    public void setPhotoUuid(String photoUuid) {
        this.photoUuid = photoUuid;
    }

    @Override
    public Photo getPhoto() {
        return getPhotoTables().size() > 0 ? getPhotoTables().get(0) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryTable)) return false;
        CategoryTable that = (CategoryTable) o;
        return getUuid() != null ? getUuid().equals(that.getUuid()) : that.getUuid() == null;
    }

    @Override
    public int hashCode() {
        return getUuid() != null ? getUuid().hashCode() : 0;
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
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 228559382)
    public List<PhotoTable> getPhotoTables() {
        if (photoTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PhotoTableDao targetDao = daoSession.getPhotoTableDao();
            List<PhotoTable> photoTablesNew = targetDao
                    ._queryCategoryTable_PhotoTables(photoUuid);
            synchronized (this) {
                if (photoTables == null) {
                    photoTables = photoTablesNew;
                }
            }
        }
        return photoTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1475926327)
    public synchronized void resetPhotoTables() {
        photoTables = null;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1828535427)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCategoryTableDao() : null;
    }
}