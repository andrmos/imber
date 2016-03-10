package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseMenuRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {

    private static final String TAG = "CourseFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Announcement> announcements;
    private ArrayList<CalendarEvent> agendas;
    private ArrayList<String> grades;

    public CourseFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        String course_id = getArguments().getString("id");
        //requestAnnouncements(course_id);

        // Set toolbar title to course name
        //String course_name = getArguments().getString("name");
        //getActivity().setTitle(course_name);

        announcements = new ArrayList<>();
        agendas = new ArrayList<>();
        grades = new ArrayList<>();

        // Add dummy announcements, agendas and grades
        announcements.add(new Announcement("id", "Tittel 1", "Brukernavn", "kl 2", "Dette er en fin melding!", false));
        announcements.add(new Announcement("id", "Tittel 2", "Brukernavn", "kl 2", "Dette er en fin melding!", false));
        announcements.add(new Announcement("id", "Tittel 3", "Brukernavn", "kl 2", "Dette er en fin melding!", false));

        agendas.add(new CalendarEvent("Forelesning 1", "Beksrivelse", null, null));
        agendas.add(new CalendarEvent("Forelesning 2", "Beksrivelse", null, null));
        agendas.add(new CalendarEvent("Gruppe", "Beksrivelse", null, null));

        grades.add("A");
        grades.add("B");
        grades.add("E");

        initRecycleView(rootView);

        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.course_list);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new CourseRecyclerViewAdapter(announcements, agendas, grades);
        mainList.setAdapter(mAdapter);
    }

    private void requestAnnouncements(String course_id) {

        JsonArrayRequest announcementsRequest = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCourseAnnouncementsUrl(course_id), (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, "Got response");

                try {

                    Log.i(TAG, "" + response.get(0).toString());
                    //String text = response.getJSONObject(0).getString("message");
                    //Log.i(TAG, "message " + text);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());

            }
        });

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(announcementsRequest);
    }

}
