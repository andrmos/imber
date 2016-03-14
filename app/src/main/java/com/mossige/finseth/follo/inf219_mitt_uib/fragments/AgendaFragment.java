package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.AgendaRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCalendar;
import com.mossige.finseth.follo.inf219_mitt_uib.network.downloads.DownloadCalendarTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    private static final String TAG = "AgendaFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MyCalendar calendar;
    private ArrayList<CalendarEvent> agendas;

    public AgendaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        agendas = new ArrayList<>();

        URL url = null;
        try {
            url = new URL(getArguments().get("calendarURL").toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        DownloadCalendarTask dft = new DownloadCalendarTask(this);
        dft.execute(url);

        initRecycleView(rootView);

        return rootView;
    }

    /**
     * Update calendar events for a specified date.
     * @param date The date of the calendar events to be shown
     */
    public void updateAgendaCards(Date date) {
        agendas.clear();
        agendas.addAll(calendar.getEventsForDate(date.getDate(), date.getMonth()+1, date.getYear()));
        mAdapter.notifyDataSetChanged();
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.agenda_list);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new AgendaRecyclerViewAdapter(agendas);
        mainList.setAdapter(mAdapter);
    }

    public void setAgendas(ArrayList<CalendarEvent> calendarEvents) {
        calendar = new MyCalendar(calendarEvents);
        // TODO Use updateAgendas() instead
        setTodaysAgenda();
    }

    private void setTodaysAgenda() {
        agendas.clear();
        Calendar cal = Calendar.getInstance();
//         Add calendar events for current date
        agendas.addAll(calendar.getEventsForDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR)-1900));
        mAdapter.notifyDataSetChanged();
    }
}
