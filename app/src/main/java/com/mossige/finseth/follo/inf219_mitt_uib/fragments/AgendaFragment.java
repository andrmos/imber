package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.AgendaRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCalendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    private static final String TAG = "AgendaFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;

    private MyCalendar calendar;
    private ArrayList<CalendarEvent> agendas;

    public AgendaFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        agendas = new ArrayList<>();

        ArrayList<CalendarEvent> tmpAgendas = new ArrayList<>();

        Bundle arguments = getArguments();
        if (arguments != null) {
            ArrayList<String> start_date = arguments.getStringArrayList("start_date");
            ArrayList<String> end_date = arguments.getStringArrayList("end_date");
            ArrayList<String> name = arguments.getStringArrayList("name");
            ArrayList<String> location = arguments.getStringArrayList("location");

            for (int i = 0; i < start_date.size(); i++) {

                DateTime start = new DateTime(start_date.get(i));
                DateTime end = new DateTime(end_date.get(i));

                TimeZone timeZone = TimeZone.getTimeZone("Europe/Oslo");

                tmpAgendas.add(new CalendarEvent(name.get(i), start, end, location.get(i), timeZone));
            }

        } else {
            Log.i(TAG, "onCreate: arguments is null");
        }

        calendar = new MyCalendar(tmpAgendas);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        initRecycleView(rootView);
        setTodaysAgenda();
        return rootView;
    }

    public void updateAgendaCards(DateTime dateTime) {
        agendas.clear();
        agendas.addAll(calendar.getEventsForDate(dateTime.getDay(), dateTime.getMonth(), dateTime.getYear()));
        mAdapter.notifyDataSetChanged();
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Create the LayoutManager that holds all the views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new AgendaRecyclerViewAdapter(agendas);
        mainList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setTodaysAgenda() {
        //Add calendar events for current date
        Calendar cal = Calendar.getInstance();
        agendas.clear();
        // TODO Change to DateTime.now(TimeZone)
        agendas.addAll(calendar.getEventsForDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR)));
        mAdapter.notifyDataSetChanged();
    }
}
