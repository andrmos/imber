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
    private String primary_email;
    private String login_id;

    public User(String id, String name, String primary_email, String login_id, String calendar) {
        this.id = id;
        this.name = name;
        this.primary_email = primary_email;
        this.login_id = login_id;
    }

    public String getPrimary_email() {
        return primary_email;
    }

    public String getLogin_id() {
        return login_id;
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

        if (!this.getLogin_id().equals(that.getLogin_id())) {
            return false;
        }

        if (!this.getPrimary_email().equals(that.getPrimary_email())) {
            return false;
        }

        if (!this.getName().equals(that.getName())) {
            return false;
        }

        return true;
    }

}
