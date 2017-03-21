package no.mofifo.imber.models;

import java.util.ArrayList;

/**
 * Model used for creating a conversation, sent to REST API.
 * 
 * Created by andre on 30.12.16.
 */
public class SendMessage {

    private String subject;
    private String body;
    private ArrayList<Integer> recipients;

    public SendMessage(String subject, String body, ArrayList<Integer> recipients) {
        this.subject = subject;
        this.body = body;
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }

    public ArrayList<Integer> getRecipients() {
        return recipients;
    }
}
