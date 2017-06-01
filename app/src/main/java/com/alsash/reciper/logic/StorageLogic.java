package com.alsash.reciper.logic;

import com.alsash.reciper.data.cloud.CloudManager;
import com.alsash.reciper.data.db.DbManager;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.logic.exception.NoInternetException;

import java.util.ArrayList;
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
            int offset = 0;
            int limit = 10;
            dbFoods = dbManager.restrictWith(offset, limit).getFoodTable(false);
            if (dbFoods.size() == 0) break;
            // Getting ndbno ids
            List<String> ndbNoList = new ArrayList<>();
            for (int i = 0; i < dbFoods.size(); i++) {
                try {
                    String ndbNo = dbFoods.get(i).getFoodUsdaTables().get(i).getNdbNo();
                    if (ndbNo != null) ndbNoList.add(ndbNo);
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }
            // Fetching nutrition values from usda
            List<FoodTable> udsaFoods = cloudManager.getUsdaFoodTable(
                    ndbNoList.toArray(new String[ndbNoList.size()]));
            // Updating nutrition values
            for (FoodTable usdaFood : udsaFoods) {
                for (FoodTable dbFood : dbFoods) {
                    try {
                        if (dbFood.getFoodUsdaTables().get(0).getNdbNo().equals(
                                usdaFood.getFoodUsdaTables().get(0).getNdbNo())) {
                            if (dbFood.getName() == null) dbFood.setName(usdaFood.getName());
                            dbFood.setProtein(usdaFood.getProtein());
                            dbFood.setFat(usdaFood.getFat());
                            dbFood.setCarbs(usdaFood.getCarbs());
                            dbFood.setWeightUnit(usdaFood.getWeightUnit());
                            dbFood.setEnergy(usdaFood.getEnergy());
                            dbFood.setEnergyUnit(usdaFood.getEnergyUnit());
                            dbFood.getFoodUsdaTables().get(0).setFetched(true);
                            break; // dbFoods
                        }
                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }
                }
            }
            dbManager.modifyDeepFoodTable(dbFoods);
            offset = limit;
            limit += 10;
        } while (dbFoods.size() > 0);
        return true;

    }
}
