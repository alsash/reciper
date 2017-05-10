package com.alsash.reciper.api.storage.local.database;

import android.content.Context;
import android.util.Log;

import com.alsash.reciper.api.storage.local.database.table.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Simple SQLiteOpenHelper for the GreenDao database framework
 */
class DatabaseOpenHelper extends DaoMaster.OpenHelper {

    public DatabaseOpenHelper(Context context, String name) {
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
