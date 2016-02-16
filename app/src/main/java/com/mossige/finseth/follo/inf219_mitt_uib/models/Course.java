package com.mossige.finseth.follo.inf219_mitt_uib.models;

/**
 * Created by PatrickFinseth on 16.02.16.
 */
public class Course {

    private int id;
    private String name;
    private String calender;
    private Announcement announcement;

    public Course(int id, String name, String calender){
        this.id = id;
        this.name = name;
        this.calender = calender;
        announcement = new Announcement(id);
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getCalenderUrl(){
        return calender;
    }

    public Announcement getAnnouncement(){
        return announcement;
    }

}
