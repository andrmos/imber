package com.mossige.finseth.follo.inf219_mitt_uib.models;

/**
 * Created by PatrickFinseth on 16.02.16.
 */
public class Announcement {

    private String id;
    private String title;
    private String user_name;
    private String posted_at;
    private String message;
    private int unread_count;
    private boolean locked;

    // TODO Update constructor
    public Announcement(String id, String title, String userName, String postedAt, String message, boolean locked){
        this.id = id;
        this.title = title;
        this.user_name = userName;
        this.posted_at = postedAt;
        this.message = message;
        this.locked = locked;
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return user_name;
    }

    public String getPostedAt() {
        return posted_at;
    }

    public String getMessage() {
        return message;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getUnread_count() {
        return unread_count;
    }
}
