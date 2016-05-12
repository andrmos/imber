package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Follo on 16.02.2016.
 */
public class Message {

    private String author;
    private String authorID;
    private String date;
    private String message;

    private ArrayList<String> recipiants;
    private String subject;
    private String body;

    public Message(String authorID, String date, String message, String author) {

        //Make datetime object with string - subststring removes 'z' in date format
        DateTime dt = new DateTime(date.substring(0,date.length()-1));
        if(!author.equals("FAILED")) {
            dt = dt.changeTimeZone(TimeZone.getTimeZone("UTC"), TimeZone.getTimeZone("Europe/Oslo"));
            this.date = trimDate(dt.toString());
        }
        this.author = author;
        this.authorID = authorID;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor(){
        return author;
    }

    public String getAuthorID() { return authorID; }

    private static String trimDate(String date){
        String dateToString = "";

        String year = date.substring(0, 4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);
        String hour = date.substring(11,13);
        String min = date.substring(14,16);

        dateToString = day + "/" + month + "-" + year + "  " + hour + ":" + min;

        return dateToString;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean equals(Message that) {
        if (!this.getAuthor().equals(that.getAuthor())) {
            return false;
        }

        if (!this.getAuthorID().equals(that.getAuthorID())) {
            return false;
        }

        if(!this.getDate().equals(that.getDate())) {
            return false;
        }

        if (!this.getMessage().equals(that.getMessage())) {
            return false;
        }

        return true;
    }
}
