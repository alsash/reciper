package com.alsash.reciper.logic;

import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.logic.error.NoInternetConnectionException;

import java.util.Date;

/**
 * Created by alsash on 5/26/17.
 */

public class StorageLogic {

    private final CloudManager cloudManager;
    private final DbManager dbManager;

    public StorageLogic(DbManager dbManager, CloudManager cloudManager) {
        this.dbManager = dbManager;
        this.cloudManager = cloudManager;
    }

    /**
     * Check whether the startup entities are stored in the database
     *
     * @return true if there is no need to update database from cloud
     */
    public boolean isDbUpToDate() {
        return false;
    }

    public void createStartupEntitiesIfNeed() throws NoInternetConnectionException {
        Date localUpdateDate = dbManager.getSettingsUpdateDate();
        Date cloudUpdateDate = cloudManager.getDbUpdateDate();
        boolean create = false;
        if (localUpdateDate != null && cloudUpdateDate != null) {
            create = localUpdateDate.getTime() >= cloudUpdateDate.getTime();
        } else if (cloudUpdateDate == null && !cloudManager.isOnline()) {
            throw new NoInternetConnectionException();
        }
        if (create) createStartupEntities();
    }

    private void createStartupEntities() {

    }

}
