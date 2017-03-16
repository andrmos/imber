package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO Merge SplashActivity and LoginActivity, and fetch profile while splash is showing.
        Intent intent = new Intent(this, LoginActivityWithAccessToken.class);
        startActivity(intent);
        finish();
    }
}
