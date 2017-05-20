package com.alsash.reciper.api.storage.local.database.table;

import com.alsash.reciper.api.storage.local.database.converter.GuidConverter;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;
import java.util.UUID;

/**
 * Local relational database table that represents the Category entity
 */
@Entity(nameInDb = "CATEGORY")
public class CategoryTable {
    @Id
    private Long id;
    @Unique
    @Convert(converter = GuidConverter.class, columnType = String.class)
    private UUID uuid;
    private String name;
    private Date creationDate;
    private Date changeDate;
    private Long photoId;
    @ToOne(joinProperty = "photoId")
    private PhotoTable photo;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 940999369)
    private transient CategoryTableDao myDao;
    @Generated(hash = 1137958716)
    private transient Long photo__resolvedKey;

    @Generated(hash = 176096263)
    public CategoryTable(Long id, UUID uuid, String name, Date creationDate,
                         Date changeDate, Long photoId) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.creationDate = creationDate;
        this.changeDate = changeDate;
        this.photoId = photoId;
    }
    @Generated(hash = 1679078959)
    public CategoryTable() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public Long getPhotoId() {
        return this.photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 2099343229)
    public PhotoTable getPhoto() {
        Long __key = this.photoId;
        if (photo__resolvedKey == null || !photo__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PhotoTableDao targetDao = daoSession.getPhotoTableDao();
            PhotoTable photoNew = targetDao.load(__key);
            synchronized (this) {
                photo = photoNew;
                photo__resolvedKey = __key;
            }
        }
        return photo;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1794974770)
    public void setPhoto(PhotoTable photo) {
        synchronized (this) {
            this.photo = photo;
            photoId = photo == null ? null : photo.getId();
            photo__resolvedKey = photoId;
        }
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
    @Generated(hash = 1828535427)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCategoryTableDao() : null;
    }
}
