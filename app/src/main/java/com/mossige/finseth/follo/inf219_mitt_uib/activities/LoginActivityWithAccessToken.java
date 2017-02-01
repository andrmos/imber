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

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.network.PrivateConstants;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivityWithAccessToken extends AppCompatActivity {

    private EditText inputField;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // Hard coded access token for development
        if(!PrivateConstants.ACCESS_TOKEN.isEmpty()) {
            storeToken(PrivateConstants.ACCESS_TOKEN);
            changeActivity();

        // Access token is stored in shared preference
        } else if (sharedPreferences.contains("access_token")) {
            changeActivity();

        // Prompt for new access token
        } else {
            setContentView(R.layout.activity_login_activity_with_access_token);

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
    }

    private void validateAccessToken(final String token){
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getApplicationContext());
        Call<List<Course>> call = client.getCourses();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, retrofit2.Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    storeToken(token);
                    changeActivity();
                } else {
                    showSnackbar();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                showSnackbar();
            }
        });
    }

    private void changeActivity() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

    private void storeToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("access_token", token);
        editor.apply();
    }

    private void showSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.login_access_token), R.string.error_invalid_access_token, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

}
