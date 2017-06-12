package com.alsash.reciper.data.db.table;

import com.alsash.reciper.mvp.model.entity.Author;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Ingredient;
import com.alsash.reciper.mvp.model.entity.Label;
import com.alsash.reciper.mvp.model.entity.Method;
import com.alsash.reciper.mvp.model.entity.Photo;
import com.alsash.reciper.mvp.model.entity.RecipeFull;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A model of the Recipe entity
 * that persists in local relational database table by GreenDao framework
 * and serialized from JSON by Gson framework
 */
@Entity(
        nameInDb = "RECIPE",
        generateGettersSetters = false,
        active = true
)
public final class RecipeTable implements Table, RecipeFull {
    @Id
    Long id;
    @Unique
    String uuid;
    @Index
    Date changedAt;
    @SerializedName("created_at_unix")
    Date createdAt;
    String name;
    String source;
    String description;
    @NotNull
    int servings;
    @NotNull
    double massFlowRateGps; // gram per second
    @Index(name = "CATEGORY_TO_RECIPE")
    String categoryUuid;
    @Index(name = "AUTHOR_TO_RECIPE")
    String authorUuid;
    @ToMany(joinProperties = {@JoinProperty(name = "categoryUuid", referencedName = "uuid")})
    List<CategoryTable> categoryTables;
    @ToMany(joinProperties = {@JoinProperty(name = "authorUuid", referencedName = "uuid")})
    List<AuthorTable> authorTables;
    @ToMany(joinProperties = {@JoinProperty(name = "uuid", referencedName = "recipeUuid")})
    List<RecipePhotoTable> recipePhotoTables;
    @ToMany(joinProperties = {@JoinProperty(name = "uuid", referencedName = "recipeUuid")})
    List<RecipeLabelTable> recipeLabelTables;
    @ToMany(joinProperties = {@JoinProperty(name = "uuid", referencedName = "recipeUuid")})
    List<RecipeFoodTable> recipeFoodTables;
    @ToMany(joinProperties = {@JoinProperty(name = "uuid", referencedName = "recipeUuid")})
    List<RecipeMethodTable> recipeMethodTables;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1210201788)
    private transient RecipeTableDao myDao;

    @Generated(hash = 1742398024)
    public RecipeTable(Long id, String uuid, Date changedAt, Date createdAt, String name,
                       String source, String description, int servings, double massFlowRateGps,
                       String categoryUuid, String authorUuid) {
        this.id = id;
        this.uuid = uuid;
        this.changedAt = changedAt;
        this.createdAt = createdAt;
        this.name = name;
        this.source = source;
        this.description = description;
        this.servings = servings;
        this.massFlowRateGps = massFlowRateGps;
        this.categoryUuid = categoryUuid;
        this.authorUuid = authorUuid;
    }

    @Generated(hash = 1656289545)
    public RecipeTable() {
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
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    @Override
    public double getMassFlowRateGps() {
        return massFlowRateGps;
    }

    public void setMassFlowRateGps(double massFlowRateGps) {
        this.massFlowRateGps = massFlowRateGps;
    }

    public String getCategoryUuid() {
        return categoryUuid;
    }

    public void setCategoryUuid(String categoryUuid) {
        this.categoryUuid = categoryUuid;
    }

    public String getAuthorUuid() {
        return authorUuid;
    }

    public void setAuthorUuid(String authorUuid) {
        this.authorUuid = authorUuid;
    }

    @Override
    public Photo getMainPhoto() {
        for (RecipePhotoTable recipePhotoTable : getRecipePhotoTables()) {
            if (recipePhotoTable.main)
                return getRecipePhotoTables().size() > 0 ?
                        recipePhotoTable.getPhotoTables().get(0) :
                        null;
        }
        return null;
    }

    @Override
    public Author getAuthor() {
        return getAuthorTables().size() > 0 ? getAuthorTables().get(0) : null;
    }

    @Override
    public Category getCategory() {
        return getCategoryTables().size() > 0 ? getCategoryTables().get(0) : null;
    }

    @Override
    public List<Photo> getPhotos() {
        List<Photo> photos = new ArrayList<>();
        for (RecipePhotoTable recipePhotoTable : getRecipePhotoTables()) {
            photos.addAll(recipePhotoTable.getPhotoTables());
        }
        return photos;
    }

    @Override
    public List<Label> getLabels() {
        List<Label> labels = new ArrayList<>();
        for (RecipeLabelTable recipeLabelTable : getRecipeLabelTables()) {
            labels.addAll(recipeLabelTable.getLabelTables());
        }
        return labels;
    }

    @Override
    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.addAll(getRecipeFoodTables());
        return ingredients;
    }

    @Override
    public List<Method> getMethods() {
        List<Method> methods = new ArrayList<>();
        methods.addAll(getRecipeMethodTables());
        return methods;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1211508832)
    public List<CategoryTable> getCategoryTables() {
        if (categoryTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CategoryTableDao targetDao = daoSession.getCategoryTableDao();
            List<CategoryTable> categoryTablesNew = targetDao
                    ._queryRecipeTable_CategoryTables(categoryUuid);
            synchronized (this) {
                if (categoryTables == null) {
                    categoryTables = categoryTablesNew;
                }
            }
        }
        return categoryTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1017928287)
    public synchronized void resetCategoryTables() {
        categoryTables = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1157942686)
    public List<AuthorTable> getAuthorTables() {
        if (authorTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AuthorTableDao targetDao = daoSession.getAuthorTableDao();
            List<AuthorTable> authorTablesNew = targetDao
                    ._queryRecipeTable_AuthorTables(authorUuid);
            synchronized (this) {
                if (authorTables == null) {
                    authorTables = authorTablesNew;
                }
            }
        }
        return authorTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 376306627)
    public synchronized void resetAuthorTables() {
        authorTables = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 601605547)
    public List<RecipePhotoTable> getRecipePhotoTables() {
        if (recipePhotoTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipePhotoTableDao targetDao = daoSession.getRecipePhotoTableDao();
            List<RecipePhotoTable> recipePhotoTablesNew = targetDao
                    ._queryRecipeTable_RecipePhotoTables(uuid);
            synchronized (this) {
                if (recipePhotoTables == null) {
                    recipePhotoTables = recipePhotoTablesNew;
                }
            }
        }
        return recipePhotoTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 71148360)
    public synchronized void resetRecipePhotoTables() {
        recipePhotoTables = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1985941725)
    public List<RecipeLabelTable> getRecipeLabelTables() {
        if (recipeLabelTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeLabelTableDao targetDao = daoSession.getRecipeLabelTableDao();
            List<RecipeLabelTable> recipeLabelTablesNew = targetDao
                    ._queryRecipeTable_RecipeLabelTables(uuid);
            synchronized (this) {
                if (recipeLabelTables == null) {
                    recipeLabelTables = recipeLabelTablesNew;
                }
            }
        }
        return recipeLabelTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 621796270)
    public synchronized void resetRecipeLabelTables() {
        recipeLabelTables = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 425446250)
    public List<RecipeFoodTable> getRecipeFoodTables() {
        if (recipeFoodTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeFoodTableDao targetDao = daoSession.getRecipeFoodTableDao();
            List<RecipeFoodTable> recipeFoodTablesNew = targetDao
                    ._queryRecipeTable_RecipeFoodTables(uuid);
            synchronized (this) {
                if (recipeFoodTables == null) {
                    recipeFoodTables = recipeFoodTablesNew;
                }
            }
        }
        return recipeFoodTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1460648270)
    public synchronized void resetRecipeFoodTables() {
        recipeFoodTables = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 822006377)
    public List<RecipeMethodTable> getRecipeMethodTables() {
        if (recipeMethodTables == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RecipeMethodTableDao targetDao = daoSession.getRecipeMethodTableDao();
            List<RecipeMethodTable> recipeMethodTablesNew = targetDao
                    ._queryRecipeTable_RecipeMethodTables(uuid);
            synchronized (this) {
                if (recipeMethodTables == null) {
                    recipeMethodTables = recipeMethodTablesNew;
                }
            }
        }
        return recipeMethodTables;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 864534768)
    public synchronized void resetRecipeMethodTables() {
        recipeMethodTables = null;
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
    @Generated(hash = 173120018)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecipeTableDao() : null;
    }

}