package com.alsash.reciper.model;

import com.alsash.reciper.model.database.LabelDb;
import com.alsash.reciper.presenter.entity.Label;

public class LabelManager extends BaseEntityManager<Label> {
    private static LabelManager instance = new LabelManager();

    private LabelManager() {
        super(20);
        // Obtain all relations
        RecipeManager.getInstance().list();
    }

    public static LabelManager getInstance() {
        return instance;
    }

    @Override
    protected Label newEntity() {
        int id = entities.size();
        LabelDb label = new LabelDb();
        label.setId(id);
        label.setName("Label # " + id);
        return label;
    }
}
