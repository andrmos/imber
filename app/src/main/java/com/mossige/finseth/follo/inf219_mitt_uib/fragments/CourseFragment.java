package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


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
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCal;
import com.mossige.finseth.follo.inf219_mitt_uib.network.downloads.DownloadCourseCalFileTask;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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

    private MyCal calendar;

    public CourseFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        String course_id = getArguments().getString("id");
        //requestAnnouncements(course_id);

        String calendar_url = getArguments().getString("calendarurl");

        // Set toolbar title to course name
        //String course_name = getArguments().getString("name");
        //getActivity().setTitle(course_name);

        announcements = new ArrayList<>();
        agendas = new ArrayList<>();
        grades = new ArrayList<>();

        // Add dummy announcements, agendas and grades

        grades.add("A - Compulsory 1 INF115");
        grades.add("B - Compulsory 2 INF115");
        grades.add("E - Compulsory 1 MAT101");

        initRecycleView(rootView);

        requestAnnouncements(course_id);
        requestAgendas(calendar_url);

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
                    announcements.clear();
                    announcements.addAll(JSONParser.parseAllAnouncements(response));
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.i(TAG, "JSONException requesting announcements");
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

    private void requestAgendas(String url) {

        Log.i(TAG, "url: " + url);

        DownloadCourseCalFileTask dft = new DownloadCourseCalFileTask(mAdapter);
        try {
            calendar = new MyCal(dft.execute(new URL(url)).get());
            agendas.clear();
            agendas.addAll(calendar.getCalendar());



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
