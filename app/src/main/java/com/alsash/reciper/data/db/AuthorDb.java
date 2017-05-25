package com.alsash.reciper.data.db;

import com.alsash.reciper.api.storage.local.database.table.DaoSession;
import com.alsash.reciper.mvp.model.Author;
import com.alsash.reciper.mvp.model.Photo;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;

/**
 * A model of the Author entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "AUTHOR",
        active = true,
        generateConstructors = false
)
public class AuthorDb implements Author {
    @Id
    Long id;

    @Unique
    @NotNull
    String uuid;
    @NotNull
    String name;
    @NotNull
    String mail;

    @Index(name = "photo", unique = true)
    @NotNull
    String photoUuid;

    @ToMany(joinProperties = {@JoinProperty(name = "photoUuid", referencedName = "uuid")})
    List<PhotoDb> photoDbs;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 202635700)
    private transient AuthorDbDao myDao;

    public AuthorDb() {
    }

    @Override
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

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getMail() {
        return this.mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhotoUuid() {
        return this.photoUuid;
    }

    public void setPhotoUuid(String photoUuid) {
        this.photoUuid = photoUuid;
    }

    @Override
    public Photo getPhoto() {
        return (getPhotoDbs().size() > 0) ? getPhotoDbs().get(0) : null;
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
    @Generated(hash = 1262264093)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAuthorDbDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1279840762)
    public List<PhotoDb> getPhotoDbs() {
        if (photoDbs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PhotoDbDao targetDao = daoSession.getPhotoDbDao();
            List<PhotoDb> photoDbsNew = targetDao._queryAuthorDb_PhotoDbs(photoUuid);
            synchronized (this) {
                if (photoDbs == null) {
                    photoDbs = photoDbsNew;
                }
            }
        }
        return photoDbs;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 863329855)
    public synchronized void resetPhotoDbs() {
        photoDbs = null;
    }
}
