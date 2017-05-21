package com.alsash.reciper.api.storage.local.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alsash.reciper.api.storage.local.database.table.DaoMaster;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

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
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DatabaseApi", "Upgrading schema from version " + oldVersion +
                " to " + newVersion + " by dropping all tables");
        DaoMaster.dropAllTables(new StandardDatabase(db), true);
        onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("DatabaseApi", "Upgrading schema from version " + oldVersion +
                " to " + newVersion + " by dropping all tables");
        DaoMaster.dropAllTables(db, true);
        onCreate(db);
    }
}
