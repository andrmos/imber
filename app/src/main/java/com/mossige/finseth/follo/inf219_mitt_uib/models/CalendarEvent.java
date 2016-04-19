package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by PatrickFinseth on 23.02.16.
 */
public class CalendarEvent {

    private Date startDate;
    private Date endDate;
    private String name;
    private String location;

    public CalendarEvent(String name, String startDate, String endDate, String location){
        this.name = name;
        this.startDate = parseDateString(startDate);
        this.endDate = parseDateString(endDate);
        this.location = location;
    }

    public CalendarEvent(String name, Date startDate, Date endDate, String location){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }

    public String getName(){
        return name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate(){
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Parsing from string to date in ISO-8601 format
     * @param date
     * @returna
     */
    private static Date parseDateString(String date){
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));
        int hour = Integer.parseInt(date.substring(11,13));
        int min = Integer.parseInt(date.substring(14,16));
        return new Date(year-1900, month-1, day, hour, min);
    }

    @Override
    public String toString(){
        return   name + "\t" + startDate + "\t" + endDate;
    }
}