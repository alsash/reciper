package com.alsash.reciper.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.data.model.Recipe;

public class RecipeExpandDialog extends BottomSheetDialogFragment {

    public static RecipeExpandDialog newInstance(Recipe recipe) {

        Bundle args = new Bundle();

        RecipeExpandDialog fragment = new RecipeExpandDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_recipe_expand_dialog, null);
        dialog.setContentView(contentView);
        final FloatingActionButton collapseFab = (FloatingActionButton) contentView.findViewById(R.id.recipe_info_collapse_fab);
        collapseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
