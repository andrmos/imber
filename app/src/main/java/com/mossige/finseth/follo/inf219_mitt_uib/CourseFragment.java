package com.mossige.finseth.follo.inf219_mitt_uib;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseFragment extends Fragment {

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View rootView;
    private ArrayList<String> titles;

    public CourseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_course, container, false);

        titles = new ArrayList<>();
        titles.add("INF112");
        titles.add("INF234");
        titles.add("INF102");

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
        mAdapter = new MainListAdapter(titles);
        mainList.setAdapter(mAdapter);
    }

}