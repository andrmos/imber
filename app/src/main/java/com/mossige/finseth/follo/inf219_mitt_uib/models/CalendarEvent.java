package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by PatrickFinseth on 23.02.16.
 */
public class CalendarEvent {

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;

    public CalendarEvent(String name,LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime){
        this.name = name;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
    }

    public String getName(){
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate(){
        return endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString(){
        return   name + ": " + startDate + "\t" + startTime + "\t" + endDate + "\t" + endTime;
    }
}