package com.alsash.reciper.logic;


import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.logic.exception.MainThreadException;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Logic for processing data in storage.
 * All methods, except constructor, must be called on the background thread.
 */
public class StorageLogic {

    private static final String TAG = "StorageLogic";

    private final CloudManager cloudManager;
    private final DbManager dbManager;

    public StorageLogic(DbManager dbManager, CloudManager cloudManager) {
        this.dbManager = dbManager;
        this.cloudManager = cloudManager;
    }

    /**
     * Fetch startup entities from cloud and create it in database.
     * Must be called on the background thread.
     */
    public void createStartupEntitiesIfNeed() throws Exception {
        MainThreadException.throwOnMainThread(TAG, "createStartupEntitiesIfNeed()");
        if (isDbUpToDate()) return;
        Date createdAt = new Date();
        if (createStartupEntities(createdAt)) {
            dbManager.setSettingsUpdateDate(createdAt);
        } else {
            deleteAllEntitiesCreatedAt(createdAt);
        }
    }

    private boolean isDbUpToDate() {
        Date localUpdateDate = dbManager.getSettingsUpdateDate();
        // First access
        if (localUpdateDate == null) return false;

        Date cloudUpdateDate = cloudManager.getDbUpdateDate();
        // Next access
        if (cloudUpdateDate == null) return true;

        // Standard access
        return localUpdateDate.getTime() >= cloudUpdateDate.getTime();
    }

    private void deleteAllEntitiesCreatedAt(Date createdAt) {
        dbManager.changeWith(createdAt).deleteAll();
    }

    private boolean createStartupEntities(Date createdAt) {
        if (!cloudManager.isOnline()) return false;
        String dbLanguage = cloudManager.getDbLanguage(Locale.getDefault());
        if (dbLanguage == null) dbLanguage = cloudManager.getDbLanguage(Locale.ENGLISH);
        if (dbLanguage == null) return false;
        boolean modify = dbManager
                .changeWith(createdAt)
                .modifyAll(
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
        boolean update = updateFoodUsda(createdAt);
        return modify && update;
    }

    private boolean updateFoodUsda(Date updateDate) {
        List<FoodTable> dbFoods;
        if (!cloudManager.isOnline()) return false; // not updated
        do {
            // Loading foods, up to 20 items, without updated nutrition values
            dbFoods = dbManager.restrictWith(0, 20).getFoodTable(false);
            if (dbFoods.size() == 0) break;

            // Fetching nutrition values
            String[] ndbNos = new String[dbFoods.size()];
            for (int i = 0; i < dbFoods.size(); i++) {
                ndbNos[i] = dbFoods.get(i).getFoodUsdaTables().get(0).getNdbNo();
            }
            Map<String, FoodTable> usdaResult = cloudManager.getUsdaFoodTable(ndbNos);
            if (usdaResult.size() == 0) return false; // not updated

            // Updating nutrition values
            for (Map.Entry<String, FoodTable> entry : usdaResult.entrySet()) {
                boolean found = false;
                for (FoodTable dbFood : dbFoods) {
                    if (dbFood.getFoodUsdaTables().get(0).getNdbNo().equals(entry.getKey())) {
                        FoodTable usdaFood = entry.getValue();
                        if (dbFood.getName() == null) dbFood.setName(usdaFood.getName());
                        dbFood.setProtein(usdaFood.getProtein());
                        dbFood.setFat(usdaFood.getFat());
                        dbFood.setCarbs(usdaFood.getCarbs());
                        dbFood.setWeightUnit(usdaFood.getWeightUnit());
                        dbFood.setEnergy(usdaFood.getEnergy());
                        dbFood.setEnergyUnit(usdaFood.getEnergyUnit());
                        dbFood.getFoodUsdaTables().get(0).setFetched(true);
                        found = true;
                        break;
                    }
                }
                if (!found) return false; // not updated
            }
            dbManager.changeWith(updateDate).modifyDeepFoodTable(dbFoods);
        } while (dbFoods.size() > 0);
        return true;
    }
}
