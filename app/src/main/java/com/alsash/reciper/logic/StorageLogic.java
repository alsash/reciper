package com.alsash.reciper.logic;

import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.logic.exception.NoInternetException;

import java.util.Date;
import java.util.Locale;

/**
 * Logic for processing data in storage
 */
public class StorageLogic {

    private final CloudManager cloudManager;
    private final DbManager dbManager;

    public StorageLogic(DbManager dbManager, CloudManager cloudManager) {
        this.dbManager = dbManager;
        this.cloudManager = cloudManager;
    }

    public void createStartupEntitiesIfNeed() throws NoInternetException {
        Date localUpdateDate = dbManager.getSettingsUpdateDate();
        Date cloudUpdateDate = cloudManager.getDbUpdateDate();
        boolean create;
        // First access
        if (localUpdateDate == null) {
            if (cloudUpdateDate != null) {
                create = true;
            } else if (!cloudManager.isOnline()) {
                throw new NoInternetException();
            } else {
                create = false; // Something goes wrong, but we can't do anything...
            }
            // Next access
        } else {
            create = false;
            if (cloudUpdateDate != null) {
                create = localUpdateDate.getTime() < cloudUpdateDate.getTime();
            }
        }
        if (create) {
            boolean created = createStartupEntities();
            if (created) dbManager.setSettingsUpdateDate(new Date());
        }
    }

    private boolean createStartupEntities() {
        String pathDbLanguage = cloudManager.getDbLanguage(Locale.getDefault());
        if (pathDbLanguage == null) pathDbLanguage = cloudManager.getDbLanguage(Locale.ENGLISH);
        if (pathDbLanguage == null) return false;
        return dbManager.insertAllInTx(
                cloudManager.getAuthorTable(pathDbLanguage),
                cloudManager.getCategoryTable(pathDbLanguage),
                cloudManager.getFoodMeasureTable(pathDbLanguage),
                cloudManager.getFoodTable(pathDbLanguage),
                cloudManager.getFoodUsdaTable(pathDbLanguage),
                cloudManager.getLabelTable(pathDbLanguage),
                cloudManager.getPhotoTable(pathDbLanguage),
                cloudManager.getRecipeFoodTable(pathDbLanguage),
                cloudManager.getRecipeLabelTable(pathDbLanguage),
                cloudManager.getRecipeMethodTable(pathDbLanguage),
                cloudManager.getRecipePhotoTable(pathDbLanguage),
                cloudManager.getRecipeTable(pathDbLanguage)
        );
    }
}
