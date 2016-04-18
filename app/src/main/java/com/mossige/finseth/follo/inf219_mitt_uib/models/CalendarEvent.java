package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.Date;

/**
 * Created by PatrickFinseth on 23.02.16.
 */
public class CalendarEvent {

    private Date startDate;
    private Date endDate;
    private String name;
    private String summary;
    private String location;

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

    @Override
    public String toString(){
        return   name + "\t" + startDate + "\t" + endDate;
    }
}