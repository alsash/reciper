package com.alsash.reciper.data.db;

import android.support.annotation.Nullable;

import com.alsash.reciper.data.db.table.AuthorTable;
import com.alsash.reciper.data.db.table.AuthorTableDao;
import com.alsash.reciper.data.db.table.CategoryTable;
import com.alsash.reciper.data.db.table.CategoryTableDao;
import com.alsash.reciper.data.db.table.DaoSession;
import com.alsash.reciper.data.db.table.FoodMeasureTable;
import com.alsash.reciper.data.db.table.FoodMeasureTableDao;
import com.alsash.reciper.data.db.table.FoodTable;
import com.alsash.reciper.data.db.table.FoodTableDao;
import com.alsash.reciper.data.db.table.FoodUsdaTable;
import com.alsash.reciper.data.db.table.FoodUsdaTableDao;
import com.alsash.reciper.data.db.table.LabelTable;
import com.alsash.reciper.data.db.table.LabelTableDao;
import com.alsash.reciper.data.db.table.PhotoTable;
import com.alsash.reciper.data.db.table.PhotoTableDao;
import com.alsash.reciper.data.db.table.RecipeFoodTable;
import com.alsash.reciper.data.db.table.RecipeFoodTableDao;
import com.alsash.reciper.data.db.table.RecipeLabelTable;
import com.alsash.reciper.data.db.table.RecipeLabelTableDao;
import com.alsash.reciper.data.db.table.RecipeMethodTable;
import com.alsash.reciper.data.db.table.RecipeMethodTableDao;
import com.alsash.reciper.data.db.table.RecipePhotoTable;
import com.alsash.reciper.data.db.table.RecipePhotoTableDao;
import com.alsash.reciper.data.db.table.RecipeTable;
import com.alsash.reciper.data.db.table.RecipeTableDao;
import com.alsash.reciper.data.db.table.SettingsTable;
import com.alsash.reciper.data.db.table.SettingsTableDao;
import com.alsash.reciper.data.db.table.Table;

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
    private Date changeDate;

    public DbManager(DaoSession daoSession) {
        this.daoSession = daoSession;
        this.changeDate = new Date();
    }

    public DbManager restrictWith(int offset, int limit) {
        this.restrict = true;
        this.offset = offset;
        this.limit = limit;
        return this;
    }

    public DbManager changeWith(Date date) {
        if (date != null) this.changeDate = date;
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
                obtainChangeDate(foodTableList);
                daoSession.getFoodTableDao().insertOrReplaceInTx(foodTableList);
                List<FoodUsdaTable> foodUsdaTableList = new ArrayList<>();
                List<FoodMeasureTable> foodMeasureTableList = new ArrayList<>();
                for (FoodTable foodTable : foodTableList) {
                    foodUsdaTableList.addAll(foodTable.getFoodUsdaTables());
                    foodMeasureTableList.addAll(foodTable.getFoodMeasureTables());
                }
                if (foodUsdaTableList.size() > 0)
                    obtainChangeDate(foodUsdaTableList);
                daoSession.getFoodUsdaTableDao().insertOrReplaceInTx(foodUsdaTableList);
                if (foodMeasureTableList.size() > 0)
                    obtainChangeDate(foodMeasureTableList);
                daoSession.getFoodMeasureTableDao().insertOrReplaceInTx(foodMeasureTableList);
            }
        });
    }

    /**
     * Update all entities in database, set {@link #changeDate} to all entities
     *
     * @return true if all tables updated, false otherwise
     */
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
        obtainChangeDate(authors);
        obtainChangeDate(categories);
        obtainChangeDate(foodMeasures);
        obtainChangeDate(foods);
        obtainChangeDate(foodUsda);
        obtainChangeDate(labels);
        obtainChangeDate(photos);
        obtainChangeDate(recipeFoods);
        obtainChangeDate(recipeLabels);
        obtainChangeDate(recipeMethods);
        obtainChangeDate(recipePhotos);
        obtainChangeDate(recipes);
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
        return !(authors == null
                || categories == null
                || foodMeasures == null
                || foods == null
                || foodUsda == null
                || labels == null
                || photos == null
                || recipeFoods == null
                || recipeLabels == null
                || recipeMethods == null
                || recipePhotos == null
                || recipes == null);
    }

    /**
     * Delete all entities, that changed at {@link #changeDate}
     */
    public void deleteAll() {
        daoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                daoSession.getAuthorTableDao().deleteInTx(
                        daoSession.getAuthorTableDao().queryBuilder()
                                .where(AuthorTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getCategoryTableDao().deleteInTx(
                        daoSession.getCategoryTableDao().queryBuilder()
                                .where(CategoryTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getFoodMeasureTableDao().deleteInTx(
                        daoSession.getFoodMeasureTableDao().queryBuilder()
                                .where(FoodMeasureTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getFoodTableDao().deleteInTx(
                        daoSession.getFoodTableDao().queryBuilder()
                                .where(FoodTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getFoodUsdaTableDao().deleteInTx(
                        daoSession.getFoodUsdaTableDao().queryBuilder()
                                .where(FoodUsdaTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getLabelTableDao().deleteInTx(
                        daoSession.getLabelTableDao().queryBuilder()
                                .where(LabelTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getPhotoTableDao().deleteInTx(
                        daoSession.getPhotoTableDao().queryBuilder()
                                .where(PhotoTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getRecipeFoodTableDao().deleteInTx(
                        daoSession.getRecipeFoodTableDao().queryBuilder()
                                .where(RecipeFoodTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getRecipeLabelTableDao().deleteInTx(
                        daoSession.getRecipeLabelTableDao().queryBuilder()
                                .where(RecipeLabelTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getRecipeMethodTableDao().deleteInTx(
                        daoSession.getRecipeMethodTableDao().queryBuilder()
                                .where(RecipeMethodTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getRecipePhotoTableDao().deleteInTx(
                        daoSession.getRecipePhotoTableDao().queryBuilder()
                                .where(RecipePhotoTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
                daoSession.getRecipeTableDao().deleteInTx(
                        daoSession.getRecipeTableDao().queryBuilder()
                                .where(RecipeTableDao.Properties.ChangedAt.eq(changeDate))
                                .list()
                );
            }
        });
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

    private void setSettingsValue(String key, String val) {
        SettingsTable settingsTable = new SettingsTable();
        settingsTable.setKey(key);
        settingsTable.setVal(val);
        obtainChangeDate(settingsTable);
        daoSession.getSettingsTableDao().insertOrReplaceInTx(settingsTable);
    }

    private void obtainRestriction(QueryBuilder<?> builder) {
        if (restrict) {
            builder.offset(offset).limit(limit);
            restrict = false;
        }
    }

    private void obtainChangeDate(List<? extends Table> tables) {
        if (tables == null) return;
        for (Table table : tables) {
            obtainChangeDate(table);
        }
    }

    private void obtainChangeDate(Table table) {
        if (table == null) return;
        table.setChangedAt(changeDate);
    }
}
