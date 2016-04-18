package com.mossige.finseth.follo.inf219_mitt_uib.models;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * Calendar class holding CalendarEvents for a user.
 *
 * Created by PatrickFinseth on 07.03.16.
 */
public class MyCalendar {

    private static final String TAG = "Calendar";

    private ArrayList<CalendarEvent> calendar;

    public MyCalendar(ArrayList<CalendarEvent> calendar){
        this.calendar = calendar;
        Log.i(TAG, "MyCalendar: size: " + this.calendar.size());

        Log.i(TAG, "MyCalendar: hash " + calendar.hashCode());
    }

    public ArrayList<CalendarEvent> getEventsForDate(int day, int month, int year){
        ArrayList<CalendarEvent> retCalendar = new ArrayList<>();


        for(CalendarEvent c : calendar){

            Log.i(TAG, "getEventsForDate: c: " + c);
            //Checks if a date is equal to given date in calendar
            if(c.getStartDate().getDate() == day && c.getStartDate().getMonth() == month && c.getStartDate().getYear() == year){
                retCalendar.add(c);
                Log.i(TAG, "getEventsForDate: add");
            }
        }

        return retCalendar;
    }

    public ArrayList<CalendarEvent> getThreeNextEvents(Date curDate) {
        ArrayList<CalendarEvent> ret = new ArrayList<>();

        for (CalendarEvent e : calendar) {
            if(e.getStartDate().before(curDate)) {
                continue;
            }

            ret.add(e);

            if(ret.size() == 3) {
                break;
            }
        }

        return ret;
    }

    public CalendarEvent getEvent(int position){
        return calendar.get(position);
    }

    public ArrayList<CalendarEvent> getAllEvents(){
        return calendar;
    }
}
