package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;

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

    public Message(ArrayList<String> recipiants, String subject, String body) {
        this.recipiants = recipiants;
        this.subject = subject;
        this.body = body;
    }

    public Message(String authorID, String date, String message, String author) {
        this.author = author;
        this.authorID = authorID;
        this.date = trimDate(date);
        this.message = message;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor(){
        return author;
    }

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
}
