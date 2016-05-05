package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.content.Context;
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
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ShowSnackbar;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCalendar;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

    private Course course;

    /* If data is loaded */
    private boolean[] loaded;

    ShowSnackbar.ShowToastListener mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (ShowSnackbar.ShowToastListener) context;
        }catch(ClassCastException e){
            Log.i(TAG, "onAttach: " + e.toString());
        }
    }

    public CourseFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        announcements = new ArrayList<>();
        agendas = new ArrayList<>();
        loaded = new boolean[2];

        // Get arguments from course list
        int course_id = getArguments().getInt("id");
        String calendar_url = getArguments().getString("calendar_url");
        String name = getArguments().getString("name");
        String course_code = getArguments().getString("course_code");

        course = new Course(course_id,name,calendar_url,course_code);

        requestAnnouncements("" + course.getId());
        requestAgendas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        // Set toolbar title to course name
        String course_name = getArguments().getString("name");
        getActivity().setTitle(course_name);

        initRecycleView(rootView);

        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        // Hide content before everything is loaded
        //mainList.setVisibility(View.GONE);

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
                    ArrayList<String> announcementSender =  new ArrayList<>();
                    ArrayList<String> announcementDates =  new ArrayList<>();
                    ArrayList<String> announcementIds = new ArrayList<>();

                    //Make list with all announcement titles
                    for(Announcement a : announcements){
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

                    transaction.commit();
                } else {
                    Log.i(TAG, "onItemClicked: clicking item at position " + position + " is not supported.");
                }
            }
        });
    }



    private void requestAnnouncements(final String course_id) {

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
                mCallback.showSnackbar("Error requesting announcements", new View.OnClickListener() {
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

    private void showToast() {
        Toast.makeText(getContext(), R.string.error_course_info, Toast.LENGTH_SHORT).show();
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
        Log.i(TAG, "requestAgendas");

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

        Log.i(TAG, "onResponse: url:" + UrlEndpoints.getCalendarEventsUrl(ids, exclude, type, start_date, end_date,per_page,1));
        JsonArrayRequest calendarEventsRequest = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCalendarEventsUrl(ids, exclude, type, start_date, end_date, per_page,1), (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {

                    ArrayList<CalendarEvent> tmpList = JSONParser.parseAllCalendarEvents(response);
                    agendas.clear();
                    for(int i = 0; i < tmpList.size() && i < 3; i++){
                        agendas.add(tmpList.get(i));
                    }

                    Log.i(TAG, "onResponse: " + agendas.size());

                } catch (JSONException e) {
                    Log.i(TAG, "exception: " + e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
            }
        });

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(calendarEventsRequest);
    }
}
