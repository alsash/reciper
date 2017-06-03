package com.alsash.reciper.logic;

import android.os.Looper;

import com.alsash.reciper.logic.exception.MainThreadException;
import com.alsash.reciper.logic.exception.NoInternetException;

import java.util.Date;

/**
 * Application business logic.
 * Makes business processes clear for understanding and usage.
 */
public class BusinessLogic {

    private static final String TAG = "BusinessLogic";

    private final StorageLogic storageLogic;

    public BusinessLogic(StorageLogic storageLogic) {
        this.storageLogic = storageLogic;
    }

    /**
     * Create startup entities in database. Must be called on background thread.
     */
    public void createStartupEntitiesIfNeededInBackground() throws MainThreadException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new MainThreadException(TAG + " : create startup entities");
        }
        Date createdAt = new Date();
        try {
            if (storageLogic.isDbUpToDate()) return;
            if (!storageLogic.createStartupEntities(createdAt)) {
                storageLogic.deleteAllEntitiesCreatedAt(createdAt);
            }
        } catch (NoInternetException e) {
            storageLogic.deleteAllEntitiesCreatedAt(createdAt);
        }
    }
}
