package com.alsash.reciper.database;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.alsash.reciper.R;
import com.alsash.reciper.database.entity.Category;
import com.alsash.reciper.database.entity.DaoMaster;
import com.alsash.reciper.database.entity.DaoSession;
import com.alsash.reciper.database.entity.Label;
import com.alsash.reciper.database.entity.Recipe;
import com.alsash.reciper.database.entity.RecipeLabelJoin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.database.Database;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Singleton;

/**
 * Single instance for database api
 */
@Singleton
public class ApiDatabase {

    private static final String DATABASE_NAME = "reciper_db";
    private static Boolean firstCreated;
    private final WeakReference<DaoSession> refDaoSession;
    private final WeakReference<Resources> resourcesRef;

    public ApiDatabase(Context appContext) {
        Database db = new DbOpenHelper(appContext, DATABASE_NAME).getWritableDb();
        refDaoSession = new WeakReference<>(new DaoMaster(db).newSession());
        resourcesRef = new WeakReference<Resources>(appContext.getResources());
    }

    public synchronized DaoSession getSession() {
        return refDaoSession.get();
    }

    public synchronized void clearSession() {
        if (refDaoSession.get() == null) return;
        refDaoSession.get().clear();
    }

    public synchronized void createStartupEntriesIfNeed(Resources res) {
        // Trying to set firstCreated in DbOpenHelper.onCreate(db)
        if (firstCreated == null) getSession().getRecipeDao().load(0L);
        // Check if DbOpenHelper.onCreate(db) has been called
        if (firstCreated != null && firstCreated) createStartupEntities(res);
        // Set firstCreated marker for future calls
        firstCreated = false;
    }

    /**
     * Persist entities to database.
     * Entities will insert with nested transactions.
     * Consider call to this method in background.
     */
    void createStartupEntities(Resources res) {
        // Fetch Json arrays
        String recipesJson = res.getString(R.string.startup_entity_recipe);
        String categoriesJson = res.getString(R.string.startup_entity_category);
        String labelsJson = res.getString(R.string.startup_entity_label);
        String recipeLabelsJson = res.getString(R.string.startup_entity_recipe_label_join);

        // Prepare serializer
        Gson gson = new Gson();
        Type recipesType = new TypeToken<List<Recipe>>() {
        }.getType();
        Type categoriesType = new TypeToken<List<Category>>() {
        }.getType();
        Type labelsType = new TypeToken<List<Label>>() {
        }.getType();
        Type recipeLabelJoinType = new TypeToken<List<RecipeLabelJoin>>() {
        }.getType();

        // Deserialization Json arrays to entities
        final List<Recipe> recipes = gson.fromJson(recipesJson, recipesType);
        final List<Category> categories = gson.fromJson(categoriesJson, categoriesType);
        final List<Label> labels = gson.fromJson(labelsJson, labelsType);
        final List<RecipeLabelJoin> recipeLabelJoins = gson.fromJson(recipeLabelsJson,
                recipeLabelJoinType);

        // Insert entities with nested transactions (outer transaction will provide final commit)
        final DaoSession session = getSession();
        session.runInTx(new Runnable() {
            @Override
            public void run() {
                session.getRecipeDao().insertOrReplaceInTx(recipes, false);
                session.getCategoryDao().insertOrReplaceInTx(categories, false);
                session.getLabelDao().insertOrReplaceInTx(labels, false);
                session.getRecipeLabelJoinDao().insertOrReplaceInTx(recipeLabelJoins, false);
            }
        });

        clearSession();
    }

    private static class DbOpenHelper extends DaoMaster.OpenHelper {

        public DbOpenHelper(Context context, String name) {
            super(context, name);
        }

        @Override
        public void onCreate(Database db) {
            super.onCreate(db);
            ApiDatabase.firstCreated = true;
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("ApiDatabase", "Upgrading schema from version " + oldVersion +
                    " to " + newVersion + " by dropping all tables");
            DaoMaster.dropAllTables(db, true);
            onCreate(db);
        }
    }
}
