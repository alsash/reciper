package com.alsash.reciper.logic;

import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.data.db.table.FoodUsdaTable;
import com.alsash.reciper.logic.exception.NoInternetException;

import java.util.Date;
import java.util.List;
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
        if (!isFoodUsdaFetched()) {
            updateFoodUsda();
        }
    }

    private boolean createStartupEntities() {
        String dbLanguage = cloudManager.getDbLanguage(Locale.getDefault());
        if (dbLanguage == null) dbLanguage = cloudManager.getDbLanguage(Locale.ENGLISH);
        if (dbLanguage == null) return false;
        return dbManager.modifyAllInTx(
                cloudManager.getDbAuthorTable(dbLanguage),
                cloudManager.getDbCategoryTable(dbLanguage),
                cloudManager.getDbFoodMeasureTable(dbLanguage),
                cloudManager.getDbFoodTable(dbLanguage),
                cloudManager.getDbFoodUsdaTable(dbLanguage),
                cloudManager.getDbLabelTable(dbLanguage),
                cloudManager.getDbPhotoTable(dbLanguage),
                cloudManager.getDbRecipeFoodTable(dbLanguage),
                cloudManager.getDbRecipeLabelTable(dbLanguage),
                cloudManager.getDbRecipeMethodTable(dbLanguage),
                cloudManager.getDbRecipePhotoTable(dbLanguage),
                cloudManager.getDbRecipeTable(dbLanguage)
        );
    }

    private boolean isFoodUsdaFetched() {
        return dbManager.restrictWith(0, 1).getFoodUsdaTable(false).size() == 0;
    }

    private void updateFoodUsda() {
        List<FoodUsdaTable> foodUsdaTables = dbManager.restrictWith(0, 10).getFoodUsdaTable(false);
        if (foodUsdaTables.size() == 0) return;
        String[] ndbNos = new String[foodUsdaTables.size()];
        for (int i = 0; i < foodUsdaTables.size(); i++) {
            ndbNos[i] = foodUsdaTables.get(i).getNdbNo();
        }
        List<FoodTable> foodTables = cloudManager.getUsdaFoodTable(ndbNos);

    }
}
