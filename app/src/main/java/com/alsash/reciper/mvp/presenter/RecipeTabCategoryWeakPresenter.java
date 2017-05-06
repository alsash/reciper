package com.alsash.reciper.mvp.presenter;

import android.content.Context;

import com.alsash.reciper.api.storage.local.database.table.DaoSession;
import com.alsash.reciper.mvp.model.entity.Category;
import com.alsash.reciper.mvp.model.entity.Recipe;
import com.alsash.reciper.mvp.view.RecipeTabCategoryView;
import com.alsash.reciper.ui.adapter.interaction.RecipeListInteraction;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

public class RecipeTabCategoryWeakPresenter extends BaseWeakPresenter<RecipeTabCategoryView>
        implements RecipeListInteraction {

    private final Context context;
    private final DaoSession session;
    private final List<Category> categories;

    private Subscription loadSubscription;
    private volatile boolean loading;

    public RecipeTabCategoryWeakPresenter(Context context, DaoSession session) {
        this.context = context;
        this.session = session;
        this.categories = new ArrayList<>();
    }

    @Override
    public void onExpand(Recipe recipe, int position) {
        if (getView() != null) getView().showRecipeDetails(recipe);
    }

    @Override
    public void onOpen(Recipe recipe, int position) {

        // Test begin
        Subject<Integer, Integer> subject = PublishSubject.create();

        // Test end
    }

    @Override
    protected void init() {
        setInitialized(true); // Used isLoading() instead for call to show() at setInForeground()
        if (getView() != null) getView().setCategories(categories); // Single list for attached view
        if (loadSubscription == null && !isLoading()) {
            loadSubscription = Completable
                    .fromAction(new Action0() {
                        @Override
                        public void call() {
                            setLoading(true);
                            loadCategories();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action0() {
                        @Override
                        public void call() {
                            setLoading(false);
                            if (isInForeground()) show();           // Call to show() in foreground
                        }
                    });
        }
    }

    @Override
    protected void show() {
        if (getView() == null) return;
        getView().showLoading(isLoading());
        if (!isLoading()) getView().showCategories();
    }

    @Override
    protected void clear() {
        loadSubscription.unsubscribe();
        loadSubscription = null;
    }

    public synchronized boolean isLoading() {
        return loading;
    }

    public synchronized void setLoading(boolean loading) {
        this.loading = loading;
    }

    private void loadCategories() {

    }

    /*
    private synchronized List<Category> getCategories() {
        if (categories.size() > 0) return categories;
        List<CategoryTable> dbCategories = session.getCategoryTableDao().loadAll();

        for (CategoryTable categoryDb : dbCategories) {
            List<Recipe> categoryRecipes = new ArrayList<>();
            categories.add(new CategoryMvpDb(categoryDb.getId(), categoryDb.getName(),
                    categoryRecipes));

            for (RecipeTable recipeDb : categoryDb.getRecipes()) {

                List<Label> recipeLabels = new ArrayList<>();
                categoryRecipes.add(new RecipeMvpDb(recipeDb.getId(), recipeDb.getName(),
                        categories.get(categories.size() - 1), recipeLabels));

                for (LabelTable labelDb : recipeDb.getLabels()) {
                    recipeLabels.add(new LabelMvpDb(labelDb.getId(), labelDb.getName(),
                            new ArrayList<Recipe>())); // Labels without inner recipes
                }
            }
        }
        return categories;
    }*/
}
