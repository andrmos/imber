package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import java.util.TimeZone;

import hirondelle.date4j.DateTime;


public class CalendarFragment extends Fragment {

    private static final String TAG = "CalendarFragment";

    private ArrayList<CalendarEvent> calendarEvents;
    private CaldroidFragment caldroidFragment;

    /*
    Have to use java.Utils.Date, since Caldroid uses Map<Date, 'color'> to display background color for dates.
     */
    private Date tmpDate;
    private Map<Date,ColorDrawable> backgrounds;

    OnDateClickListener mCallback;

    public interface OnDateClickListener {
        void onDateSelected(DateTime dateTime);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendarEvents = new ArrayList<>();

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

                calendarEvents.add(new CalendarEvent(name.get(i), start, end, location.get(i), timeZone));
            }

        } else {
            Log.i(TAG, "onCreate: arguments is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
        getActivity().setTitle(R.string.calendar_title);

        tmpDate = null;

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
        for(CalendarEvent e : calendarEvents){

            /*
            Date conversion explained:
            In java.Utils.Date, months are zero indexed. So January = 0.
            In date library Date4j, months are one indexed. So January = 1.
            In java.Utils.Date, years are stored as year - 1900. So 2016 - 1900 = 116.
            Caldroid uses java.Utils.Date, while our project uses Date4j.
            So to convert Date4j to java.Utils.Date, 1900 is subtracted to year, while one is subtracted to month.
             */
            DateTime dt = e.getStartDate();
            Date startDate = new Date(dt.getYear() - 1900, dt.getMonth() - 1, dt.getDay());

            dates.put(startDate, bg);
            backgrounds.put(startDate, bg);
        }
        caldroidFragment.setBackgroundDrawableForDates(dates);

        Calendar cal = Calendar.getInstance();

        // Calendar is used to get current date
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

                /*
                Date conversion explained:
                In java.Utils.Date, months are zero indexed. So January = 0.
                In date library Date4j, months are one indexed. So January = 1.
                In java.Utils.Date, years are stored as year - 1900. So 2016 - 1900 = 116.
                Caldroid uses java.Utils.Date, while our project uses Date4j.
                So to convert java.Utils.Date to Date4j, 1900 is added to year, while one is added to month.

                Hours, minutes, seconds and nano seconds are ignored, since they are irrelevant when clicking on a date.
                 */
                DateTime dateTime = new DateTime(date.getYear() + 1900, date.getMonth() + 1, date.getDate(), 0, 0, 0, 0);

                // Callback to main activity to notify agenda fragment to update its calendar events
                mCallback.onDateSelected(dateTime);

                //Remove higlighting for selected day
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