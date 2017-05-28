package com.alsash.reciper.data.cloud;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.alsash.reciper.data.cloud.request.GithubDbRequest;
import com.alsash.reciper.data.cloud.request.UsdaRequest;
import com.alsash.reciper.data.cloud.response.GithubDbConfigResponse;
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
 * A cloud manager for single access to all cloud services
 */
public class CloudManager {

    private final Context context;
    private final GithubDbRequest githubDbRequest;
    private final UsdaRequest usdaRequest;
    private GithubDbConfigResponse dbConfig;

    public CloudManager(Context context, GithubDbRequest githubDbRequest, UsdaRequest usdaRequest) {
        this.context = context;
        this.githubDbRequest = githubDbRequest;
        this.usdaRequest = usdaRequest;
    }

    @Nullable
    @WorkerThread
    public Date getDbUpdateDate() {
        return (getDbConfig() == null) ? null : getDbConfig().dbUpdateDate;
    }

    @Nullable
    @WorkerThread
    public String getPathDbLanguage(Locale userLocale) {
        if (dbConfig == null) dbConfig = getDbConfig();
        if (dbConfig == null) return null;
        String userLanguage = userLocale.getLanguage().toLowerCase();
        for (String dbLanguage : dbConfig.dbLanguages) {
            if (userLanguage.equals(dbLanguage.toLowerCase())) {
                return dbConfig.dbPath + dbLanguage.toLowerCase();
            }
        }
        return null;
    }

    @Nullable
    @WorkerThread
    public List<AuthorTable> getAuthorTable(String pathDbLanguage) {
        return githubDbRequest.getAuthorTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<CategoryTable> getCategoryTable(String pathDbLanguage) {
        return githubDbRequest.getCategoryTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<FoodMeasureTable> getFoodMeasureTable(String pathDbLanguage) {
        return githubDbRequest.getFoodMeasureTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<FoodTable> getFoodTable(String pathDbLanguage) {
        return githubDbRequest.getFoodTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<FoodUsdaTable> getFoodUsdaTable(String pathDbLanguage) {
        return githubDbRequest.getFoodUsdaTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<LabelTable> getLabelTable(String pathDbLanguage) {
        return githubDbRequest.getLabelTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<PhotoTable> getPhotoTable(String pathDbLanguage) {
        return githubDbRequest.getPhotoTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipeFoodTable> getRecipeFoodTable(String pathDbLanguage) {
        return githubDbRequest.getRecipeFoodTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipeLabelTable> getRecipeLabelTable(String pathDbLanguage) {
        return githubDbRequest.getRecipeLabelTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipeMethodTable> getRecipeMethodTable(String pathDbLanguage) {
        return githubDbRequest.getRecipeMethodTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipePhotoTable> getRecipePhotoTable(String pathDbLanguage) {
        return githubDbRequest.getRecipePhotoTable(pathDbLanguage).blockingGet();
    }

    @Nullable
    @WorkerThread
    public List<RecipeTable> getRecipeTable(String pathDbLanguage) {
        return githubDbRequest.getRecipeTable(pathDbLanguage).blockingGet();
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
