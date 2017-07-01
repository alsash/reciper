package com.alsash.reciper.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.alsash.reciper.R;
import com.alsash.reciper.app.ReciperApp;
import com.alsash.reciper.logic.NavigationLogic;

import javax.inject.Inject;

/**
 * Simple activity that holds one fragment
 */
public class FragmentSingleActivity extends AppCompatActivity {

    @Inject
    NavigationLogic navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((ReciperApp) getApplicationContext())
                .getUiStartComponent()
                .inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_single);
        setSupportActionBar((Toolbar) findViewById(R.id.activity_fragment_single_toolbar));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        startFragment(false);
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
        startFragment(true);
    }

    private void startFragment(boolean replace) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_fragment_single_container);

        if (fragment == null || replace) {
            fragment = navigator.getFragmentSingle(getIntent());
            fm.beginTransaction()
                    .replace(R.id.activity_fragment_single_container, fragment)
                    .commit();
        }
    }
}
