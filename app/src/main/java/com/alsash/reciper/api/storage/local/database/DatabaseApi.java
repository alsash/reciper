package com.alsash.reciper.api.storage.local.database;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.alsash.reciper.R;
import com.alsash.reciper.api.storage.local.database.table.CategoryTable;
import com.alsash.reciper.api.storage.local.database.table.DaoMaster;
import com.alsash.reciper.api.storage.local.database.table.DaoSession;
import com.alsash.reciper.api.storage.local.database.table.LabelTable;
import com.alsash.reciper.api.storage.local.database.table.RecipeLabelTable;
import com.alsash.reciper.api.storage.local.database.table.RecipeTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.database.Database;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Singleton;

/**
 * Single instance for database api
 */
@Singleton
public class DatabaseApi {

    private static final String DATABASE_NAME = "reciper_db";
    private static Boolean firstCreated;
    private final Database database;
    private final DaoSession daoSession;
    private final Resources resources;

    public DatabaseApi(Context appContext) {
        database = new DbOpenHelper(appContext, DATABASE_NAME).getWritableDb();
        daoSession = new DaoMaster(database).newSession();
        resources = appContext.getResources();
    }

    public synchronized DaoSession getSession() {
        return daoSession;
    }

    public synchronized void createStartupEntriesIfNeed() {
        // Trying to set firstCreated in DbOpenHelper.onCreate(db)
        if (firstCreated == null) getSession().getRecipeTableDao().load(0L);
        // Check if DbOpenHelper.onCreate(db) has been called
        if (firstCreated != null && firstCreated) createStartupEntities();
        // Set firstCreated marker for future calls
        firstCreated = false;
    }

    /**
     * Persist entities to database.
     * Entities will insert with nested transactions.
     * Consider call to this method in background.
     */
    void createStartupEntities() {
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
        final DaoSession session = getSession();
        session.runInTx(new Runnable() {
            @Override
            public void run() {
                session.getRecipeTableDao().insertOrReplaceInTx(recipes, false);
                session.getCategoryTableDao().insertOrReplaceInTx(categories, false);
                session.getLabelTableDao().insertOrReplaceInTx(labels, false);
                session.getRecipeLabelTableDao().insertOrReplaceInTx(recipeLabelTables, false);
            }
        });
        session.clear();
    }

    private static class DbOpenHelper extends DaoMaster.OpenHelper {

        public DbOpenHelper(Context context, String name) {
            super(context, name);
        }

        @Override
        public void onCreate(Database db) {
            super.onCreate(db);
            DatabaseApi.firstCreated = true;
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("DatabaseApi", "Upgrading schema from version " + oldVersion +
                    " to " + newVersion + " by dropping all tables");
            DaoMaster.dropAllTables(db, true);
            onCreate(db);
        }
    }
}
