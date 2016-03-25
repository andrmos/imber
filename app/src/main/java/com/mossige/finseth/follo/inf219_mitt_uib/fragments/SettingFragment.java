package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import com.mossige.finseth.follo.inf219_mitt_uib.R;


/**
 * Created by PatrickFinseth on 14.03.16.
 */
public class SettingFragment extends PreferenceFragmentCompat {


    private static final String TAG = "SettingFragment";

    private SharedPreferences sharedPreferences;
    private CheckBoxPreference checkBoxPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Inflates view
        addPreferencesFromResource(R.xml.preferences);

        //Get shared preferences from file
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        //Get checkbox preference for course filter
        checkBoxPreference = (CheckBoxPreference) getPreferenceManager().findPreference("checkbox_preference");

        //Make click listener for box
        checkBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String key = preference.getKey();
                
                Boolean courseFilter = false;
                
                if(newValue instanceof Boolean){
                    courseFilter = (Boolean) newValue;

                    //Add changes to shared preferences
                    editor.putBoolean(key,courseFilter);
                    checkBoxPreference.setChecked(courseFilter);
                }

//                editor.commit();
                editor.apply();

                return courseFilter;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //empty
    }
}

