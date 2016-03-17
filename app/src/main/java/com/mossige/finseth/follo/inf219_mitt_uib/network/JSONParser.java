package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Message;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Participant;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parser for different JSONRequests
 *
 * @author Øystein Follo
 * @date 21.02.2016
 */
public class JSONParser {

    private static final String TAG = "JSONParser";
    private static boolean courseFilter = true;

    public JSONParser() {
        Log.i(TAG, "JSONParser: " + "JSONParser created");
    }


    /**
     * Parses a user profile
     * @param unParsed JSONObject you get onResponse with the request
     * @return A {@link User user}
     * @throws JSONException
     */
    public static User parseUserProfile(JSONObject unParsed) throws JSONException {
        String id = unParsed.getString("id");
        String name = unParsed.getString("name");
        String email = unParsed.getString("primary_email");
        String loginID = unParsed.getString("login_id");
        String calendar = unParsed.getJSONObject("calendar").getString("ics");

        return new User(id, name, email, loginID, calendar);
    }

    /**
     * Parses all conversations
     * Used when the latest conversations are requested
     * @param unParsed JSONArray you get onResponse with the request
     * @return An ArrayList containing the latest {@link Message messages} in previous {@link Conversation conversations}
     * @throws JSONException
     */
    public static ArrayList<Conversation> parseAllConversations(JSONArray unParsed) throws JSONException {
        ArrayList<Conversation> parsed = new ArrayList<>();

        for (int i = 0; i < unParsed.length(); i++) {
            parsed.add(getLastMessage(unParsed.getJSONObject(i)));
            Log.i(TAG, "parseAllConversations: " + "Conversation with id: " + parsed.get(i).getId() + " is parsed");
        }

        return parsed;
    }

    /**
     * Parses a single conversation
     * Used when a whole conversation is requested
     * @param unParsed JSONObject you get onResponse with the request
     * @return The requested {@link Conversation conversation}
     * @throws JSONException
     */
    public static Conversation parseSingleConversation(JSONObject unParsed) throws JSONException {
        String id = unParsed.getString("id");
        String subject = unParsed.getString("subject");
        ArrayList<Participant> participants = getParticipants(unParsed.getJSONArray("participants"));
        ArrayList<Message> messages = getMessages(unParsed.getJSONArray("messages"));

        Log.i(TAG, "parseSingleConversation: " + "Conversation with id: " + id + " is parsed");
        return new Conversation(id, subject, participants, messages);
    }

    /**
     * Parses all courses
     * Used when all the courses are requested
     * @param unParsed JSONArray you get onResponse with the request
     * @return An ArrayList containing all the users {@link Course courses}
     * @throws JSONException
     */
    public static ArrayList<Course> parseAllCourses(JSONArray unParsed) throws JSONException {

        ArrayList<Course> parsed = new ArrayList<>();
        
        for (int i = 0; i < unParsed.length(); i++) {

            if(courseFilter){
                if(unParsed.getJSONObject(i).getString("course_code").matches(".*\\d.*")) {
                    parsed.add(getSingleCourse(unParsed.getJSONObject(i)));
                }
            }else {
                parsed.add(getSingleCourse(unParsed.getJSONObject(i)));
            }

        }

        Log.i(TAG, "parseAllCourses: " + "All courses parsed");

        return parsed;
    }

    /**
     * Parses all announcements
     * Used when all the announcements are requested
     * @param unParsed JSONArray you get onResponse with the request
     * @return An ArrayList containing all the users {@link Announcement announcements}
     * @throws JSONException
     */
    public static ArrayList<Announcement> parseAllAnouncements(JSONArray unParsed) throws JSONException {
        ArrayList<Announcement> parsed = new ArrayList<>();

        for (int i = 0; i < unParsed.length(); i++) {
            parsed.add(getSingleAnnouncement(unParsed.getJSONObject(i)));
        }

        return parsed;
    }

    private static Conversation getLastMessage(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String subject = obj.getString("subject");
        ArrayList<Participant> participants = getParticipants(obj.getJSONArray("participants"));
        String lastMessage = obj.getString("last_message");

        return new Conversation(id,subject, participants, lastMessage);
    }

    private static ArrayList<Message> getMessages(JSONArray unParsed) throws JSONException {
        ArrayList<Message> parsed = new ArrayList<>();

        for (int i = 0; i < unParsed.length(); i++) {
            parsed.add(getSingleMessage(unParsed.getJSONObject(i)));
        }

        return parsed;
    }

    private static Message getSingleMessage(JSONObject obj) throws JSONException {
        String authorID = obj.getString("author_id");
        String date = obj.getString("created_at");
        String message = obj.getString("body");

        Log.i(TAG, "getSingleMessage: message is parsed" + authorID);

        return new Message(authorID, date, message);
    }

    private static ArrayList<Participant> getParticipants(JSONArray unParsed) throws JSONException {
        ArrayList<Participant> participants = new ArrayList<>();

        for (int i = 0; i < unParsed.length(); i++) {
            participants.add(getSingleParticipant(unParsed.getJSONObject(i)));
        }

        return participants;
    }

    private static Participant getSingleParticipant(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String name = obj.getString("name");

        Log.i(TAG, "getSingleParticipant: participant is parsed " + name);

        return new Participant(id, name);
    }

    private static Announcement getSingleAnnouncement(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String title = obj.getString("title");
        String userName = obj.getString("user_name");
        String postedAt = obj.getString("posted_at");
        String message = obj.getString("message");
        boolean unread = obj.getString("read_state").equals("true");

        return new Announcement(id, title, userName, postedAt, message, unread);

    }

    private static Course getSingleCourse(JSONObject obj) throws JSONException {
        int id = obj.getInt("id");
        String name = obj.getString("name");
        String cal = obj.getJSONObject("calendar").getString("ics");
        String code = obj.getString("course_code");

        return new Course(id, name, cal, code);
    }

}
