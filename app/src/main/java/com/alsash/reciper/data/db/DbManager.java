package com.alsash.reciper.data.db;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.data.db.table.DaoMaster;
import com.alsash.reciper.data.db.table.DaoSession;
import com.alsash.reciper.data.db.table.SettingsTable;
import com.alsash.reciper.data.db.table.SettingsTableDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;

import java.util.Date;
import java.util.List;

/**
 * An Database manager for single access to all database tables
 */
public class DbManager {

    private static final String SETTINGS_KEY_UPDATE_DATE = "update_date";
    private DaoSession daoSession;
    private Query<SettingsTable> settingsQuery;
    private boolean restrict;
    private int limit;
    private int offset;

    public DbManager(Context context, String databaseName) {
        Database database = new DbHelper(context, databaseName).getWritableDb();
        daoSession = new DaoMaster(database).newSession();
    }

    public void clearCache() {
        daoSession.clear();
    }

    public DbManager restrictWith(int limit, int offset) {
        this.restrict = true;
        this.limit = limit;
        this.offset = offset;
        return this;
    }

    @Nullable
    @WorkerThread
    public Date getSettingsUpdateDate() {
        String updateDateString = getSettingsValue(SETTINGS_KEY_UPDATE_DATE);
        return (updateDateString == null) ? null : new Date(Long.parseLong(updateDateString));
    }

    @WorkerThread
    public void setSettingsUpdateDate(Date date) {
        setSettingsValue(SETTINGS_KEY_UPDATE_DATE, Long.toString(date.getTime()));
    }

    @Nullable
    private String getSettingsValue(String key) {
        if (settingsQuery == null) {
            settingsQuery = daoSession
                    .getSettingsTableDao()
                    .queryBuilder()
                    .where(SettingsTableDao.Properties.Key.eq(key))
                    .build();
        } else {
            settingsQuery.setParameter(0, key);
        }
        List<SettingsTable> settings = settingsQuery.forCurrentThread().list();
        if (settings != null && settings.size() > 0) {
            return settings.get(0).getVal();
        } else {
            return null;
        }
    }

    private void setSettingsValue(String key, String val) {
        daoSession.getSettingsTableDao().updateInTx(new SettingsTable(null, key, val));
    }
}
