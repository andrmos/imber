package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputField;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        if (getIntent().getBooleanExtra("update_access_token", false)) {
            promptAccessToken();

        } else if (sharedPreferences.contains("access_token")) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            changeActivity(intent);

        } else {
            promptAccessToken();
        }
    }

    private void promptAccessToken() {
        setContentView(R.layout.activity_login);

        inputField = (EditText) findViewById(R.id.token);
        Button login = (Button) findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accessToken = inputField.getText().toString();
                validateAccessToken(accessToken);
            }
        });
    }

    private void validateAccessToken(final String token){
        final Intent intent = new Intent(getApplication(), MainActivity.class);

        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, token);
        Call<User> call = client.getProfile();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    storeToken(token);
                    String profileJson = new Gson().toJson(response.body());
                    intent.putExtra("profile", profileJson);
                    changeActivity(intent);
                } else {
                    showSnackbar();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showSnackbar();
            }
        });
    }

    private void changeActivity(Intent intent) {
        startActivity(intent);
    }

    private void storeToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("access_token", token);
        editor.apply();
    }

    private void showSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.login_access_token), R.string.error_invalid_access_token, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
