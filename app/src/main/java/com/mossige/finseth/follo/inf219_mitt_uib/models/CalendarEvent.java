package com.mossige.finseth.follo.inf219_mitt_uib.models;

import android.util.Log;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by PatrickFinseth on 23.02.16.
 */
public class CalendarEvent implements Comparable<CalendarEvent>{

    private static final String TAG = "CalendarEvent";
    public static final String fail = "FAILED";

    private String name;

    private String location;
    private DateTime mStartDate;
    private DateTime mEndDate;

    public CalendarEvent(String name, DateTime startDate, DateTime endDate, String location, TimeZone oldTimeZone) {
        this.name = trimEventName(name);
        TimeZone newTimeZone = TimeZone.getTimeZone("Europe/Oslo");
        this.mStartDate = startDate.changeTimeZone(oldTimeZone, newTimeZone);
        this.mEndDate = endDate.changeTimeZone(oldTimeZone, newTimeZone);

        this.location = trimLocation(location);
    }

    public String getName(){
        return name;
    }

    public DateTime getStartDate() {
        return mStartDate;
    }

    public DateTime getEndDate() {
        return mEndDate;
    }

    public String getLocation() {
        return location;
    }

    /**
     * Remove brackets and unnecessary name info
     * @param name
     * @return
     */
    private String trimEventName(String name){
        String[] splitArray = name.split(" ");
        String trimedName = "";

        if(splitArray[0].startsWith("[")) {
            String course_code = "";
            for (int i = 1; i < splitArray[0].length(); i++){
                if(splitArray[0].charAt(i) != ']'){
                    course_code += splitArray[0].charAt(i);
                }

            }
            trimedName += course_code;
            trimedName += " " + splitArray[1];
        }else{
            trimedName = name;
        }

        return trimedName;
    }

    private String trimLocation(String location){
        String trimmedLocation = location.trim();

        if(trimmedLocation.startsWith("(")){
            trimmedLocation = trimmedLocation.substring(1,trimmedLocation.length());

            if(location.endsWith(")")){
                trimmedLocation = trimmedLocation.substring(0, trimmedLocation.length() - 1);
                return trimmedLocation;
            }
        }


        return location.trim();
    }

    @Override
    public int compareTo(CalendarEvent event){
        if(this.getStartDate().equals(event.getStartDate())) {
            return 0;
        }else if(this.getStartDate().gt(event.getStartDate())){
            return 1;
        } else {
            return -1;
        }
    }

    public static CalendarEvent getFailedCalendarEvent() {
        return new CalendarEvent(fail, new DateTime(1970, 1, 1, 0, 0, 0, 0), new DateTime(1970, 1, 1, 0, 0, 0, 0), fail, TimeZone.getTimeZone("UTC"));
    }

    @Override
    public String toString(){
        return   name + "\t" + mStartDate + "\t" + mEndDate;
    }
}