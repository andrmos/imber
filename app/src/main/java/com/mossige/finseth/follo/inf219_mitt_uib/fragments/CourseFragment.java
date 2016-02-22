package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.RecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
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
public class CourseFragment extends Fragment {

    private static final String TAG = "CourseFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View rootView;
    private ArrayList<Course> courses;

    public CourseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_course, container, false);

        courses = new ArrayList<>();

        requestCourses();

        initRecycleView(rootView);

        return rootView;
    }


    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.mainList);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new RecyclerViewAdapter(courses);
        mainList.setAdapter(mAdapter);
    }

    private void requestCourses() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        // Show progress bar
        progressDialog.setMessage("Laster fag...");
        progressDialog.show();

        JsonArrayRequest coursesReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCoursesListUrl(), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, "Got response");

                try {
                    courses.clear();

                    ArrayList<Course> temp = JSONParser.parseAllCourses(response);

                    for (Course c: temp) {
                        courses.add(c);
                    }

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                }

                progressDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error response");

                progressDialog.hide();
            }
        });

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(coursesReq);
    }
}