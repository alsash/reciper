package com.alsash.reciper.database;

import android.content.Context;
import android.content.res.Resources;

import com.alsash.reciper.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.database.Database;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Single instance for database api
 */
public class ApiDb {

    public static final int VERSION = DaoMaster.SCHEMA_VERSION;

    private static final String DATABASE_NAME = "reciper_db";
    private static final ApiDb INSTANCE = new ApiDb();
    private WeakReference<DaoSession> refDaoSession;
    private boolean created;

    private ApiDb() {
    }

    public static ApiDb getInstance() {
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

    /**
     * Check if database was created
     *
     * @return true only once
     */
    public synchronized boolean isCreated() {
        boolean created = this.created;
        if (created) this.created = false;
        return created;
    }

    /**
     * Create entries if needed
     */
    public void createStartupEntriesIfNeed(Context context) {
        getSession(context).getRecipeLabelJoinDao().load(1L); // will be run chain from DbOpenHelper
    }

    protected void createStartupEntities(final Context context) {
        this.created = true;
        // Fetch Json arrays
        Resources res = context.getResources();
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
        Type recipeLabelsType = new TypeToken<List<RecipeLabelJoin>>() {
        }.getType();

        // Deserialization Json arrays to entities
        final List<Recipe> recipes = gson.fromJson(recipesJson, recipesType);
        final List<Category> categories = gson.fromJson(categoriesJson, categoriesType);
        final List<Label> labels = gson.fromJson(labelsJson, labelsType);
        final List<RecipeLabelJoin> recipeLabels = gson.fromJson(recipeLabelsJson, recipeLabelsType);

        // Persist entities to database in background
        Runnable backgroundInsert = new Runnable() {
            @Override
            public void run() {
                // Moves the current Thread into the background
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                // Do in background
                DaoSession session = getSession(context);
                session.getRecipeDao().insertInTx(recipes);
                session.getCategoryDao().insertInTx(categories);
                session.getLabelDao().insertInTx(labels);
                session.getRecipeLabelJoinDao().insertInTx(recipeLabels);
                clearSession(); // Reset all caches
            }
        };
        backgroundInsert.run();
    }

    private static class DbOpenHelper extends DaoMaster.DevOpenHelper {

        private final Context context;

        public DbOpenHelper(Context context, String name) {
            super(context, name);
            this.context = context;
        }

        @Override
        public void onCreate(Database db) {
            super.onCreate(db);
            ApiDb.getInstance().createStartupEntities(context);
        }
    }
}
