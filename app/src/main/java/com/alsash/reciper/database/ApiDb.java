package com.alsash.reciper.database;

import android.content.Context;
import android.util.Log;

import com.alsash.reciper.api.database.DaoMaster;
import com.alsash.reciper.api.database.DaoSession;

import org.greenrobot.greendao.database.Database;

import java.lang.ref.SoftReference;

/**
 * Single instance for database api
 */

public class ApiDb {

    private static final String DATABASE_NAME = "reciper-db";
    private static final ApiDb INSTANCE = new ApiDb();
    private SoftReference<DaoSession> refDaoSession;

    private ApiDb() {
    }

    public static ApiDb getInstance() {
        return INSTANCE;
    }

    public synchronized DaoSession getSession(Context context) {
        if (refDaoSession == null || refDaoSession.get() == null) {
            Context appContext = context.getApplicationContext();
            Database db = new DaoMaster.DevOpenHelper(appContext, DATABASE_NAME).getWritableDb();
            refDaoSession = new SoftReference<>(new DaoMaster(db).newSession());
        }
        return refDaoSession.get();
    }

    private void setStartValues(Context context) {
        Log.i("Reciper", "Setting start database values");
        // DaoSession session = getSession(context);

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
            ApiDb.getInstance().setStartValues(context);
        }
    }
}
