package com.alsash.reciper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.alsash.reciper.R;
import com.alsash.reciper.app.AppNavigator;
import com.alsash.reciper.app.ReciperApp;

import javax.inject.Inject;

public class SingleFragmentActivity extends AppCompatActivity {

    @Inject
    AppNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((ReciperApp) getApplicationContext())
                .getAppComponent()
                .inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_frame);
        setSupportActionBar((Toolbar) findViewById(R.id.activity_base_frame_toolbar));
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        startFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        startFragment();
    }

    private void startFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_base_frame_container);

        if (fragment == null) {
            fragment = navigator.getFragmentSingle(getIntent());
            fm.beginTransaction()
                    .add(R.id.activity_base_frame_container, fragment)
                    .commit();
        }
    }
}
