package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;

/**
 * Created by PatrickFinseth on 16.02.16.
 */
public class Course {

    private String id;
    private String name;
    private String calender;
//    private ArrayList<Announcement> announcements;

    public Course(String id, String name, String calender){
        this.id = id;
        this.name = name;
        this.calender = calender;
//        announcements = new ArrayList<Announcementmcmd>(id);
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getCalenderUrl(){
        return calender;
    }
//
//    public ArrayList<Announcement> getAnnouncement(){
//        return announcements;
//    }

}
