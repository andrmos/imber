package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;

/**
 * Created by Follo on 16.02.2016.
 */
public class Message {

    private ArrayList<String> participants;
    private String date;
    private String message;

    public Message(ArrayList<String> participants, String date, String message) {
        this.participants = participants;
        this.date = date;
        this.message = message;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
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
