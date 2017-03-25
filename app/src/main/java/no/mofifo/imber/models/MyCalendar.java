package no.mofifo.imber.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

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

    /** If year-month at key: 'year-month' is loaded.*/
    private HashMap<String, Boolean> loaded;

    public MyCalendar() {
        this.calendar = new HashSet<>();
        this.loaded = new HashMap<>();
    }

    public ArrayList<CalendarEvent> getEventsForDate(DateTime date){
        ArrayList<CalendarEvent> retCalendar = new ArrayList<>();

        for (CalendarEvent event : this.calendar) {
            if (event.getStartDate() != null && event.getStartDate().hasYearMonthDay() && date.isSameDayAs(event.getStartDate()) && !retCalendar.contains(event)) {
                retCalendar.add(event);
            }
        }
        Collections.sort(retCalendar);
        return retCalendar;
    }

    /**
     *
     * @param year
     * @param month
     * @param type "event" or "assignment"
     * @param value
     */
    public void setLoaded(int year, int month, String type, boolean value) {
        loaded.put(year + "-" + month + " " + type, value);
    }

    /**
     *
     * @param year
     * @param month
     * @param type "event" or "assignment"
     * @return
     */
    public boolean loaded(int year, int month, String type) {
        Boolean ret = loaded.get(year + "-" + month + " " + type);
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
