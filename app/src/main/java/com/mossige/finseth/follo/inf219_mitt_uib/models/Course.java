package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;

/**
 * Created by PatrickFinseth on 16.02.16.
 */
public class Course {

    private int id;
    private String name;
    private String calender;
    private String courseCode;
//    private ArrayList<Announcement> announcements;

    public Course(int id, String name, String calender, String courseCode){
        this.id = id;
        this.name = name;
        this.calender = calender;
        this.courseCode = courseCode;
//        announcements = new ArrayList<Announcementmcmd>(id);
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

    public String getCourseCode(){
        return courseCode;
    }
//
//    public ArrayList<Announcement> getAnnouncement(){
//        return announcements;
//    }

}
