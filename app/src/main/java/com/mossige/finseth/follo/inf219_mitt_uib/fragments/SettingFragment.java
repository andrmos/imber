package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener{


    private static final String TAG = "SettingFragment";

    private SharedPreferences sharedPreferences;
    private ArrayList<CheckBoxPreference> checkBoxes;
    private ArrayList<Course> courses;
    private ArrayList<Course> favoriteCourses;
    private boolean loaded;

    private SmoothProgressBar smoothProgressBar;

    MainActivityListener mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courses = new ArrayList<>();
        favoriteCourses = new ArrayList<>();
        loaded = false;
        checkBoxes = new ArrayList<>();

        setHasOptionsMenu(true);

        //Inflates view
        addPreferencesFromResource(R.xml.preferences);

        requestCourses();


    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //empty
    }

    private void initSettings(){
        if(loaded){
            PreferenceScreen screen = getPreferenceScreen();

            for(int i = 0; i < courses.size(); i++){
                CheckBoxPreference cbp = new CheckBoxPreference(this.getActivity());
                cbp.setTitle(courses.get(i).getCourseCode());
                cbp.setKey("" + courses.get(i).getId());
                cbp.setViewId(i);
                cbp.setOnPreferenceClickListener(this);
                for(int j = 0; j < favoriteCourses.size(); j++){
                    if(courses.get(i).getId() == favoriteCourses.get(j).getId()){
                        cbp.setChecked(true);
                        Log.i(TAG, "checked");
                    }
                }
                checkBoxes.add(cbp);
                screen.addPreference(cbp);
            }
        }else{
            Log.i(TAG, "Not loaded");
        }
    }

    private void saveFavoriteCourse(final String id) {

        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());
        Call<Course> call = client.addFavortiteCourse(id);
        call.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, retrofit2.Response<Course> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Lagret");
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.i(TAG, "Fail");
            }
        });
    }


    private void requestCourses() {
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());
        Call<List<Course>> call = client.getCourses();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, retrofit2.Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    courses.clear();
                    courses.addAll(response.body());

                    requestFavoriteCourse();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                if (smoothProgressBar != null) smoothProgressBar.setVisibility(View.GONE);
                mCallback.showSnackbar(getString(R.string.error_course_list), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestCourses();
                    }
                });
            }
        });
    }

    private void requestFavoriteCourse() {
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());
        Call<List<Course>> call = client.getFavoriteCourses();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, retrofit2.Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    favoriteCourses.clear();
                    favoriteCourses.addAll(response.body());

                    loaded = true;

                    Log.i(TAG, "size is " + favoriteCourses.size());

                    initSettings();

                    if (smoothProgressBar != null) smoothProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                if (smoothProgressBar != null) smoothProgressBar.setVisibility(View.GONE);
                mCallback.showSnackbar(getString(R.string.error_course_list), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestFavoriteCourse();
                    }
                });
            }
        });
    }

    private void removeFavoriteCourse(final String id) {
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());
        Call<Course> call = client.removeFavortiteCourse(id);
        call.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, retrofit2.Response<Course> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Removed");
                }
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                Log.i(TAG, "Fail");
            }
        });
    }

    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        int id = 0;

        for(int i = 0; i < courses.size(); i++){
            if(key.equals(checkBoxes.get(i).getKey())){
                id = i;
                break;
            }
        }

        if(checkBoxes.get(id).isChecked()){
            saveFavoriteCourse(key);
            return true;
        }else{
            removeFavoriteCourse(key);
            return true;
        }
    }
}

