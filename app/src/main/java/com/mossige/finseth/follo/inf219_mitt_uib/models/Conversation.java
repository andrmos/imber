package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;

/**
 * Conversationmodel
 *
 * @author Øystein Follo
 * @date 16.02.2016
 */
public class Conversation {

    private String id;
    private String subject;
    private ArrayList<Participant> participants;
    private ArrayList<Message> messages;
    private String lastMessage;

    /**
     * Used when you want all the messages in a conversation
     *
     * @param id String containing conversation id
     * @param subject String containing conversation subject
     * @param participants ArrayList containing the conversation {@link Participant participants}
     * @param messages ArrayList containing the conversation {@link Message messages}
     */
    public Conversation(String id, String subject, ArrayList<Participant> participants, ArrayList<Message> messages) {
        this.id = id;
        this.subject = subject;
        this.participants = participants;
        this.lastMessage = messages.get(messages.size()-1).getMessage();
        this.messages = messages;
    }

    /**
     * Used when you want last message of the conversation
     * Creates an empty ArrayList of messages
     *
     * @param id String containing conversation id
     * @param subject String containing conversation subject
     * @param participants ArrayList containing the conversation {@link Participant participants}
     * @param lastMessage String containg the last message in the conversation
     */
    public Conversation(String id, String subject, ArrayList<Participant> participants, String lastMessage) {
        this.id = id;
        this.subject = subject;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.messages = new ArrayList<>();
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

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
