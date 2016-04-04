package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;

/**
 * Created by Follo on 16.02.2016.
 */
public class Message {

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

    public Message(String authorID, String date, String message) {
        this.authorID = authorID;
        this.date = date;
        this.message = message;
    }

    public String getAuthorID() {
        return authorID;
    }

    public String getMessage() {
        return message;
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
