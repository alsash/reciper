package com.alsash.reciper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alsash.reciper.R;
import com.alsash.reciper.ui.adapter.RecipeListAdapter;

public class RecipeListActivity extends DrawerFrameActivity implements RecipeListAdapter.OnRecipeInteraction {

    private FloatingActionButton mainFab;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    public void click() {

    }

    @Override
    public void expand() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View bottomSheetLayout = findViewById(R.id.bottom_sheet_recipe);

        final FloatingActionButton bottomFab = (FloatingActionButton)
                bottomSheetLayout.findViewById(R.id.bottom_sheet_recipe_fab);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                    mainFab.animate().scaleX(0).scaleY(0).setDuration(300).start();
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mainFab.animate().scaleX(1).scaleY(1).setDuration(300).start();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //  bottomFab.animate().scaleX(slideOffset).scaleY(slideOffset).setDuration(0).start();
            }
        });

        bottomFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    @Override
    protected Fragment getFrameFragment(@Nullable Intent activityIntent) {
        return RecipeListFragment.newInstance();
    }

    @Override
    protected void setupFab(FloatingActionButton fab) {
        this.mainFab = fab;
    }

}
