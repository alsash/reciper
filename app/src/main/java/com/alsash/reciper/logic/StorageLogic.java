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
        if (!isUsdaFetched()) {
            updateUsda();
        }
    }

    private boolean createStartupEntities() {
        String dbLanguage = cloudManager.getDbLanguage(Locale.getDefault());
        if (dbLanguage == null) dbLanguage = cloudManager.getDbLanguage(Locale.ENGLISH);
        if (dbLanguage == null) return false;
        return dbManager.modifyAllInTx(
                cloudManager.getAuthorTable(dbLanguage),
                cloudManager.getCategoryTable(dbLanguage),
                cloudManager.getFoodMeasureTable(dbLanguage),
                cloudManager.getFoodTable(dbLanguage),
                cloudManager.getFoodUsdaTable(dbLanguage),
                cloudManager.getLabelTable(dbLanguage),
                cloudManager.getPhotoTable(dbLanguage),
                cloudManager.getRecipeFoodTable(dbLanguage),
                cloudManager.getRecipeLabelTable(dbLanguage),
                cloudManager.getRecipeMethodTable(dbLanguage),
                cloudManager.getRecipePhotoTable(dbLanguage),
                cloudManager.getRecipeTable(dbLanguage)
        );
    }

    private boolean isUsdaFetched() {
        return false;
    }

    private void updateUsda() {

    }
}
