package com.alsash.reciper.logic;


import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.logic.exception.NoInternetException;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Logic for processing data in storage.
 * All methods, except constructor, must be called on background thread
 */
public class StorageLogic {

    private final CloudManager cloudManager;
    private final DbManager dbManager;

    public StorageLogic(DbManager dbManager, CloudManager cloudManager) {
        this.dbManager = dbManager;
        this.cloudManager = cloudManager;
    }

    public boolean createStartupEntitiesIfNeed() throws NoInternetException {
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
            return created;
        }
        return false;
    }

    private boolean createStartupEntities() {
        String dbLanguage = cloudManager.getDbLanguage(Locale.getDefault());
        if (dbLanguage == null) dbLanguage = cloudManager.getDbLanguage(Locale.ENGLISH);
        if (dbLanguage == null) return false;
        boolean modify = dbManager.modifyAll(
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
        boolean update = updateFoodUsda();
        return modify && update;
    }

    private boolean updateFoodUsda() {
        List<FoodTable> dbFoods;
        do {
            // Load foods, without updated nutrition values
            dbFoods = dbManager.restrictWith(0, 10).getFoodTable(false);
            if (dbFoods.size() == 0) break;
            for (FoodTable dbFood : dbFoods) {
                String ndbNo = (dbFood.getFoodUsdaTables().size() == 0) ? null :
                        dbFood.getFoodUsdaTables().get(0).getNdbNo();
                if (ndbNo == null) continue;
                // Fetching nutrition values
                FoodTable usdaFood = cloudManager.getUsdaFoodTable(ndbNo);
                // Updating nutrition values
                if (dbFood.getName() == null) dbFood.setName(usdaFood.getName());
                dbFood.setProtein(usdaFood.getProtein());
                dbFood.setFat(usdaFood.getFat());
                dbFood.setCarbs(usdaFood.getCarbs());
                dbFood.setWeightUnit(usdaFood.getWeightUnit());
                dbFood.setEnergy(usdaFood.getEnergy());
                dbFood.setEnergyUnit(usdaFood.getEnergyUnit());
                dbFood.getFoodUsdaTables().get(0).setFetched(true);
            }
            dbManager.modifyDeepFoodTable(dbFoods);
        } while (dbFoods.size() > 0);
        return true;
    }
}
