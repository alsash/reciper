package com.alsash.reciper.model;

import com.alsash.reciper.model.database.LabelDb;
import com.alsash.reciper.model.database.RecipeDb;
import com.alsash.reciper.presenter.entity.Category;
import com.alsash.reciper.presenter.entity.Label;
import com.alsash.reciper.presenter.entity.Recipe;

import java.util.List;

public class RecipeManager extends BaseEntityManager<Recipe> {

    private static RecipeManager instance = new RecipeManager();

    private RecipeManager() {
        super(30);
    }

    public static RecipeManager getInstance() {
        return instance;
    }

    @Override
    protected Recipe newEntity() {
        long id = entities.size();

        // Recipe without relations
        RecipeDb recipe = new RecipeDb();
        recipe.setId(id);
        recipe.setName("Recipe # " + id);

        // Recipe's Category
        CategoryManager categoryManager = CategoryManager.getInstance();
        long categoryId = id % categoryManager.list().size();
        Category category = categoryManager.search(categoryId);
        assert category != null;
        // set relations
        recipe.setCategory(category);
        category.getRecipes().add(recipe);

        // Recipe's Labels
        LabelManager labelManager = LabelManager.getInstance();
        int labelLimit = (int) (id % 4) + 1;
        int labelOffset = (int) id % (labelManager.list().size() - 4);
        List<Label> labels = labelManager.list(labelLimit, labelOffset);
        // set relations
        recipe.setLabels(labels);
        for (Label label : labels) {
            if (!(label instanceof LabelDb)) continue;
            if (!label.getRecipes().contains(recipe)) {
                List<Recipe> labelRecipes = label.getRecipes();
                labelRecipes.add(recipe);
                ((LabelDb) label).setRecipes(labelRecipes);
            }
        }
        return recipe;
    }
}
