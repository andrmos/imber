package com.mossige.finseth.follo.inf219_mitt_uib.models;

/**
 * Created by PatrickFinseth on 16.02.16.
 */
public class Announcement {

    private String id;
    private String title;
    private String userName;
    private String postedAt;
    private String message;
    private boolean unread;

    public Announcement(String id, String title, String userName, String postedAt, String message, boolean unread){
        this.id = id;
        this.title = title;
        this.userName = userName;
        this.postedAt = postedAt;
        this.message = message;
        this.unread = unread;
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public String getMessage() {
        return message;
    }

    public boolean equals(Announcement that) {
        if(!this.getId().equals(that.getId())) {
            return false;
        }

        if (!this.getTitle().equals(that.getTitle())) {
            return false;
        }

        if (!this.getUserName().equals(that.getUserName())) {
            return false;
        }

        if (!this.getMessage().equals(that.getMessage())) {
            return false;
        }
        return true;
    }

}
