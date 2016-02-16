package com.mossige.finseth.follo.inf219_mitt_uib.models;

/**
 * Created by PatrickFinseth on 16.02.16.
 */
public class Announcement {

    private String title;
    private String userName;
    private String message;

    public Announcement(String title, String userName, String message){
        this.title = title;
        this.userName = userName;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }
}
