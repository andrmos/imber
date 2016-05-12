package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {

    private static final String TAG = "CourseFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<Announcement> announcements;
    private ArrayList<CalendarEvent> agendas;

    private SmoothProgressBar progressbar;

    private Course course;

    /* If data is loaded */
    private boolean[] loaded;

    MainActivityListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (MainActivityListener) context;
        }catch(ClassCastException e){
            //Do nothing
        }
    }

    public CourseFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        announcements = new ArrayList<>();
        agendas = new ArrayList<>();
        loaded = new boolean[3];

        // Get arguments from course list
        int course_id = getArguments().getInt("id");

        if(getArguments() != null) {
            String calendar_url = "";
            String name = "";
            String course_code = "";

            if (getArguments().containsKey("calendar_url")) {
                calendar_url = getArguments().getString("calendar_url");
            }

            if(getArguments().containsKey("name")) {
                name = getArguments().getString("name");
            }

            if(getArguments().containsKey("course_code")) {
                course_code = getArguments().getString("course_code");
            }

            course = new Course(course_id,name,calendar_url,course_code);
        }

        requestAnnouncements("" + course.getId());
        requestAgendas();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        // Set toolbar title to course name
        String course_name = getArguments().getString("name");
        getActivity().setTitle(course_name);

        progressbar =  (SmoothProgressBar) rootView.findViewById(R.id.progressbar);
        initRecycleView(rootView);

        // Hide progress bar if data is already loaded
        if (isLoaded()) {
            progressbar.setVisibility(View.GONE);
            mainList.setVisibility(View.VISIBLE);
        } else {
            progressbar.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
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

                    //Temporary lists for bundling announcement object
                    ArrayList<String> announcementTitles = new ArrayList<>();
                    ArrayList<String> announcementMessages = new ArrayList<>();
                    ArrayList<String> announcementSender = new ArrayList<>();
                    ArrayList<String> announcementDates = new ArrayList<>();
                    ArrayList<String> announcementIds = new ArrayList<>();

                    //Make list with all announcement titles
                    for (Announcement a : announcements) {
                        announcementIds.add(a.getId());
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
                    args.putStringArrayList("announcementIds", announcementIds);
                    args.putString("course_code", course.getCourseCode());
                    announcementFragment.setArguments(args);

                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Log.i(TAG, "onItemClicked: clicking item at position " + position + " is not supported.");
                }

                if(position == 1){
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    mCallback.initCalendar();
                }
            }
        });
    }



    private void requestAnnouncements(final String course_id) {

        final JsonArrayRequest announcementsRequest = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCourseAnnouncementsUrl(course_id), (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                announcements.clear();
                announcements.addAll(JSONParser.parseAllAnnouncements(response));

                loaded[0] = true;


                if (isLoaded()) {
                    mainList.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressbar.setVisibility(View.GONE);
                mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestAnnouncements(course_id);
                    }
                });
            }
        });

        announcementsRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(announcementsRequest);
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

    private void requestAgendas() {

        //Course ids for context_codes in url
        ArrayList<String> ids = new ArrayList<>();
        ids.add("course_" + course.getId());

        //What to exclude
        ArrayList<String> exclude = new ArrayList<>();

        //Type - event/assignment
        String type = "event";

        //Only 3 agendas for one course
        String per_page = "3";

        //Get todays date in right format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String start_date = df.format(cal.getTime());
        String end_date = df.format(cal.getTime());

        JsonArrayRequest calendarEventsRequest = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCalendarEventsUrl(ids, exclude, type, start_date, end_date, per_page,1), (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                agendas.clear();
                agendas.addAll(JSONParser.parseAllCalendarEvents(response));

                loaded[1] = true;

                requestAssignments();

                if (isLoaded()) {
                    mainList.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
                if (progressbar != null) progressbar.setVisibility(View.GONE);
                mCallback.showSnackbar(getString(R.string.error_requesting_agendas), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestAgendas();
                    }
                });
            }
        });

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(calendarEventsRequest);
    }

    private void requestAssignments() {
        Log.i(TAG, "requestAssignements");

        //Course ids for context_codes in url
        ArrayList<String> ids = new ArrayList<>();
        ids.add("course_" + course.getId());

        //What to exclude
        ArrayList<String> exclude = new ArrayList<>();

        //Type - event/assignment
        String type = "assignment";

        //Only 3 agendas for one course
        String per_page = "3";

        //Get todays date in right format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String start_date = df.format(cal.getTime());
        String end_date = df.format(cal.getTime());

        Log.i(TAG, "onResponse: url:" + UrlEndpoints.getCalendarEventsUrl(ids, exclude, type, start_date, end_date,per_page,1));
        JsonArrayRequest calendarEventsRequest = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCalendarEventsUrl(ids, exclude, type, start_date, end_date, per_page,1), (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                agendas.addAll(JSONParser.parseAllCalendarEvents(response));

                loaded[2] = true;

                if (isLoaded()) {
                    mainList.setVisibility(View.VISIBLE);
                    progressbar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
                if (progressbar != null) progressbar.setVisibility(View.GONE);
                mCallback.showSnackbar(getString(R.string.error_requesting_agendas), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestAgendas();
                    }
                });
            }
        });

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(calendarEventsRequest);
    }
}
