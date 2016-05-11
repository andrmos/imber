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

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.AgendaRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaFragment extends Fragment implements CalendarFragment.OnDateClickListener {

    private static final String TAG = "AgendaFragment";

    private AgendaRecyclerViewAdapter mAdapter;
    private ArrayList<CalendarEvent> agendas;

    public AgendaFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        agendas = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        initRecycleView(rootView);
        return rootView;
    }

    @Override
    public void setAgendas(ArrayList<CalendarEvent> events) {
        agendas.clear();
        agendas.addAll(events);
        mAdapter.notifyDataSetChanged();
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
}
