package com.mossige.finseth.follo.inf219_mitt_uib.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Calendar class holding CalendarEvents for a user.
 *
 * Created by PatrickFinseth on 07.03.16.
 */
public class MyCalendar {

    private static final String TAG = "Calendar";

    private ArrayList<CalendarEvent> calendar;

    public MyCalendar(ArrayList<CalendarEvent> mcalendar){
        this.calendar = mcalendar;
    }

    public void test() {
        Log.i(TAG, "test: size " + calendar.size());
    }

    public ArrayList<CalendarEvent> getEventsForDate(int day, int month, int year){
        ArrayList<CalendarEvent> retCalendar = new ArrayList<>();

        Date d = this.calendar.get(0).getStartDate();
        Log.i(TAG, "getEventsForDate: calendar event: month:" + d.getMonth() + " year:" + d.getYear());
//        TODO Calendar.setTime(date)
        for(CalendarEvent c : this.calendar){
            //Checks if a date is equal to given date in calendar
            if(c.getStartDate().getDate() == day && c.getStartDate().getMonth() == month && c.getStartDate().getYear() == year){
                retCalendar.add(c);
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
        Log.i(TAG, "getEvent: size " + calendar.size());
        return calendar.get(position);
    }

    public ArrayList<CalendarEvent> getAllEvents(){
        return calendar;
    }
}
