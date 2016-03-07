package com.mossige.finseth.follo.inf219_mitt_uib.models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Calendar class holding CalendarEvents for a user.
 *
 * Created by PatrickFinseth on 07.03.16.
 */
public class MyCal {

    private final String TAG = "MyCal";

    private ArrayList<CalendarEvent> calendar;

    public MyCal(ArrayList<CalendarEvent> calendar){
        this.calendar = calendar;
    }

    public ArrayList<CalendarEvent> getEventsForDate(int day, int month, int year){
        ArrayList<CalendarEvent> retCalendar = new ArrayList<>();

        for(CalendarEvent c : calendar){

            //Checks if a date is equal to given date in calendar
            if(c.getStartDate().getDate() == day && c.getStartDate().getMonth() == month && c.getStartDate().getYear() == year){
                retCalendar.add(c);
            }
        }

        return retCalendar;
    }

    public CalendarEvent getEvent(int position){
        return calendar.get(position);
    }

    public ArrayList<CalendarEvent> getCalendar(){
        return calendar;
    }
}
