package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.SendMessage;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;


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

        Preference preference = findPreference("favorite");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SettingFavoriteCourseFragment settingFavoriteCourseFragment = new SettingFavoriteCourseFragment();
                transaction.replace(R.id.content_frame, settingFavoriteCourseFragment);
                transaction.commit();
                return true;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //empty
    }

}
