package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.activities.LoginActivityWithAccessToken;


/**
 * Created by PatrickFinseth on 14.03.16.
 */
public class SettingFragment extends PreferenceFragmentCompat{


    private static final String TAG = "SettingFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflates view
        addPreferencesFromResource(R.xml.preferences);

        getActivity().setTitle("Innstillinger");

        Preference favoritePreference = findPreference("favorite");
        favoritePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SettingFavoriteCourseFragment settingFavoriteCourseFragment = new SettingFavoriteCourseFragment();
                transaction.replace(R.id.content_frame, settingFavoriteCourseFragment);

                transaction.addToBackStack(null);

                transaction.commit();
                return true;
            }
        });

        Preference accessTokenPreference = findPreference("update_access_token");
        accessTokenPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), LoginActivityWithAccessToken.class);
                intent.putExtra("update_access_token", true);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //empty
    }

}
