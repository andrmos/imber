package com.mossige.finseth.follo.inf219_mitt_uib.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.File;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.ServiceGenerator;

import java.util.List;

import hugo.weaving.DebugLog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity showing the Imber logo while the profile and its favorite courses are loaded.
 */
public class SplashActivity extends AppCompatActivity {

    private boolean profileLoaded;
    private boolean coursesLoaded;
    private MittUibClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", "");

        // TODO Add negation
        if (accessToken.isEmpty()) {
            client = ServiceGenerator.createService(MittUibClient.class, accessToken);
            final Intent intent = new Intent(getBaseContext(), MainActivity.class);
            getProfileAndCourses(intent);

        } else {
            // Launch login
            Intent intent = new Intent(this, LoginActivityWithAccessToken.class);
            startActivity(intent);
            finish();
        }
    }

    private void getProfileAndCourses(Intent intent) {
        if (!profileLoaded) {
            getProfile(intent);
        }

        if (!coursesLoaded) {
            getCourses(intent);
        }
    }

    private void getProfile(final Intent intent) {
        Call<User> profileCall = client.getProfile();
        profileCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    profileLoaded = true;
                    String json = new Gson().toJson(response.body());
                    intent.putExtra("profile", json);
                    changeActivity(intent);
                } else {
                    showSnackbar(getString(R.string.error_profile), intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showSnackbar(getString(R.string.error_profile), intent);
            }
        });
    }

    private void getCourses(final Intent intent) {
        Call<List<Course>> coursesCall = client.getFavoriteCourses();
        coursesCall.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    coursesLoaded = true;
                    String coursesJson = new Gson().toJson(response.body());
                    intent.putExtra("courses", coursesJson);
                    changeActivity(intent);
                } else {
                    showSnackbar(getString(R.string.request_courses_error), intent);
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                showSnackbar(getString(R.string.request_courses_error), intent);
            }
        });
    }

    // Can only be called by one instance at the time
    private synchronized void changeActivity(Intent intent) {
        if (profileLoaded && coursesLoaded) {
            startActivity(intent);
            finish();
        }
    }

    private void showSnackbar(String toastMessage, final Intent intent) {
        setContentView(R.layout.activity_splash);
        Snackbar snackbar =  Snackbar.make(findViewById(R.id.splash_container), toastMessage, Snackbar.LENGTH_INDEFINITE);
        int duration = 4000;
        snackbar = snackbar.setDuration(duration);

        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        snackbar.setAction(getString(R.string.snackback_action_text), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfileAndCourses(intent);
            }
        });

        if (!snackbar.isShown()) {
            snackbar.show();
        }
    }

    // TODO Use dialog or snackbar?
    private void showPromt(String toastMessage, final Intent intent) {
        Log.i("heybab", "showPromt: heyhbab");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(toastMessage).setTitle("Feil");

        builder.setNeutralButton(R.string.file_size_promt_positive_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.i("Heybaby", "onClick prompt");
                getProfileAndCourses(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
