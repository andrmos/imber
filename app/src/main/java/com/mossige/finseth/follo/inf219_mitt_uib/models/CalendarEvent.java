package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by PatrickFinseth on 23.02.16.
 */
public class CalendarEvent {

    private String name;
    private String location;
    private DateTime mStartDate;
    private DateTime mEndDate;

    public CalendarEvent(String name, String startDate, String endDate, String location){
        this.name = trimEventName(name);
        // TODO Change to timezone from profile?
        this.mStartDate = new DateTime(startDate).changeTimeZone(TimeZone.getTimeZone("GMT+0"),TimeZone.getTimeZone("GMT+1"));
        this.mEndDate = new DateTime(endDate).changeTimeZone(TimeZone.getTimeZone("GMT+0"), TimeZone.getTimeZone("GMT+1"));
        this.location = location;
    }

    public CalendarEvent(String name, DateTime startDate, DateTime endDate, String location) {
        this.name = trimEventName(name);
        // TODO Change to timezone from profile?
        this.mStartDate = startDate.changeTimeZone(TimeZone.getTimeZone("GMT+0"), TimeZone.getTimeZone("GMT+1"));
        this.mEndDate = endDate.changeTimeZone(TimeZone.getTimeZone("GMT+0"), TimeZone.getTimeZone("GMT+1"));
        this.location = location;
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
            trimedName += splitArray[0];
            trimedName += " " + splitArray [1];
        }

        return trimedName;
    }

    @Override
    public String toString(){
        return   name + "\t" + mStartDate + "\t" + mEndDate;
    }
}