package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.content.Context;
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
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCal;
import com.mossige.finseth.follo.inf219_mitt_uib.network.DownloadFileTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    private static final String TAG = "AgendaFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private MyCal calendar;
    private ArrayList<CalendarEvent> agendas;

    public AgendaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);

        Log.i(TAG, "Created agenda fragment");

        URL url = null;

        try {
            url = new URL(getArguments().get("calendarURL").toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        agendas = new ArrayList<>();
        initRecycleView(rootView);

        DownloadFileTask dft = new DownloadFileTask(mAdapter);

        try {

            calendar = new MyCal(dft.execute(url).get());

            agendas.clear();
            Calendar cal = Calendar.getInstance();
            // Add calendar events for current date (adapter notified in DownloadFileTask)
            agendas.addAll(calendar.getEventsForDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR)));

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }

        return rootView;
    }

    /**
     * Update calendar events for a specified date.
     * @param date The date of the calendar events to be shown
     */
    public void updateAgendaCards(Date date) {
        agendas.clear();
        agendas.addAll(calendar.getEventsForDate(date.getDate(), date.getMonth()+1, date.getYear()+1900));
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

        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Log.i(TAG, "Clicked item " + position);
            }
        });
    }

}
