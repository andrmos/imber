package com.mossige.finseth.follo.inf219_mitt_uib.models;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by PatrickFinseth on 16.02.16.
 */
public class Course {

    private int id;
    private String name;
    private String calender;
    private String courseCode;

    public Course(int id, String name, String calender, String courseCode){
        this.id = id;
        this.name = trimName(name,courseCode);
        this.calender = calender;
        this.courseCode = courseCode;
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

    private String trimName(String name, String courseCode){
        String trimmedName = name;

        String [] splitArray = name.split("/");

        if(splitArray[0].trim().equalsIgnoreCase(courseCode) && splitArray.length > 1){
            trimmedName = splitArray[1].trim();
        }

        return trimmedName;
    }

}
