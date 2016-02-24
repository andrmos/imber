package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;

public class CalendarFragment extends Fragment {

    public CalendarFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        CaldroidFragment caldroidFragment = initCalendarFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.calendar_container, caldroidFragment);

        // TODO
        // - Create recycler view fragment with agendas
        // - replace R.id.agenda_container

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
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, true);

        caldroidFragment.setArguments(args);
        return caldroidFragment;
    }
}