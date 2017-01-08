package com.mossige.finseth.follo.inf219_mitt_uib.models;

import com.google.gson.annotations.SerializedName;

import hirondelle.date4j.DateTime;

/**
 * Created by Follo on 16.02.2016.
 */
public class Message {

    private int id;
    @SerializedName("author_id")
    private int authorId;
    @SerializedName("created_at")
    private DateTime createdAt;
    private String body;

    public Message(int id, int authorId, DateTime createdAt, String body) {
        this.id = id;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (id != message.id) return false;
        if (authorId != message.authorId) return false;
        if (createdAt != null ? !createdAt.equals(message.createdAt) : message.createdAt != null)
            return false;
        return body != null ? body.equals(message.body) : message.body == null;

    }
}
