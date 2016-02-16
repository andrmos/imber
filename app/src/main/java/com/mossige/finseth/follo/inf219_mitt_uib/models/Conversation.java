package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;

/**
 * Created by Follo on 16.02.2016.
 */
public class Conversation {

    private String id;
    private String subject;
    private ArrayList<String> participants;
    private ArrayList<Message> messages;
    private String lastMessage;

    public Conversation(String id, String subject, ArrayList<String> participants, String lastMessage) {
        this.id = id;
        this.subject = subject;
        this.participants = participants;
        this.lastMessage = lastMessage;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public ArrayList<Message> getMessages() {

        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public String getSubject() {

        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<String> getParticipants() {

        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
