package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.ServiceGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;

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
    private MittUibClient mittUibClient;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (MainActivityListener) context;
        }catch(ClassCastException e){
            //Do nothing
        }

        mittUibClient = ServiceGenerator.createService(MittUibClient.class, context);
    }

    public CourseFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        announcements = new ArrayList<>();
        agendas = new ArrayList<>();
        loaded = new boolean[3];

        if(getArguments() != null) {
            if (getArguments().containsKey("course")) {
                String json = getArguments().getString("course");
                course = new Gson().fromJson(json, Course.class);
            }
        }

        requestAnnouncements(course.getId());
        requestAgendas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);
        // Set toolbar title to course name
        getActivity().setTitle(course.getTrimmedName());

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

    private void loadFileBrowserFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        FileBrowserFragment fileBrowserFragment = new FileBrowserFragment();
        transaction.replace(R.id.content_frame, fileBrowserFragment);
        transaction.addToBackStack(null);

        Bundle args = new Bundle();
        String json = new Gson().toJson(course);
        args.putString("course", json);
        fileBrowserFragment.setArguments(args);

        transaction.addToBackStack(null);
        transaction.commit();
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

                    Bundle args = new Bundle();
                    args.putString("courseCode", course.getCourseCode());
                    args.putInt("courseId", course.getId());
                    announcementFragment.setArguments(args);

                    transaction.addToBackStack(null);
                    transaction.commit();

                } else if (position == 1) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    mCallback.initCalendar();

                } else if (position == 2) {
                    // Clicked file browser card
                    loadFileBrowserFragment();

                }
            }
        });
    }

    private void requestAnnouncements(final int course_id) {

        Call<List<Announcement>> call = mittUibClient.getAnnouncements(course_id);
        call.enqueue(new Callback<List<Announcement>>() {
            @Override
            public void onResponse(Call<List<Announcement>> call, retrofit2.Response<List<Announcement>> response) {

                if (response.isSuccessful()) {
                    announcements.addAll(response.body());
                    loaded[0] = true;

                    if (isLoaded()) {
                        mainList.setVisibility(View.VISIBLE);
                        progressbar.progressiveStop();
                    }

                } else {
                    if (isAdded()) {
                        progressbar.progressiveStop();
                        mCallback.showSnackbar(getString(R.string.error_requesting_announcements), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestAnnouncements(course_id);
                            }
                        });
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Announcement>> call, Throwable t) {
                if (isAdded()) {
                    progressbar.progressiveStop();
                    mCallback.showSnackbar(getString(R.string.error_requesting_announcements), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestAnnouncements(course_id);
                        }
                    });
                }
            }
        });
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
        ArrayList<String> contextCodes = new ArrayList<>();
        contextCodes.add("course_" + course.getId());

        //Type - event/assignment
        String type = "event";

        //Only 3 agendas for one course
        int perPage = 3;

        //Get todays date in right format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String startDate = df.format(cal.getTime());
        String endDate = df.format(cal.getTime());

        Call<List<CalendarEvent>> call = mittUibClient.getCalendarEvents(startDate, endDate, contextCodes, null, type, perPage, null);
        call.enqueue(new Callback<List<CalendarEvent>>() {
            @Override
            public void onResponse(Call<List<CalendarEvent>> call, retrofit2.Response<List<CalendarEvent>> response) {
                if (response.isSuccessful()) {


                    agendas.clear();
                    agendas.addAll(response.body());

                    loaded[1] = true;

                    requestAssignments();

                    if (isLoaded()) {
                        mainList.setVisibility(View.VISIBLE);
                        progressbar.progressiveStop();
                    }

                } else {
                    if (isAdded()) {
                        mCallback.showSnackbar(getString(R.string.error_requesting_agendas), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestAgendas();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
                if (isAdded()) {
                    mCallback.showSnackbar(getString(R.string.error_requesting_agendas), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestAgendas();
                        }
                    });
                }
            }
        });

    }

    private void requestAssignments() {
        //Course ids for context_codes in url
        ArrayList<String> contextCodes = new ArrayList<>();
        contextCodes.add("course_" + course.getId());

        //Type - event/assignment
        String type = "assignment";

        //Only 3 agendas for one course
        int perPage = 3;

        //Get todays date in right format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String startDate = df.format(cal.getTime());
        String endDate = df.format(cal.getTime());

        Call<List<CalendarEvent>> call = mittUibClient.getCalendarEvents(startDate, endDate, contextCodes, null, type, perPage, null);
        call.enqueue(new Callback<List<CalendarEvent>>() {
            @Override
            public void onResponse(Call<List<CalendarEvent>> call, retrofit2.Response<List<CalendarEvent>> response) {
                if (response.isSuccessful()) {

                    agendas.addAll(response.body());

                    loaded[2] = true;

                    if (isLoaded()) {
                        mainList.setVisibility(View.VISIBLE);
                        progressbar.progressiveStop();
                    }
                } else {
                    if (isAdded()) {
                        progressbar.progressiveStop();
                        mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestAssignments();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
                if (isAdded()) {
                    mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestAssignments();
                        }
                    });
                }
            }
        });
    }
}
