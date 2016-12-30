package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Created by Follo on 16.02.2016.
 */
public class Message {

    private int id;
    private String author_id;
    private String created_at;
    private String body;

    public Message(String authorID, String date, String body, String author) {

        // TODO Remove?
        //Make datetime object with string - subststring removes 'z' in created_at format
        DateTime dt = new DateTime(date.substring(0,date.length()-1));
        if(!author.equals("FAILED")) {
            dt = dt.changeTimeZone(TimeZone.getTimeZone("UTC"), TimeZone.getTimeZone("Europe/Oslo"));
            this.created_at = trimDate(dt.toString());
        }
        this.author_id = authorID;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public String getAuthorId() { return author_id; }

    // TODO Remove
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

    public String getCreatedAt() {
        return created_at;
    }

    public boolean equals(Message that) {

        if (!this.getAuthorId().equals(that.getAuthorId())) {
            return false;
        }

        //TODO Date from a JSONObject will not be equals to a generated created_at
//        if(!this.getCreatedAt().equals(that.getCreatedAt())) {
//            return false;
//        }

        if (!this.getBody().equals(that.getBody())) {
            return false;
        }

        return true;
    }
}
