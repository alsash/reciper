package com.alsash.reciper.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.alsash.reciper.R;

public abstract class DrawerSpinnerActivity extends DrawerBaseActivity {

    protected abstract Fragment getSpinnerFragment(int position, @Nullable Intent activityIntent);

    protected abstract SpinnerArrayRes getSpinnerArrayRes();

    @Override
    protected int getDrawerLayout() {
        return R.layout.activity_drawer_spinner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setDisplayShowTitleEnabled(false);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new SpinnerAdapter(actionBar.getThemedContext(), getSpinnerArrayRes()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.spinner_container, getSpinnerFragment(position, getIntent()))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public static class SpinnerArrayRes {
        final int namesArrayRes;
        final int iconsArrayRes;

        public SpinnerArrayRes(@ArrayRes int namesArrayRes, @Nullable Integer iconsArrayRes) {
            this.namesArrayRes = namesArrayRes;
            this.iconsArrayRes = iconsArrayRes == null ? 0 : iconsArrayRes;
        }
    }

    public static class SpinnerAdapter extends ArrayAdapter<CharSequence> implements ThemedSpinnerAdapter {

        private final ThemedSpinnerAdapter.Helper dropDownHelper;
        private final SpinnerArrayRes arrayResources;

        public SpinnerAdapter(Context context, SpinnerArrayRes arrayResources) {
            super(context, android.R.layout.simple_list_item_1,
                    context.getResources().getTextArray(arrayResources.namesArrayRes));
            this.dropDownHelper = new ThemedSpinnerAdapter.Helper(context);
            this.arrayResources = arrayResources;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View dropDownView;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = dropDownHelper.getDropDownViewInflater();
                dropDownView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                dropDownView = convertView;
            }

            TextView textView = (TextView) dropDownView.findViewById(android.R.id.text1);
            textView.setText(getItem(position));
            if (arrayResources.iconsArrayRes != 0) {
                TypedArray iconsArray = textView.getContext().getResources()
                        .obtainTypedArray(arrayResources.iconsArrayRes);
                Drawable icon = iconsArray.getDrawable(position);
                if (icon != null) {
                    // icon.mutate();
                    // DrawableCompat.setTint(icon, R.color.icon_dark);
                    textView.setCompoundDrawablePadding(14);
                    textView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                }
                iconsArray.recycle();
            }

            return dropDownView;
        }

        @Override
        public Resources.Theme getDropDownViewTheme() {
            return dropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Resources.Theme theme) {
            dropDownHelper.setDropDownViewTheme(theme);
        }
    }

}
