package com.mossige.finseth.follo.inf219_mitt_uib;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by PatrickFinseth on 16.02.16.
 */
public class Course {

    private int id;
    private String name;
    private String calender;
    private ArrayList<Announcement> announcements;

    public Course(int id, String name, String calender){
        this.id = id;
        this.name = name;
        this.calender = calender;
        announcements = new ArrayList<Announcement>(id);
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

    public ArrayList<Announcement> getAnnouncement(){
        return announcements;
    }

}
