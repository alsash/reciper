package com.alsash.reciper.database;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import java.lang.ref.SoftReference;

/**
 * Single instance for database api
 */
public class ApiDb {

    public static final int VERSION = DaoMaster.SCHEMA_VERSION;

    private static final String DATABASE_NAME = "reciper-db";
    private static final ApiDb INSTANCE = new ApiDb();
    private SoftReference<DaoSession> refDaoSession;
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
            refDaoSession = new SoftReference<>(new DaoMaster(db).newSession());
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

    private static class DbOpenHelper extends DaoMaster.DevOpenHelper {
        private final Context context;

        public DbOpenHelper(Context context, String name) {
            super(context, name);
            this.context = context;
        }

        @Override
        public void onCreate(Database db) {
            super.onCreate(db);
            ApiDb.getInstance().created = true;
        }
    }
}
