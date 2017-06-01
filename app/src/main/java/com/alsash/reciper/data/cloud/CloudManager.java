package com.alsash.reciper.data.cloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;

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

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A cloud manager for single access to all cloud services.
 * All methods, except constructor, must be called on background thread
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

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null
                && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnected());
    }

    public FoodTable getUsdaFoodTable(String usdaNdbNo) {
        FoodTable foodTable = new FoodTable();
        foodTable.setWeightUnit(AppContract.UNIT.GRAM.toString());
        foodTable.setEnergyUnit(AppContract.UNIT.KCAL.toString());

        if (!isOnline()) return foodTable;

        UsdaFoodsResponse response = usdaRequest
                .getFood(Usda.API_KEY, usdaNdbNo)
                .onErrorComplete()
                .blockingGet();
        if (response == null
                || response.foods == null
                || response.foods.size() == 0) {
            return foodTable;
        }

        foodTable.setName(response.foods.get(0).food.desc.name);

        if (response.foods.get(0).food.nutrients == null) {
            return foodTable;
        }

        for (UsdaFoodsResponse.Nutrient nutrient : response.foods.get(0).food.nutrients) {
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
                    foodTable.setEnergy(nutrient.value);
                    break;
            }
        }
        return foodTable;
    }

    @Nullable
    public Date getDbUpdateDate() {
        return (getDbConfig() == null) ? null : getDbConfig().updatedAt;
    }

    @Nullable
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
    public List<AuthorTable> getDbAuthorTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getAuthorTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<CategoryTable> getDbCategoryTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getCategoryTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<FoodMeasureTable> getDbFoodMeasureTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getFoodMeasureTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<FoodTable> getDbFoodTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getFoodTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<FoodUsdaTable> getDbFoodUsdaTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getFoodUsdaTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<LabelTable> getDbLabelTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getLabelTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<PhotoTable> getDbPhotoTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getPhotoTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<RecipeFoodTable> getDbRecipeFoodTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipeFoodTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<RecipeLabelTable> getDbRecipeLabelTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipeLabelTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<RecipeMethodTable> getDbRecipeMethodTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipeMethodTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<RecipePhotoTable> getDbRecipePhotoTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipePhotoTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    public List<RecipeTable> getDbRecipeTable(String language) {
        return (getDbConfig() == null) ? null :
                githubDbRequest.getRecipeTable(getDbConfig().version, language).blockingGet();
    }

    @Nullable
    private GithubDbConfigResponse getDbConfig() {
        if (dbConfig != null) return dbConfig;
        if (!isOnline()) return null;
        dbConfig = githubDbRequest.getConfig().blockingGet();
        return dbConfig;
    }
}
