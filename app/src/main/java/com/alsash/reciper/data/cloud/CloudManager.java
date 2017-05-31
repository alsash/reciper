package com.alsash.reciper.data.cloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.app.AppContract;
import com.alsash.reciper.app.AppContract.Cloud.Usda;
import com.alsash.reciper.data.cloud.request.GithubDbRequest;
import com.alsash.reciper.data.cloud.request.UsdaRequest;
import com.alsash.reciper.data.cloud.response.GithubDbConfigResponse;
import com.alsash.reciper.data.cloud.response.UsdaFoodsResponse;
import com.alsash.reciper.data.db.table.AuthorTable;
import com.alsash.reciper.data.db.table.CategoryTable;
import com.alsash.reciper.data.db.table.FoodMeasureTable;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.data.db.table.FoodUsdaTable;
import com.alsash.reciper.data.db.table.LabelTable;
import com.alsash.reciper.data.db.table.PhotoTable;
import com.alsash.reciper.data.db.table.RecipeFoodTable;
import com.alsash.reciper.data.db.table.RecipeLabelTable;
import com.alsash.reciper.data.db.table.RecipeMethodTable;
import com.alsash.reciper.data.db.table.RecipePhotoTable;
import com.alsash.reciper.data.db.table.RecipeTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A cloud manager for single access to all cloud services
 */
public class CloudManager {

    private static final int USDA_NUTRITION_ID_PROTEIN = 203;
    private static final int USDA_NUTRITION_ID_FAT = 204;
    private static final int USDA_NUTRITION_ID_CARBS = 205;
    private static final int USDA_NUTRITION_ID_ENERGY = 208;

    private final Context context;
    private final GithubDbRequest githubDbRequest;
    private final UsdaRequest usdaRequest;
    private GithubDbConfigResponse dbConfig;

    public CloudManager(Context context, GithubDbRequest githubDbRequest, UsdaRequest usdaRequest) {
        this.context = context;
        this.githubDbRequest = githubDbRequest;
        this.usdaRequest = usdaRequest;
    }

    @WorkerThread
    public List<FoodTable> getUsdaFoodTable(String... usdaNdbNo) {
        List<FoodTable> foodTables = new ArrayList<>();
        UsdaFoodsResponse response = usdaRequest.getFood(Usda.API_KEY, usdaNdbNo).blockingGet();

        for (UsdaFoodsResponse.FoodContainer foodContainer : response.foods) {
            FoodTable foodTable = new FoodTable();
            foodTable.setName(foodContainer.food.desc.name);
            for (UsdaFoodsResponse.Nutrient nutrient : foodContainer.food.nutrients) {
                switch (nutrient.nutrientId) {
                    case USDA_NUTRITION_ID_PROTEIN:
                        foodTable.setProtein(nutrient.value / 100);
                        break;
                    case USDA_NUTRITION_ID_FAT:
                        foodTable.setFat(nutrient.value / 100);
                        break;
                    case USDA_NUTRITION_ID_CARBS:
                        foodTable.setCarbs(nutrient.value / 100);
                        break;
                    case USDA_NUTRITION_ID_ENERGY:
                        foodTable.setCarbs(nutrient.value);
                        break;
                }
            }
            foodTable.setWeightUnit(AppContract.UNIT.GRAM.toString());
            foodTable.setEnergyUnit(AppContract.UNIT.KCAL.toString());
            foodTables.add(foodTable);
        }
        return foodTables;
    }

    @Nullable
    @WorkerThread
    public Date getDbUpdateDate() {
        return (getDbConfig() == null) ? null : getDbConfig().updatedAt;
    }

    @Nullable
    @WorkerThread
    public String getDbLanguage(Locale userLocale) {
        if (dbConfig == null) dbConfig = getDbConfig();
        if (dbConfig == null) return null;
        String userLanguage = userLocale.getLanguage().toLowerCase();
        for (String language : dbConfig.languages) {
            if (userLanguage.equals(language.toLowerCase())) {
                return language.toLowerCase();
            }
        }
        return null;
    }

    @Nullable
    @WorkerThread
    public List<AuthorTable> getDbAuthorTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getAuthorTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<CategoryTable> getDbCategoryTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getCategoryTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<FoodMeasureTable> getDbFoodMeasureTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getFoodMeasureTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<FoodTable> getDbFoodTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getFoodTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<FoodUsdaTable> getDbFoodUsdaTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getFoodUsdaTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<LabelTable> getDbLabelTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getLabelTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<PhotoTable> getDbPhotoTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getPhotoTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipeFoodTable> getDbRecipeFoodTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipeFoodTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipeLabelTable> getDbRecipeLabelTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipeLabelTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipeMethodTable> getDbRecipeMethodTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipeMethodTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipePhotoTable> getDbRecipePhotoTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipePhotoTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipeTable> getDbRecipeTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipeTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    @WorkerThread
    private GithubDbConfigResponse getDbConfig() {
        if (dbConfig != null) return dbConfig;
        if (!isOnline()) return null;
        dbConfig = githubDbRequest.getConfig().blockingGet();
        return dbConfig;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null
                && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected());
    }
}
