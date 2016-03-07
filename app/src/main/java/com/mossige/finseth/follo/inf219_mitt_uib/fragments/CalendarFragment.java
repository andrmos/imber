package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.AgendaRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseMenuRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    public CalendarFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        CaldroidFragment caldroidFragment = initCalendarFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.calendar_container, caldroidFragment);
        
        AgendaFragment agendaFragment = new AgendaFragment();
        ft.replace(R.id.agenda_container, agendaFragment);

        ft.commit();
        return rootView;
    }

    /**
     * Initializes a CaldroidFragment and bundles it with arguments defining the calendar.
     *
     * @return the Caldroid Fragment
     */
    private CaldroidFragment initCalendarFragment() {
        CaldroidFragment caldroidFragment = new CaldroidFragment();

        Calendar cal = Calendar.getInstance();

        // Bundle Caldroid arguments to initialize calendar
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1); // January = 0
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, true);
        args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
        args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
        caldroidFragment.setArguments(args);

        // Set listeners
        caldroidFragment.setCaldroidListener(initCaldroidListener());


        return caldroidFragment;
    }

    private CaldroidListener initCaldroidListener() {

        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Log.i(TAG, "Clicked date " + date.getDate() + "/" + (date.getMonth() + 1));
                // TODO notify agenda fragment for showing agenda
            }
        };

        return listener;
    }
}