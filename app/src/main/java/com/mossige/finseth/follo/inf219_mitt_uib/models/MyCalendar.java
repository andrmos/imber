package com.mossige.finseth.follo.inf219_mitt_uib.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import hirondelle.date4j.DateTime;

/**
/**
 * Calendar class holding CalendarEvents for a user.
 *
 * Created by PatrickFinseth on 07.03.16.
 */
public class MyCalendar {

    private static final String TAG = "Calendar";

    private ArrayList<CalendarEvent> calendar;

    /** If year-month at key: 'year-month' is loaded. Zero indexed month.*/
    private HashMap<String, Boolean> loaded;

    public MyCalendar() {
        this.calendar = new ArrayList<>();
        this.loaded = new HashMap<>();
    }

    public ArrayList<CalendarEvent> getEventsForDate(DateTime date){
        ArrayList<CalendarEvent> retCalendar = new ArrayList<>();

        for(CalendarEvent c : this.calendar){
            if (date.isSameDayAs(c.getStartDate())) {
                retCalendar.add(c);
            }
        }

        return retCalendar;
    }

    /**
     *
     * @param year
     * @param month zero indexed
     * @param value
     */
    public void setLoaded(int year, int month, boolean value) {
        loaded.put(year + "-" + month, value);
    }

    /**
     *
     * @param year
     * @param month zero indexed
     * @return
     */
    public boolean loaded(int year, int month) {
        Boolean ret = loaded.get(year + "-" + month);
        if (ret != null) {
            return ret;
        } else {
            // No key = not loaded
            return false;
        }
    }

    public void addEvents(ArrayList<CalendarEvent> events) {
        // TODO Ensure no duplicates are added...
        // Maybe change data structure?
        // Using HashSet? It does not add duplicates.
        // Would handle everything automatically.
        calendar.addAll(events);
    }

    public CalendarEvent getEvent(int position){
        return calendar.get(position);
    }

    public ArrayList<CalendarEvent> getAllEvents(){
        return calendar;
    }

    public void removeEvents(int year, int month) {

        // TODO remove events in this year and month
        // Needed to ensure no duplicates are added

        for (int i = 0; i < calendar.size(); i++) {
            DateTime dateTime = calendar.get(i).getStartDate();
            if (dateTime.getYear() == year && dateTime.getMonth() == month) {
                calendar.remove(i);
            }
        }
    }
}
