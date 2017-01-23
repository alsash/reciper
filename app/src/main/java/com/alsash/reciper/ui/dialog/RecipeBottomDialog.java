package com.alsash.reciper.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;

public class RecipeBottomDialog extends BottomSheetDialogFragment implements Toolbar.OnMenuItemClickListener {

    public static RecipeBottomDialog newInstance(Recipe recipe) {

        Bundle args = new Bundle();

        RecipeBottomDialog fragment = new RecipeBottomDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_recipe_bottom_dialog, null);
        // Toolbar
        Toolbar bottomToolbar = (Toolbar) contentView.findViewById(R.id.recipe_bottom_toolbar);
        bottomToolbar.inflateMenu(R.menu.recipe_bottom);
        bottomToolbar.setOnMenuItemClickListener(this);
        bottomToolbar.setTitle("Recipe name");
        bottomToolbar.setSubtitle("Recipe title");

        dialog.setContentView(contentView);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recipe_bottom_bookmark:
                return true;
            case R.id.recipe_bottom_cart:
                return true;
            case R.id.recipe_bottom_collapse:
                dismiss();
                return true;
            default:
                return false;
        }
    }
}
