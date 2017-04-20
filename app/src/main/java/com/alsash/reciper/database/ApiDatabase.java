package com.alsash.reciper.database;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
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

import org.greenrobot.greendao.async.AsyncOperation;
import org.greenrobot.greendao.async.AsyncOperationListener;
import org.greenrobot.greendao.async.AsyncSession;
import org.greenrobot.greendao.database.Database;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Single instance for database api
 */
public class ApiDatabase {

    public static final int VERSION = DaoMaster.SCHEMA_VERSION;

    private static final String DATABASE_NAME = "reciper_db";
    private static final ApiDatabase INSTANCE = new ApiDatabase();
    private WeakReference<DaoSession> refDaoSession;
    private boolean firstCreated;

    private ApiDatabase() {
    }

    public static ApiDatabase getInstance() {
        return INSTANCE;
    }

    public synchronized DaoSession getSession(Context context) {
        if (refDaoSession == null || refDaoSession.get() == null) {
            Context appContext = context.getApplicationContext();
            Database db = new DbOpenHelper(appContext, DATABASE_NAME).getWritableDb();
            refDaoSession = new WeakReference<>(new DaoMaster(db).newSession());
        }
        return refDaoSession.get();
    }

    public synchronized void clearSession() {
        if (refDaoSession == null || refDaoSession.get() == null) return;
        refDaoSession.get().clear();
        refDaoSession = null;
    }

    boolean popFirstCreated() {
        boolean firstCreated = this.firstCreated;
        if (firstCreated) this.firstCreated = false;
        return firstCreated;
    }

    /**
     * Create entries if needed
     */
    public void createStartupEntriesIfNeed(final Context context, final Runnable callback) {
        if (popFirstCreated()) {
            createStartupEntities(context, callback);
        } else {
            callback.run();
        }
    }

    /**
     * Persist entities to database in background.
     * Entities will insert with nested transactions.
     * See {@link SQLiteDatabase#beginTransaction()} for details.
     */
    void createStartupEntities(final Context context, final Runnable callback) {
        final DaoSession session = getSession(context);
        final Resources res = context.getResources();
        Runnable backgroundInsert = new Runnable() {
            @Override
            public void run() {
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

                // Insert entities
                session.getRecipeDao().insertInTx(recipes);
                session.getCategoryDao().insertInTx(categories);
                session.getLabelDao().insertInTx(labels);
                session.getRecipeLabelJoinDao().insertInTx(recipeLabelJoins);
            }
        };
        // Run in background
        AsyncSession asyncSession = session.startAsyncSession();
        asyncSession.setListenerMainThread(new AsyncOperationListener() {
            @Override
            public void onAsyncOperationCompleted(AsyncOperation operation) {
                clearSession();
                if (callback != null) callback.run();
            }
        });
        asyncSession.runInTx(backgroundInsert);
    }

    private static class DbOpenHelper extends DaoMaster.OpenHelper {

        public DbOpenHelper(Context context, String name) {
            super(context, name);
        }

        @Override
        public void onCreate(Database db) {
            super.onCreate(db);
            ApiDatabase.getInstance().firstCreated = true;
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
