package com.alsash.reciper;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(StartActivity.this, "that's all!", Toast.LENGTH_LONG).show();
                StartActivity.this.finish();
            }
        }, 1000);
    }
}
