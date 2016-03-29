package com.mossige.finseth.follo.inf219_mitt_uib.models;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Follo on 20.03.2016.
 */
public class RecipientGroup {

    private String id;
    private String name;
    private String size;

    public RecipientGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

}
