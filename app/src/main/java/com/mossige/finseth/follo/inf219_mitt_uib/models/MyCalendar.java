package com.mossige.finseth.follo.inf219_mitt_uib.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import hirondelle.date4j.DateTime;

/**
/**
 * Calendar class holding CalendarEvents for a user.
 *
 * Created by PatrickFinseth on 07.03.16.
 */
public class MyCalendar {

    private static final String TAG = "Calendar";

    private HashSet<CalendarEvent> calendar;

    /** If year-month at key: 'year-month' is loaded. Zero indexed month.*/
    private HashMap<String, Boolean> loaded;

    public MyCalendar() {
        this.calendar = new HashSet<>();
        this.loaded = new HashMap<>();
    }

    public ArrayList<CalendarEvent> getEventsForDate(DateTime date){
        ArrayList<CalendarEvent> retCalendar = new ArrayList<>();

        Iterator it = this.calendar.iterator();

        while(it.hasNext()){
            CalendarEvent event = (CalendarEvent) it.next();
            if (date.isSameDayAs(event.getStartDate())) {
                retCalendar.add(event);
            }
        }
        Collections.sort(retCalendar);
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
        this.calendar.addAll(events);
    }

}
