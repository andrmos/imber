package com.mossige.finseth.follo.inf219_mitt_uib.models;

import java.util.ArrayList;

/**
 * Model used for creating a conversation, sent to REST API.
 * 
 * Created by andre on 30.12.16.
 */
public class SendMessage {

    private String subject;
    private String body;
    private ArrayList<String> recipients;

    public SendMessage(String subject, String body, ArrayList<String> recipients) {
        this.subject = subject;
        this.body = body;
        this.recipients = recipients;
    }
}
