package com.alsash.reciper.ui.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.vector.VectorHelper;

public class RecipeBottomDialog extends BottomSheetDialogFragment
        implements Toolbar.OnMenuItemClickListener {

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

        VectorHelper vectorHelper = new VectorHelper(getContext());

        // Toolbar
        Toolbar bottomToolbar = (Toolbar) contentView.findViewById(R.id.recipe_bottom_toolbar);
        bottomToolbar.inflateMenu(R.menu.recipe_bottom);
        vectorHelper.tintMenuItems(bottomToolbar.getMenu(),
                R.color.gray_600,
                R.color.black_a054);
        bottomToolbar.setOnMenuItemClickListener(this);
        bottomToolbar.setLogo(R.mipmap.ic_launcher);
        bottomToolbar.setTitle(" Recipe name");

        // Button label
        Button button = (Button) contentView.findViewById(R.id.recipe_label_button);
        vectorHelper.setCompoundDrawables(button, R.color.accent, R.drawable.ic_labeled); // to Left

        dialog.setContentView(contentView);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
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
