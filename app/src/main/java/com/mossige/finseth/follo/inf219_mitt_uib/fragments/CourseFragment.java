package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCalendar;
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

    private ArrayList<Announcement> announcements;
    private ArrayList<CalendarEvent> agendas;

    private ProgressBar spinner;

    /* If data is loaded */
    private boolean[] loaded;

    public CourseFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        announcements = new ArrayList<>();
        agendas = new ArrayList<>();
        loaded = new boolean[2];

        // Get arguments from course list
        String course_id = getArguments().getString("id");
        String calendar_url = getArguments().getString("calendarurl");

        requestAnnouncements(course_id);
        requestAgendas(calendar_url);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        // Set toolbar title to course name
        String course_name = getArguments().getString("name");
        getActivity().setTitle(course_name);

        spinner =  (ProgressBar) rootView.findViewById(R.id.progressBar);
        initRecycleView(rootView);

        // Show recycler view and hide progress bar if data is already loaded
        if (isLoaded()) {
            mainList.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
        } else {
            mainList.setVisibility(View.GONE);
            spinner.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // Hide content before everything is loaded
        mainList.setVisibility(View.GONE);

        // Create the LayoutManager that holds all the views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new CourseRecyclerViewAdapter(announcements, agendas);
        mainList.setAdapter(mAdapter);

        initOnClickListener();
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (position == 0) {

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    AnnouncementFragment announcementFragment = new AnnouncementFragment();
                    transaction.replace(R.id.content_frame, announcementFragment);
                    transaction.addToBackStack(null);

                    ArrayList<String> announcementTitles = new ArrayList<>();
                    ArrayList<String> announcementMessages = new ArrayList<>();
                    ArrayList<String> announcementSender =  new ArrayList<>();
                    ArrayList<String> announcementDates =  new ArrayList<>();

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
                } else {
                    Log.i(TAG, "onItemClicked: clicking item at position " + position + " is not supported.");
                }
            }
        });
    }



    private void requestAnnouncements(String course_id) {

        final JsonArrayRequest announcementsRequest = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCourseAnnouncementsUrl(course_id), (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {
                    announcements.clear();
                    announcements.addAll(JSONParser.parseAllAnouncements(response));

                    loaded[0] = true;
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.i(TAG, "JSONException requesting announcements");
                    e.printStackTrace();
                }

                if (isLoaded()) {
                    mainList.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                showToast();
            }
        });

        announcementsRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(announcementsRequest);
    }

    private void showToast() {
        Toast.makeText(getContext(), R.string.error_course_info, Toast.LENGTH_SHORT).show();
    }

    private void requestAgendas(String string_url) {
        URL url = null;
        try {
            url = new URL(string_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

//        DownloadCourseCalendarTask dft = new DownloadCourseCalendarTask(this);
//        dft.execute(url);
    }

    public void setAgendas(ArrayList<CalendarEvent> calendarEvents) {
        MyCalendar calendar = new MyCalendar(calendarEvents);
        agendas.clear();
        agendas.addAll(calendar.getAllEvents());
        mAdapter.notifyDataSetChanged();

        loaded[1] = true;

        if (isLoaded()) {
            mainList.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);
        }
    }

    /**
     * @return Returns true if all data is loaded
     */
    private boolean isLoaded() {
        for (int i = 0; i < loaded.length; i++) {
            if (!loaded[i]) return false;
        }
        return true;
    }
}
