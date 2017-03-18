package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", "");

        if (!accessToken.isEmpty()) {
            // Point of Splash Activity is to laod data necessary for app to work.
            // TODO Also load courses in splash activity?

            MittUibClient client = ServiceGenerator.createService(MittUibClient.class, accessToken);
            Call<User> call = client.getProfile();
            final Intent intent = new Intent(getBaseContext(), MainActivity.class);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        String json = new Gson().toJson(response.body());
                        // Launch MainActivity
                        intent.putExtra("profile", json);
                        changeActivity(intent);
                    } else {
                        changeActivity(intent);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    changeActivity(intent);
                }
            });

        } else {
            // Launch login
            Intent intent = new Intent(this, LoginActivity.class);
            changeActivity(intent);
        }
    }

    private void changeActivity(Intent intent) {
        startActivity(intent);
        finish();
    }
}
