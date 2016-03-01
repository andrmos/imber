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
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment {

    private static final String TAG = "AgendaFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<CalendarEvent> agendas;

    public AgendaFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);

        Log.i(TAG, "Created agenda fragment");

        agendas = new ArrayList<>();
        agendas.add(new CalendarEvent("Agenda1", "summary 1", new Date(1000, 10, 10, 10, 10), new Date(2000, 20, 20, 20, 20)));
        agendas.add(new CalendarEvent("Agenda2", "summary 2",new Date(1000, 10, 10, 10, 10), new Date(2000, 20, 20, 20, 20)));
        agendas.add(new CalendarEvent("Agenda3", "summary 3",new Date(1000, 10, 10, 10, 10), new Date(2000, 20, 20, 20, 20)));
        agendas.add(new CalendarEvent("Agenda4", "summary 4",new Date(1000, 10, 10, 10, 10), new Date(2000, 20, 20, 20, 20)));
        agendas.add(new CalendarEvent("Agenda5", "summary 5",new Date(1000, 10, 10, 10, 10), new Date(2000, 20, 20, 20, 20)));
        agendas.add(new CalendarEvent("Agenda6", "summary 6",new Date(1000, 10, 10, 10, 10), new Date(2000, 20, 20, 20, 20)));
        agendas.add(new CalendarEvent("Agenda7", "summary 7",new Date(1000, 10, 10, 10, 10), new Date(2000, 20, 20, 20, 20)));

        initRecycleView(rootView);

        return rootView;
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

}
