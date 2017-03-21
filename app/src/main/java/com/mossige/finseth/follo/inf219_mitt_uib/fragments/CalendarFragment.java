package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.AgendaRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCalendar;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.PaginationUtils;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.retrofit.ServiceGenerator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;
import hugo.weaving.DebugLog;
import retrofit2.Call;
import retrofit2.Callback;


public class CalendarFragment extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    private static final String TAG = "CalendarFragment";

    private MyCalendar calendar;
    private MainActivityListener mCallback;
    private MittUibClient mittUibClient;
    private ArrayList<String> globalContextCodes;
    private String nextPage;
    private String nextPageAssignment;
    private AgendaRecyclerViewAdapter mAdapter;
    private ArrayList<CalendarEvent> agendas;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (MainActivityListener) context;
        } catch (ClassCastException e) {
            //Do nothing
        }

        mittUibClient = ServiceGenerator.createService(MittUibClient.class, context);
    }

    public CalendarFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nextPage = "";
        nextPageAssignment = "";
        calendar = new MyCalendar();
        agendas = new ArrayList<>();
        initContextCodes();
    }

    /**
     * Creates the list of context codes with the right format.
     * The list is used by the API to know what courses to get events for.
     */
    private void initContextCodes() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey("ids")) {
                ArrayList<Integer> courseIds = arguments.getIntegerArrayList("ids");

                // Add all course ids for use in context_codes in url
                globalContextCodes = new ArrayList<>();
                for (int i = 0; i < courseIds.size(); i++) {
                    globalContextCodes.add("course_" + courseIds.get(i));
                }
            }

            if (arguments.containsKey("user_id")) {
                globalContextCodes.add("user_" + arguments.getInt("user_id"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        getActivity().setTitle(R.string.calendar_title);
        initRecycleView(rootView);

        MaterialCalendarView calendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);

        // Get events for current month
        this.onMonthChanged(calendarView, CalendarDay.today());
        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        RecyclerView mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Create the LayoutManager that holds all the views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new AgendaRecyclerViewAdapter(agendas);
        mainList.setAdapter(mAdapter);
    }

    @DebugLog
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        DateTime dateSelected = DateTime.forDateOnly(date.getYear(), date.getMonth() + 1, date.getDay());
        setAgendas(calendar.getEventsForDate(dateSelected));
    }

    @DebugLog
    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        DateTime curMonth = DateTime.forDateOnly(date.getYear(), date.getMonth() + 1, 1);
        // TODO Refactor
        int requiredSplits = (int) Math.ceil((double) globalContextCodes.size() / 10);
        int j = 0;
        for (int i = 0; i < requiredSplits; i++) {
            // The context codes for a call
            ArrayList<String> contextCodeSingleCall = new ArrayList<>();
            for (; contextCodeSingleCall.size() <= 9 && j < globalContextCodes.size(); j++) {
                contextCodeSingleCall.add(globalContextCodes.get(j));
            }

            if (!calendar.loaded(curMonth.getYear(), curMonth.getMonth(), "event")) {
                getEvents(curMonth.getYear(), curMonth.getMonth(), contextCodeSingleCall);
            }

            if (!calendar.loaded(curMonth.getYear(), curMonth.getMonth(), "assignment")) {
                getAssignments(curMonth.getYear(), curMonth.getMonth(), contextCodeSingleCall);
            }

        }
    }

    private void setAgendas(ArrayList<CalendarEvent> events) {
        agendas.clear();
        mAdapter.notifyDataSetChanged();
        agendas.addAll(events);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get all calendar events for a month, and add them to the 'events' field.
     *
     * @param year  The year to get the calendar events.
     * @param month The month to get the calendar events.
     */
    private void getEvents(final int year, final int month, final ArrayList<String> contextCodes) {
        final String type = "event";

        //What to exlude
        ArrayList<String> excludes = new ArrayList<>();
        excludes.add("child_events");

        final DateTime startDate = DateTime.forDateOnly(year, month, 1);
        final DateTime endDate = startDate.getEndOfMonth();
        String startDateString = startDate.toString();
        String endDateString = endDate.format("YYYY-MM-DD");

        int perPage = 50;

        // TODO Fix pagination: several calls are made, and will use each others nextPage field...
        // TODO Should use a arraylist?
        Call<List<CalendarEvent>> call;
        boolean firstPage = nextPage.isEmpty();
        if (firstPage) {
            call = mittUibClient.getEvents(startDateString, endDateString, contextCodes, excludes, type, perPage);
        } else {
            call = mittUibClient.getEventsPaginate(nextPage);
        }

        call.enqueue(new Callback<List<CalendarEvent>>() {

            @Override
            public void onResponse(Call<List<CalendarEvent>> call, retrofit2.Response<List<CalendarEvent>> response) {
                if (response.isSuccessful()) {

                    ArrayList<CalendarEvent> events = new ArrayList<>();
                    events.addAll(response.body());

                    calendar.addEvents(events);

                    // If returned maximum amount of events, get events for next page

                    nextPage = PaginationUtils.getNextPageUrl(response.headers());
                    if (!nextPage.isEmpty()) {
                        getEvents(year, month, contextCodes);
                    }

                    calendar.setLoaded(year, month, type, true);

                    // If in current month, set todays agendas
                    DateTime today = DateTime.today(TimeZone.getTimeZone("Europe/Oslo"));
                    if (today.gteq(startDate) && today.lteq(endDate)) {
                        setAgendas(calendar.getEventsForDate(today));
                    }

                } else {

                    if (isAdded()) {
                        mCallback.showSnackbar(getString(R.string.error_requesting_calendar), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getEvents(year, month, contextCodes);
                            }
                        });
                        calendar.setLoaded(year, month, type, false);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CalendarEvent>> call, Throwable t) {
                if (isAdded()) {
                    mCallback.showSnackbar(getString(R.string.error_requesting_calendar), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getEvents(year, month, contextCodes);
                        }
                    });
                    calendar.setLoaded(year, month, type, false);
                }
            }
        });
    }

    private void getAssignments(final int year, final int month, final ArrayList<String> contextCodes) {

        final String type = "assignment";

        final DateTime startDate = DateTime.forDateOnly(year, month, 1);
        final DateTime endDate = startDate.getEndOfMonth();
        String startDateString = startDate.toString();
        String endDateString = endDate.format("YYYY-MM-DD");

        int perPage = 50;

        Call<List<CalendarEvent>> call;
        boolean firstPage = nextPageAssignment.isEmpty();
        if (firstPage) {
            call = mittUibClient.getEvents(startDateString, endDateString, contextCodes, null, type, perPage);
        } else {
            call = mittUibClient.getEventsPaginate(nextPageAssignment);
        }

        call.enqueue(new Callback<List<CalendarEvent>>() {
            @Override
            public void onResponse(Call<List<CalendarEvent>> call, retrofit2.Response<List<CalendarEvent>> response) {
                if (response.isSuccessful()) {
                    ArrayList<CalendarEvent> events = new ArrayList<>();
                    events.addAll(response.body());

                    nextPageAssignment = PaginationUtils.getNextPageUrl(response.headers());
                    if (!nextPage.isEmpty()) {
                        getEvents(year, month, contextCodes);
                    }

                    calendar.addEvents(events);
                    calendar.setLoaded(year, month, type, true);

                } else {
                    if (isAdded()) {
                        mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCallback.showSnackbar(getString(R.string.error_requesting_calendar), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        getAssignments(year, month, contextCodes);
                                    }
                                });
                                calendar.setLoaded(year, month, type, false);
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
                            mCallback.showSnackbar(getString(R.string.error_requesting_calendar), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getAssignments(year, month, contextCodes);
                                }
                            });
                            calendar.setLoaded(year, month, type, false);
                        }
                    });
                }
            }
        });
    }
}