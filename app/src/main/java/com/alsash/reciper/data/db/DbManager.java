package com.alsash.reciper.data.db;

import android.content.Context;
import android.support.annotation.Nullable;

import com.alsash.reciper.data.db.table.AuthorTable;
import com.alsash.reciper.data.db.table.CategoryTable;
import com.alsash.reciper.data.db.table.DaoMaster;
import com.alsash.reciper.data.db.table.DaoSession;
import com.alsash.reciper.data.db.table.FoodMeasureTable;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.data.db.table.FoodTableDao;
import com.alsash.reciper.data.db.table.FoodUsdaTable;
import com.alsash.reciper.data.db.table.FoodUsdaTableDao;
import com.alsash.reciper.data.db.table.LabelTable;
import com.alsash.reciper.data.db.table.PhotoTable;
import com.alsash.reciper.data.db.table.RecipeFoodTable;
import com.alsash.reciper.data.db.table.RecipeLabelTable;
import com.alsash.reciper.data.db.table.RecipeMethodTable;
import com.alsash.reciper.data.db.table.RecipePhotoTable;
import com.alsash.reciper.data.db.table.RecipeTable;
import com.alsash.reciper.data.db.table.SettingsTable;
import com.alsash.reciper.data.db.table.SettingsTableDao;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An Database manager for single access to all database tables.
 * All methods, except constructor, must be called on background thread
 */
public class DbManager {

    private static final String TAG = "DbManager";
    private static final String SETTINGS_KEY_UPDATE_DATE = "update_date";
    private DaoSession daoSession;
    private Query<SettingsTable> settingsQuery;
    private boolean restrict;
    private int offset;
    private int limit;

    public DbManager(Context context, String databaseName) {
        Database database = new DbHelper(context, databaseName).getWritableDb();
        daoSession = new DaoMaster(database).newSession();
    }

    public DbManager restrictWith(int offset, int limit) {
        this.restrict = true;
        this.offset = offset;
        this.limit = limit;
        return this;
    }

    public List<FoodTable> getFoodTable(boolean usdaFetched) {
        QueryBuilder<FoodTable> builder = daoSession.getFoodTableDao().queryBuilder();
        obtainRestriction(builder);
        builder.join(FoodTableDao.Properties.Uuid,
                FoodUsdaTable.class,
                FoodUsdaTableDao.Properties.FoodUuid)
                .where(FoodUsdaTableDao.Properties.Fetched.eq((usdaFetched) ? 1 : 0));
        return builder.build().list();
    }

    public void modifyDeepFoodTable(final List<FoodTable> foodTableList) {
        daoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                daoSession.getFoodTableDao().insertOrReplaceInTx(foodTableList);
                List<FoodUsdaTable> foodUsdaTableList = new ArrayList<>();
                List<FoodMeasureTable> foodMeasureTableList = new ArrayList<>();
                for (FoodTable foodTable : foodTableList) {
                    foodUsdaTableList.addAll(foodTable.getFoodUsdaTables());
                    foodMeasureTableList.addAll(foodTable.getFoodMeasureTables());
                }
                if (foodUsdaTableList.size() > 0)
                    daoSession.getFoodUsdaTableDao().insertOrReplaceInTx(foodUsdaTableList);
                if (foodMeasureTableList.size() > 0)
                    daoSession.getFoodMeasureTableDao().insertOrReplaceInTx(foodMeasureTableList);
            }
        });
    }

    public boolean modifyAll(@Nullable final List<AuthorTable> authors,
                             @Nullable final List<CategoryTable> categories,
                             @Nullable final List<FoodMeasureTable> foodMeasures,
                             @Nullable final List<FoodTable> foods,
                             @Nullable final List<FoodUsdaTable> foodUsda,
                             @Nullable final List<LabelTable> labels,
                             @Nullable final List<PhotoTable> photos,
                             @Nullable final List<RecipeFoodTable> recipeFoods,
                             @Nullable final List<RecipeLabelTable> recipeLabels,
                             @Nullable final List<RecipeMethodTable> recipeMethods,
                             @Nullable final List<RecipePhotoTable> recipePhotos,
                             @Nullable final List<RecipeTable> recipes) {
        if (authors == null
                && categories == null
                && foodMeasures == null
                && foods == null
                && foodUsda == null
                && labels == null
                && photos == null
                && recipeFoods == null
                && recipeLabels == null
                && recipeMethods == null
                && recipePhotos == null
                && recipes == null) {
            return false;
        }
        daoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                if (authors != null)
                    daoSession.getAuthorTableDao().insertOrReplaceInTx(authors);
                if (categories != null)
                    daoSession.getCategoryTableDao().insertOrReplaceInTx(categories);
                if (foodMeasures != null)
                    daoSession.getFoodMeasureTableDao().insertOrReplaceInTx(foodMeasures);
                if (foods != null)
                    daoSession.getFoodTableDao().insertOrReplaceInTx(foods);
                if (foodUsda != null)
                    daoSession.getFoodUsdaTableDao().insertOrReplaceInTx(foodUsda);
                if (labels != null)
                    daoSession.getLabelTableDao().insertOrReplaceInTx(labels);
                if (photos != null)
                    daoSession.getPhotoTableDao().insertOrReplaceInTx(photos);
                if (recipeFoods != null)
                    daoSession.getRecipeFoodTableDao().insertOrReplaceInTx(recipeFoods);
                if (recipeLabels != null)
                    daoSession.getRecipeLabelTableDao().insertOrReplaceInTx(recipeLabels);
                if (recipeMethods != null)
                    daoSession.getRecipeMethodTableDao().insertOrReplaceInTx(recipeMethods);
                if (recipePhotos != null)
                    daoSession.getRecipePhotoTableDao().insertOrReplaceInTx(recipePhotos);
                if (recipes != null)
                    daoSession.getRecipeTableDao().insertOrReplaceInTx(recipes);
            }
        });
        return true;
    }

    @Nullable
    public Date getSettingsUpdateDate() {
        String updateDateString = getSettingsValue(SETTINGS_KEY_UPDATE_DATE);
        return (updateDateString == null) ? null : new Date(Long.parseLong(updateDateString));
    }

    public void setSettingsUpdateDate(Date date) {
        setSettingsValue(SETTINGS_KEY_UPDATE_DATE, Long.toString(date.getTime()));
    }

    @Nullable
    private String getSettingsValue(String key) {
        if (settingsQuery == null) {
            settingsQuery = daoSession
                    .getSettingsTableDao()
                    .queryBuilder()
                    .where(SettingsTableDao.Properties.Key.eq(key))
                    .build();
        } else {
            settingsQuery.setParameter(0, key);
        }
        List<SettingsTable> settings = settingsQuery.forCurrentThread().list();
        if (settings != null && settings.size() > 0) {
            return settings.get(0).getVal();
        } else {
            return null;
        }
    }

    private void obtainRestriction(QueryBuilder<?> builder) {
        if (restrict) {
            builder.offset(offset).limit(limit);
            restrict = false;
        }
    }

    private void setSettingsValue(String key, String val) {
        daoSession.getSettingsTableDao().insertOrReplaceInTx(new SettingsTable(null, key, val));
    }
}
