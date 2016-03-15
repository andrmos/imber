package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseMenuRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseListFragment extends Fragment {

    private static final String TAG = "CourseListFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View rootView;
    private ArrayList<Course> courses;
    private ProgressBar spinner;

    private boolean courseFilter;

    public CourseListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_course_list, container, false);
        courses = new ArrayList<>();

        spinner =  (ProgressBar) rootView.findViewById(R.id.progressBar);

        //Checks settings before intitializing courses
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("checkbox_preference", true)){
            courseFilter = true;
        }else{
            courseFilter = false;
        }

        Log.i(TAG, "" + courseFilter);

        initRecycleView();
        requestCourses();

        return rootView;
    }

    private void initRecycleView() {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.mainList);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());

        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new CourseMenuRecyclerViewAdapter(courses);
        mainList.setAdapter(mAdapter);

        initOnClickListener();
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CourseFragment courseFragment = new CourseFragment();
                transaction.replace(R.id.content_frame, courseFragment);

                TextView course_id_tv = (TextView) v.findViewById(R.id.course_id);
                TextView course_name_tv = (TextView) v.findViewById(R.id.course_title);
                TextView course_url_tv = (TextView) v.findViewById(R.id.calendar_url);

                Bundle args = new Bundle();
                args.putString("id", "" + course_id_tv.getText());
                args.putString("name", "" + course_name_tv.getText());
                args.putString("calendarurl", "" + course_url_tv.getText());
                courseFragment.setArguments(args);

                transaction.commit();
            }
        });
    }

    private void requestCourses() {
        spinner.setVisibility(View.VISIBLE);

        JsonArrayRequest coursesReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCoursesListUrl(), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    courses.clear();
                    ArrayList<Course> temp = JSONParser.parseAllCourses(response,courseFilter);
                    for (Course c: temp) {
                        courses.add(c);
                    }

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                }

                spinner.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error response");
                spinner.setVisibility(View.GONE);
            }
        });

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(coursesReq);
    }
}