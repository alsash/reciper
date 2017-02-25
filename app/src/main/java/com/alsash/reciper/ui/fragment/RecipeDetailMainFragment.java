package com.alsash.reciper.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alsash.reciper.R;
import com.alsash.reciper.data.RecipeManager;
import com.alsash.reciper.data.model.Recipe;
import com.alsash.reciper.ui.contract.KeyContract;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class RecipeDetailMainFragment extends Fragment {

    private Recipe recipe;

    private PieChart pieChart;

    public static RecipeDetailMainFragment newInstance(long recipeId) {
        Bundle args = new Bundle();
        args.putLong(KeyContract.KEY_RECIPE_ID, recipeId);
        RecipeDetailMainFragment fragment = new RecipeDetailMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long recipeId = getArguments().getLong(KeyContract.KEY_RECIPE_ID, -1);
        recipe = RecipeManager.getInstance().getRecipe(recipeId);
        assert recipe != null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recipe_detail_main, container, false);
        setupChart(layout);
        return layout;
    }

    private void setupChart(View layout) {
        pieChart = (PieChart) layout.findViewById(R.id.recipe_detail_main_pie_chart);

        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
        values.add(new PieEntry(50, "protein"));
        values.add(new PieEntry(20, "fat"));
        values.add(new PieEntry(100, "carbohydrate"));

        PieDataSet dataSet = new PieDataSet(values, "nutrition");
        dataSet.setSliceSpace(4f);
        dataSet.setSelectionShift(12f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
        pieChart.invalidate();
    }
}
