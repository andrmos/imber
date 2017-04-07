package no.mofifo.imber.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import no.mofifo.imber.R;
import no.mofifo.imber.models.Profile;
import no.mofifo.imber.retrofit.MittUibClient;
import no.mofifo.imber.retrofit.ServiceGenerator;

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
            Call<Profile> call = client.getProfile();
            final Intent intent = new Intent(getBaseContext(), MainActivity.class);
            call.enqueue(new Callback<Profile>() {
                @Override
                public void onResponse(Call<Profile> call, Response<Profile> response) {
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
                public void onFailure(Call<Profile> call, Throwable t) {
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
