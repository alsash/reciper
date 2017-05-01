package com.alsash.reciper.api;

import android.content.Context;

import com.alsash.reciper.api.storage.local.database.DatabaseApi;


/**
 * Single Api for all storage
 */
public class StorageApi {
    private final Context context;
    private final DatabaseApi databaseApi;

    public StorageApi(Context context) {
        this.context = context;
        this.databaseApi = new DatabaseApi(context);
    }

    public DatabaseApi getDatabaseApi() {
        return databaseApi;
    }
}
