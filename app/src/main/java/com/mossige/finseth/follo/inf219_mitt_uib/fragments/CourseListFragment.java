package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseListRecyclerViewAdapter;
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

    private ArrayList<Course> courses;
    private ProgressBar spinner;

    /* If data is loaded */
    private boolean loaded;

    public CourseListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loaded = false;
        courses = new ArrayList<>();

        //Check settings before intitializing courses
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        // Filter useless courses on institute level
        boolean filterInstituteCourses = sharedPreferences.getBoolean("checkbox_preference", true);

        requestCourses(filterInstituteCourses);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        getActivity().setTitle(R.string.course_list_title);

        spinner =  (ProgressBar) rootView.findViewById(R.id.progressBar);
        initRecycleView(rootView);

        // Hide progress bar if data is already loaded
        if (loaded) {
            spinner.setVisibility(View.GONE);
        } else {
            spinner.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mainList.setVisibility(View.VISIBLE);

        // Create the LayoutManager that holds all the views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new CourseListRecyclerViewAdapter(courses);
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

                transaction.addToBackStack(null);

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

    private void requestCourses(final boolean filterInstituteCourses) {

        final JsonArrayRequest coursesReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCoursesListUrl(), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    courses.clear();
                    courses.addAll(JSONParser.parseAllCourses(response, filterInstituteCourses));

                    loaded = true;
                    if (mAdapter != null) mAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                }

                if (spinner != null) spinner.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (spinner != null) spinner.setVisibility(View.GONE);
                showToast();
            }

        });

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(coursesReq);
    }

    private void showToast() {
        Toast.makeText(getContext(), R.string.error_course_list, Toast.LENGTH_SHORT).show();
    }
}
