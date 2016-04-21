package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.Date;

import hirondelle.date4j.DateTime;

/**
 * Created by PatrickFinseth on 23.02.16.
 */
public class CalendarEvent {

    private Date startDate;
    private Date endDate;
    private String name;
    private String location;

    private DateTime mStartDate;
    private DateTime mEndDate;

    public CalendarEvent(String name, String startDate, String endDate, String location){
        this.name = name;
        this.mStartDate = new DateTime(startDate);
        this.mEndDate = new DateTime(endDate);
        this.location = location;
    }

    public CalendarEvent(String name, DateTime startDate, DateTime endDate, String location) {
        this.name = name;
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.location = location;
    }

    public String getName(){
        return name;
    }

//    public Date getLameStartDate() {
//        return startDate;
//    }
//
//    public Date getLameEndDate(){
//        return endDate;
//    }

    public DateTime getStartDate() {
        return mStartDate;
    }

    public DateTime getEndDate() {
        return mEndDate;
    }

    public String getLocation() {
        return location;
    }

//    Not used
//    /**
//     * Parsing from string to date in ISO-8601 format
//     * @param date
//     * @returna
//     */
//    private static Date parseDateString(String date){
//        Log.i("calevent", "parseDateString: date: " + date);
//        int year = Integer.parseInt(date.substring(0,4));
//        int month = Integer.parseInt(date.substring(5,7));
//        int day = Integer.parseInt(date.substring(8,10));
//        int hour = Integer.parseInt(date.substring(11,13));
//        int min = Integer.parseInt(date.substring(14,16));
//        return new Date(year-1900, month-1, day, hour, min);
//    }

    @Override
    public String toString(){
        return   name + "\t" + mStartDate + "\t" + mEndDate;
    }
}