package com.mossige.finseth.follo.inf219_mitt_uib.models;

/**
 * Created by Follo on 22.02.2016.
 */
public class Participant {

    private String id;
    private String name;

    public Participant(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
