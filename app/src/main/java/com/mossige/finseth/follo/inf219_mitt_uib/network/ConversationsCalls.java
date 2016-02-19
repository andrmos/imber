package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.os.SystemClock;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Follo on 16.02.2016.
 */
public class ConversationsCalls {

    ArrayList<Conversation> conversations;

    public ConversationsCalls() {

    }

    public ArrayList<Conversation> getConversations() {

        conversations = new ArrayList<>();

        String url = "https://mitt.uib.no/api/v1/conversations?access_token=" + PrivateConstants.ACCESS_TOKEN;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject resp = null;

                String id;
                String subject;
                ArrayList<String> participants = new ArrayList<>();
                ArrayList<Message> messages = new ArrayList<>();
                String lastMessage;

                JSONArray tmpParticipants;
                JSONObject participant;

                Message message;
                String messageDate;
                String messageLastMessage;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        resp = response.getJSONObject(i);
                        id = resp.getString("id");
                        subject = resp.getString("subject");
                        tmpParticipants = new JSONArray(resp.getString("participants"));
                        for (int j = 0; j < tmpParticipants.length(); j++) {
                            participant = tmpParticipants.getJSONObject(j);
                            participants.add(participant.getString("name"));
                        }

                        messageDate = resp.getString("last_message_at");
                        messageLastMessage = resp.getString("last_message");

                        message = new Message(participants, messageDate, messageLastMessage);

                        conversations.add(new Conversation(id, subject, participants, messageLastMessage));


                    } catch (JSONException e) {
                        e.getStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }

        });


        //TODO Fix
        while(!jsonArrayRequest.hasHadResponseDelivered()) {
            SystemClock.sleep(200);
        }

        return conversations;
    }

}
