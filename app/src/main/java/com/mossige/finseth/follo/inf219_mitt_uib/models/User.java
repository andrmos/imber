package com.mossige.finseth.follo.inf219_mitt_uib.models;

/**
 * User model
 *
 * @author Øystein Follo
 * @date 23.02.2016
 */
public class User {

    private String id;
    private String name;
    private String email;
    private String loginID;
    public String calendar;

    public User(String id, String name, String email, String loginID, String calendar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.loginID = loginID;
        this.calendar = calendar;
    }

    public String getCalendar() {
        return calendar;
    }

    public String getEmail() {
        return email;
    }

    public String getLoginID() {
        return loginID;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean equals(User that) {

        if(!this.getId().equals(that.getId())) {
            return false;
        }

        if (!this.getLoginID().equals(that.getLoginID())) {
            return false;
        }

        if (!this.getCalendar().equals(that.getCalendar())) {
            return false;
        }

        if (!this.getEmail().equals(that.getEmail())) {
            return false;
        }

        if (!this.getName().equals(that.getName())) {
            return false;
        }

        return true;
    }

}
