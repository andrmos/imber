package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;

/**
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

    public ArrayList<CalendarEvent> getEventsForDate(int day, int month, int year){
        ArrayList<CalendarEvent> retCalendar = new ArrayList<>();

        for(CalendarEvent c : this.calendar){

            if (c.getStartDate().getDay() == day && c.getStartDate().getMonth() == month && c.getStartDate().getYear() == year) {
                retCalendar.add(c);
            }
        }

        return retCalendar;
    }

//    Not used
//    public ArrayList<CalendarEvent> getThreeNextEvents(Date curDate) {
//        ArrayList<CalendarEvent> ret = new ArrayList<>();
//
//        for (CalendarEvent e : calendar) {
//            if(e.getStartDate().before(curDate)) {
//                continue;
//            }
//
//            ret.add(e);
//
//            if(ret.size() == 3) {
//                break;
//            }
//        }
//
//        return ret;
//    }

    public CalendarEvent getEvent(int position){
        return calendar.get(position);
    }

    public ArrayList<CalendarEvent> getAllEvents(){
        return calendar;
    }
}
