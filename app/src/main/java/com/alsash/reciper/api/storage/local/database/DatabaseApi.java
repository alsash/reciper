package com.alsash.reciper.api.storage.local.database;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.R;
import com.alsash.reciper.api.storage.local.database.table.CategoryTable;
import com.alsash.reciper.api.storage.local.database.table.CategoryTableDao;
import com.alsash.reciper.api.storage.local.database.table.DaoMaster;
import com.alsash.reciper.api.storage.local.database.table.DaoSession;
import com.alsash.reciper.api.storage.local.database.table.LabelTable;
import com.alsash.reciper.api.storage.local.database.table.LabelTableDao;
import com.alsash.reciper.api.storage.local.database.table.RecipeLabelTable;
import com.alsash.reciper.api.storage.local.database.table.RecipeLabelTableDao;
import com.alsash.reciper.api.storage.local.database.table.RecipeTable;
import com.alsash.reciper.api.storage.local.database.table.RecipeTableDao;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Singleton;

/**
 * Single instance for database api
 */
@Singleton
public class DatabaseApi {

    private static final String DATABASE_NAME = "reciper_db";
    static Boolean firstCreated; // Set from Helper
    private final DaoSession daoSession;
    private final Resources resources;

    public DatabaseApi(Context context) {
        Database database = new DatabaseOpenHelper(context, DATABASE_NAME).getWritableDb();
        daoSession = new DaoMaster(database).newSession();
        resources = context.getResources();
    }

    public void clearCache() {
        daoSession.clear();
    }

    /**
     * Load Categories with limit and offset on background thread
     *
     * @param offset offset from current position
     * @param limit  limit for loading per 1 time
     * @return list of categories
     */
    @WorkerThread
    public List<CategoryTable> getCategories(int offset, int limit) {
        return daoSession
                .getCategoryTableDao()
                .queryBuilder()
                .orderDesc(CategoryTableDao.Properties.ChangeDate)
                .orderDesc(CategoryTableDao.Properties.CreationDate)
                .limit(limit)
                .offset(offset)
                .build()
                .list();
    }

    public List<LabelTable> getLabels(int offset, int limit) {
        return daoSession
                .getLabelTableDao()
                .queryBuilder()
                .orderDesc(LabelTableDao.Properties.ChangeDate)
                .orderDesc(LabelTableDao.Properties.CreationDate)
                .limit(limit)
                .offset(offset)
                .build()
                .list();
    }

    @WorkerThread
    public List<RecipeTable> getRecipes(int offset, int limit) {
        return daoSession
                .getRecipeTableDao()
                .queryBuilder()
                .orderDesc(RecipeTableDao.Properties.ChangeDate)
                .orderDesc(RecipeTableDao.Properties.CreationDate)
                .limit(limit)
                .offset(offset)
                .build()
                .list();
    }

    public List<RecipeTable> getLabeledRecipes(int offset, int limit, long labelId) {
        QueryBuilder<RecipeTable> queryBuilder = daoSession
                .getRecipeTableDao()
                .queryBuilder()
                .orderDesc(RecipeTableDao.Properties.ChangeDate)
                .orderDesc(RecipeTableDao.Properties.CreationDate)
                .limit(limit)
                .offset(offset);
        queryBuilder
                .join(RecipeLabelTable.class, RecipeLabelTableDao.Properties.RecipeId)
                .where(RecipeLabelTableDao.Properties.LabelId.eq(labelId));
        return queryBuilder
                .build()
                .list();
    }

    public List<RecipeTable> getCategorizedRecipes(int offset, int limit, long categoryId) {
        return daoSession
                .getRecipeTableDao()
                .queryBuilder()
                .orderDesc(RecipeTableDao.Properties.ChangeDate)
                .orderDesc(RecipeTableDao.Properties.CreationDate)
                .where(RecipeTableDao.Properties.CategoryId.eq(categoryId))
                .limit(limit)
                .offset(offset)
                .build()
                .list();
    }

    public List<RecipeTable> getBookmarkedRecipes(int offset, int limit) {
        return daoSession
                .getRecipeTableDao()
                .queryBuilder()
                .orderDesc(RecipeTableDao.Properties.ChangeDate)
                .orderDesc(RecipeTableDao.Properties.CreationDate)
                //.where(RecipeTableDao.Properties.CategoryId.eq(categoryId))
                .limit(limit)
                .offset(offset)
                .build()
                .list();
    }

    public synchronized void createStartupEntriesIfNeed() {
        // Trying to set firstCreated in DatabaseOpenHelper.onCreate(db)
        if (firstCreated == null) daoSession.getRecipeTableDao().load(0L);
        // Check if DatabaseOpenHelper.onCreate(db) has been called
        if (firstCreated != null && firstCreated) createStartupEntities();
        // Set firstCreated marker for future calls
        firstCreated = false;
    }

    /**
     * Persist entities to the database.
     * Entities will insert with nested transactions.
     * Call to this method must be in the background.
     */
    @WorkerThread
    private void createStartupEntities() {
        // Fetch Json arrays
        String recipesJson = resources.getString(R.string.startup_entity_recipe);
        String categoriesJson = resources.getString(R.string.startup_entity_category);
        String labelsJson = resources.getString(R.string.startup_entity_label);
        String recipeLabelsJson = resources.getString(R.string.startup_entity_recipe_label_join);

        // Prepare serializer
        Gson gson = new Gson();
        Type recipesType = new TypeToken<List<RecipeTable>>() {
        }.getType();
        Type categoriesType = new TypeToken<List<CategoryTable>>() {
        }.getType();
        Type labelsType = new TypeToken<List<LabelTable>>() {
        }.getType();
        Type recipeLabelJoinType = new TypeToken<List<RecipeLabelTable>>() {
        }.getType();

        // Deserialization Json arrays to database tables
        final List<RecipeTable> recipes = gson.fromJson(recipesJson, recipesType);
        final List<CategoryTable> categories = gson.fromJson(categoriesJson, categoriesType);
        final List<LabelTable> labels = gson.fromJson(labelsJson, labelsType);
        final List<RecipeLabelTable> recipeLabelTables = gson.fromJson(recipeLabelsJson,
                recipeLabelJoinType);

        // Insert entities with nested transactions (outer transaction will provide final commit)
        daoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                daoSession.getRecipeTableDao().insertOrReplaceInTx(recipes, false);
                daoSession.getCategoryTableDao().insertOrReplaceInTx(categories, false);
                daoSession.getLabelTableDao().insertOrReplaceInTx(labels, false);
                daoSession.getRecipeLabelTableDao().insertOrReplaceInTx(recipeLabelTables, false);
            }
        });
        daoSession.clear();
    }
}
