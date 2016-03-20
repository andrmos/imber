package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private ArrayList<CalendarEvent> calendarEvents;
    private CaldroidFragment caldroidFragment;
    private Date tmpDate;
    private Map<Date,ColorDrawable> backgrounds;

    OnDateClickListener mCallback;

    public interface OnDateClickListener {
        void onDateSelected(Date date);
    }

    public CalendarFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnDateClickListener) context;
        } catch (ClassCastException e) {
            Log.i(TAG, "Class cast exception");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        tmpDate = null;

        getActivity().setTitle(R.string.calendar_title);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        caldroidFragment = initCalendarFragment();
        ft.replace(R.id.calendar_container, caldroidFragment);

        AgendaFragment agendaFragment = new AgendaFragment();
        agendaFragment.setArguments(getArguments()); // Bundle with url from main activity
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
        ColorDrawable bg = new ColorDrawable(0xFFFF6666);
        Map<Date,Drawable> dates = new HashMap<>();
        backgrounds = new HashMap<>();

        //Set background for dates that contains at least one agenda
        for(String s : getArguments().getStringArrayList("Dates")){
            int year = Integer.parseInt(s.substring(0,3));
            int month = Integer.parseInt(s.substring(3,5))-1;
            int day = Integer.parseInt(s.substring(5,7));
            dates.put(new Date(year,month,day),bg);
            backgrounds.put(new Date(year,month,day),bg);
        }

        caldroidFragment.setBackgroundDrawableForDates(dates);

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
                // Callback to main activity to notify agenda fragment to update its calendar events
                mCallback.onDateSelected(date);

                //Remove higlighting for last selected day
                if(tmpDate != null) {

                    //If last selected day was highlighted by agendas reverse background color
                    if(backgrounds.get(tmpDate) != null){
                        caldroidFragment.setBackgroundDrawableForDate(backgrounds.get(tmpDate),tmpDate);
                    }else {
                        caldroidFragment.clearBackgroundDrawableForDate(tmpDate);
                    }
                }

                //Set color to selected date
                ColorDrawable bg = new ColorDrawable(0xFF0000);
                caldroidFragment.setBackgroundDrawableForDate(bg, date);
                caldroidFragment.refreshView();
                tmpDate = date;

            }
        };
        return listener;
    }


}