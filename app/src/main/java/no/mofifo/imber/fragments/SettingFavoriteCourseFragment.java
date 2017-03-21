package no.mofifo.imber.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.View;

import no.mofifo.imber.R;
import no.mofifo.imber.listeners.MainActivityListener;
import no.mofifo.imber.models.Course;
import no.mofifo.imber.retrofit.MittUibClient;
import no.mofifo.imber.retrofit.ServiceGenerator;

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

    private MainActivityListener mCallback;
    private MittUibClient mittUibClient;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (MainActivityListener) context;
        }catch(ClassCastException e){
            //Do nothing
        }

        mittUibClient = ServiceGenerator.createService(MittUibClient.class, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        courses = new ArrayList<>();
        checkBoxes = new ArrayList<>();

        //Inflates view
        addPreferencesFromResource(R.xml.preference_favorite);

        getActivity().setTitle(getString(R.string.fav_course_title));

        requestCourses();
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        //empty
    }

    private void initSettings(){
        PreferenceScreen screen = getPreferenceScreen();

        for(int i = 0; i < courses.size(); i++){

            //Add all courses as a checkbox
            CheckBoxPreference cbp = new CheckBoxPreference(getActivity());
            cbp.setPersistent(false);
            cbp.setTitle(courses.get(i).getCourseCode());
            cbp.setKey("" + courses.get(i).getId());
            cbp.setViewId(i);
            cbp.setOnPreferenceClickListener(this);

            if (courses.get(i).isFavorite()) {
                cbp.setChecked(true);
            }

            checkBoxes.add(cbp);
            screen.addPreference(cbp);
        }
    }

    private void saveFavoriteCourse(final String id) {
        Call<Course> call = mittUibClient.addFavortiteCourse(id);
        call.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, retrofit2.Response<Course> response) {
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                if (isAdded()) {
                    mCallback.showSnackbar(getString(R.string.error_saving_course), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveFavoriteCourse(id);
                        }
                    });
                }
            }
        });
    }


    private void requestCourses() {
        ArrayList<String> include = new ArrayList<>();
        include.add("favorites");

        // Only return active courses, ex. courses from this semester.
        String enrollmentState = "active";

        Call<List<Course>> call = mittUibClient.getCourses(include, enrollmentState);
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, retrofit2.Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    courses.clear();
                    courses.addAll(response.body());

                    initSettings();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                if (isAdded()) {
                    mCallback.showSnackbar(getString(R.string.error_course_list), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestCourses();
                        }
                    });
                }

            }
        });
    }

    private void removeFavoriteCourse(final String id) {
        Call<Course> call = mittUibClient.removeFavortiteCourse(id);
        call.enqueue(new Callback<Course>() {
            @Override
            public void onResponse(Call<Course> call, retrofit2.Response<Course> response) {
            }

            @Override
            public void onFailure(Call<Course> call, Throwable t) {
                if (isAdded()) {
                    mCallback.showSnackbar(getString(R.string.error_removing_course), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeFavoriteCourse(id);
                        }
                    });
                }
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
