package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by patrickfinseth on 10.01.2017.
 */
public class SettingFavoriteCourseFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener{

    private static final String TAG = "SettingFavoriteFragment";

    private ArrayList<CheckBoxPreference> checkBoxes;
    private ArrayList<Course> courses;
    private ArrayList<Course> favoriteCourses;
    private boolean loaded;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courses = new ArrayList<>();
        favoriteCourses = new ArrayList<>();
        loaded = false;
        checkBoxes = new ArrayList<>();

        getActivity().setTitle("Velg favorittfag");

        //Inflates view
        addPreferencesFromResource(R.xml.preference_favorite);

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
                    Log.i(TAG, "Saved");
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
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
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
