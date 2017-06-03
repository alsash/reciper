package com.alsash.reciper.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alsash.reciper.data.db.table.DaoMaster;

import org.greenrobot.greendao.database.StandardDatabase;

/**
 * Simple SQLiteOpenHelper for the GreenDao database framework
 */
public class DbHelper extends DaoMaster.OpenHelper {

    public DbHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DatabaseApi", "Upgrading schema from version " + oldVersion +
                " to " + newVersion + " by dropping all tables");
        DaoMaster.dropAllTables(new StandardDatabase(db), true);
        onCreate(db);
    }

    @Override
    public void onUpgrade(org.greenrobot.greendao.database.Database db, int oldVersion, int newVersion) {
        Log.i("DatabaseApi", "Upgrading schema from version " + oldVersion +
                " to " + newVersion + " by dropping all tables");
        DaoMaster.dropAllTables(db, true);
        onCreate(db);
    }
}
