package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.android.volley.toolbox.StringRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCalendar;
import com.mossige.finseth.follo.inf219_mitt_uib.network.downloads.DownloadCourseCalendarTask;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
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

    private MyCalendar calendar;

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

        initOnClickListener();
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                AnnouncementFragment announcementFragment = new AnnouncementFragment();
                transaction.replace(R.id.content_frame, announcementFragment);

                ArrayList<String> announcementTitles = new ArrayList<String>();
                ArrayList<String> announcementMessages = new ArrayList<String>();
                ArrayList<String> announcementSender =  new ArrayList<String>();
                ArrayList<String> announcementDates =  new ArrayList<String>();

                //Make list with all announcement titles
                for(Announcement a : announcements){
                    announcementTitles.add(a.getTitle());
                    announcementMessages.add(android.text.Html.fromHtml(a.getMessage()).toString());
                    announcementSender.add(a.getUserName());
                    announcementDates.add(a.getPostedAt());
                }

                Bundle args = new Bundle();
                args.putStringArrayList("announcementTitles", announcementTitles);
                args.putStringArrayList("announcementMessages", announcementMessages);
                args.putStringArrayList("announcementSender", announcementSender);
                args.putStringArrayList("announcementDates", announcementDates);
                announcementFragment.setArguments(args);

                transaction.commit();
            }
        });
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

    private void requestAgendas(String string_url) {
        URL url = null;
        try {
            url = new URL(string_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        DownloadCourseCalendarTask dft = new DownloadCourseCalendarTask(this);
        dft.execute(url);
    }

    public void setAgendas(ArrayList<CalendarEvent> calendarEvents) {
        calendar = new MyCalendar(calendarEvents);
        agendas.clear();
        agendas.addAll(calendar.getAllEvents());
        mAdapter.notifyDataSetChanged();
    }

}
