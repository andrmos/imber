package com.mossige.finseth.follo.inf219_mitt_uib;

import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Message;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Participant;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

import static org.junit.Assert.*;

/**
 * @author Øystein Follo
 */
public class JSONParserTest {

    public static final String TAG = "JSONParserTest";

    @Test
    public void parseUserTest() {
        for (int i = 0; i < lim; i++) {
            User parsed = JSONParser.parseUserProfile(JSONUsers.get(i));
            assertTrue(parsed.equals(users.get(i)));
        }
    }

    @Test
    public void parseRecipientTest() {
        ArrayList<Recipient> parsed = JSONParser.parseAllRecipients(JSONRecipients);
        for (int i = 0; i < lim; i++) {
            assertTrue(parsed.get(i).equals(recipients.get(i)));
        }
    }

    @Test
    public void parseParticipantTest() {
        ArrayList<Participant> parsed = JSONParser.getParticipants(participantList);
        for (int i = 0; i < lim; i++) {
            assertTrue(parsed.get(i).equals(participants.get(i)));
        }
    }

    @Test
    public void parseMessagesTest() {
//        ArrayList<Message> parsed = JSONParser.getMessages(messageList, participants);
//        for (int i = 0; i < lim; i++) {
//            assertTrue(parsed.get(i).equals(messages.get(i)));
//        }
    }


    //Generate test data
    RandomStringGenerator rnd = new RandomStringGenerator();
    final int lim = 100;

    //Announcement
    String annId, annTitle, annUserName, annPostedAt, annMessage;
    boolean annUnread;
    ArrayList<Announcement> announcements;

    //Course
    int couId;
    String couName, couCalendar, couCourseCode;
    ArrayList<Course> courses;

    //CalendarEvent
    String calEName, calELocation;
    DateTime calEStartDate, calEEndDate;
    TimeZone calETimeZone = TimeZone.getTimeZone("Europe/Oslo");
    ArrayList<CalendarEvent> calendarEvents;

    //User
    String usId, usName, usEmail, usLoginId, usCalendar;
    ArrayList<User> users;

    //Recipient
    String recId, recName, recGroup;
    ArrayList<Recipient> recipients;

    //Participant
    String parId, parName;
    ArrayList<Participant> participants;

    //Message
    String mesAuthor, mesAuthorId, mesDate, mesMessage;
    ArrayList<Message> messages;

    //Conversation
    String conId, conSubject, conLastMessage;
    ArrayList<Participant> conParticipant;
    ArrayList<Message> conMessages;
    ArrayList<Conversation> conversations;

    //JSONObjects
    ArrayList<JSONObject> JSONUsers;
    ArrayList<JSONObject> JSONConversation;

    //JSONArrays
    JSONArray JSONRecipients;
    JSONArray JSONCalendarEvents;
    JSONArray JSONConversations;
    JSONArray JSONCourses;
    JSONArray JSONAnnouncements;

    //Help lists
    JSONArray messageList;
    JSONArray participantList;

    @Before
    public void init() throws JSONException {
        generateTestData();
        createHelpLists();
        generateJSONObjects();
        generateJSONArrays();
    }

    private void createHelpLists() {
        if(messageList == null) {
            messageList = new JSONArray();

            for (Message m : messages) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("author", m.getAuthor());
                    obj.put("author_id", m.getAuthorID());
                    obj.put("created_at", m.getDate());
                    obj.put("body", m.getMessage());
                    messageList.put(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if(participantList == null) {
            participantList = new JSONArray();

            for (int i = 0; i < lim; i++) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", participants.get(i).getId());
                    obj.put("name", participants.get(i).getName());
                    participantList.put(i, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void generateJSONObjects() throws JSONException {
        convertUser();
        convertConversation();
    }

    private void generateJSONArrays() {
        convertRecipients();
        convertCalendarEvents();
        convertConversations();
        convertCourses();
        convertAnnouncements();
    }

    private void convertRecipients() {
        JSONRecipients = new JSONArray();

        for (int i = 0; i < lim; i++) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", recipients.get(i).getId());
                obj.put("name", recipients.get(i).getName());
                JSONRecipients.put(i, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertCalendarEvents() {
    }

    private void convertConversations() {

        JSONConversations = new JSONArray();

        for (int i = 0; i < lim; i++) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", conversations.get(i).getId());
                obj.put("subject", conversations.get(i).getSubject());
                obj.put("participants", participantList);
                obj.put("message", messageList);
                JSONConversations.put(i, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertCourses() {
        JSONCourses = new JSONArray();

        for (int i = 0; i < lim; i++) {
            JSONObject obj = new JSONObject();
            JSONObject ics = new JSONObject();
            try {
                obj.put("id", courses.get(i).getId());
                obj.put("name", courses.get(i).getName());
                ics.put("ics", courses.get(i).getCalenderUrl());
                obj.put("calendar", ics);
                obj.put("course_code", courses.get(i).getCourseCode());
                JSONCourses.put(i, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertAnnouncements() {
        JSONAnnouncements = new JSONArray();

        for (int i = 0; i < lim; i++) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", announcements.get(i).getId());
                obj.put("title", announcements.get(i).getTitle());
                obj.put("user_name", announcements.get(i).getUserName());
                if(i % 2 == 0) {
                    obj.put("posted_at", announcements.get(i).getPostedAt());
                }
                obj.put("message", announcements.get(i).getMessage());
                if(!(i % 2 == 0 && i % 10 == 0)) {
                    obj.put("read_state", announcements.get(i).isUnread());
                }
                JSONAnnouncements.put(i, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertConversation() {
        JSONConversation = new ArrayList<>();

        for (int i = 0; i < lim; i++) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("id", conversations.get(i).getId());
                obj.put("subject", conversations.get(i).getSubject());
                obj.put("participants", participantList);
                obj.put("messages", messageList);
                JSONConversation.add(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void convertUser() throws JSONException {
        JSONUsers = new ArrayList<>();

        for (int i = 0; i < lim; i++) {
            JSONObject obj = new JSONObject();
            JSONObject ics = new JSONObject().
                put("ics", users.get(i).getCalendar());
            obj.put("id", users.get(i).getId());
            obj.put("name", users.get(i).getName());
            obj.put("primary_email", users.get(i).getEmail());
            obj.put("login_id", users.get(i).getLoginID());
            obj.put("calendar", ics);
            JSONUsers.add(obj);
        }
    }

    public void generateTestData() {
        generateAnnouncements();
        generateMessages();
        generateParticipants();
        generateCalendarEvents();
        generateConversations();
        generateCourses();
        generateRecipients();
        generateUsers();
    }

    private void generateUsers() {
        users = new ArrayList<>();
        for (int i = 0; i < lim; i++) {
            usId = rnd.nextSessionId();
            usName = rnd.nextSessionId();
            usCalendar = rnd.nextSessionId();
            usEmail = rnd.nextSessionId();
            usLoginId = rnd.nextSessionId();
            users.add(new User(usId, usName, usEmail, usLoginId, usCalendar));
        }
    }

    private void generateRecipients() {
        recipients = new ArrayList<>();
        for (int i = 0; i < lim; i++) {
            recId = rnd.nextSessionId();
            recName = rnd.nextSessionId();
            recipients.add(new Recipient(recId, recName));
        }
    }

    private void generateParticipants() {
        participants = new ArrayList<>();
        for (int i = 0; i < lim; i++) {
            parId = rnd.nextSessionId();
            parName = rnd.nextSessionId();
            participants.add(new Participant(parId, parName));
        }
    }

    private void generateMessages() {
        messages = new ArrayList<>();
        for (int i = 0; i < lim; i++) {
            mesAuthor = rnd.nextSessionId();
            mesAuthorId = rnd.nextSessionId();
            mesDate = "2016-04-14T16:36:40Z";
            mesMessage = rnd.nextSessionId();
            messages.add(new Message(mesAuthorId, mesDate, mesMessage, mesAuthor));
        }
    }

    private void generateCourses() {
        courses = new ArrayList<>();
        for (int i = 0; i < lim; i++) {
            couCalendar = rnd.nextSessionId();
            couCourseCode = rnd.nextSessionId();
            couId = i;
            couName = rnd.nextSessionId();
            courses.add(new Course(couId, couName, couCalendar, couCourseCode));
        }
    }

    private void generateConversations() {
        conversations = new ArrayList<>();
        for (int i = 0; i < lim; i++) {
            conId = rnd.nextSessionId();
            conLastMessage = rnd.nextSessionId();
            conMessages = messages;
            conParticipant = participants;
            conSubject = rnd.nextSessionId();
            conversations.add(new Conversation(conId, conSubject, conParticipant, conMessages));
        }
    }

    private void generateCalendarEvents() {
        calendarEvents = new ArrayList<>();
        for (int i = 0; i < lim; i++) {
            calEName = rnd.nextSessionId();
            calELocation = rnd.nextSessionId();
            calEStartDate = new DateTime(1970, 1, 1, 1, 0, 0, 0);
            calEEndDate = new DateTime(1970, 1, 1, 1, 1, 0, 0);
            calendarEvents.add(new CalendarEvent(calEName, calEStartDate, calEEndDate, calELocation, calETimeZone));
        }
    }

    private void generateAnnouncements() {
        announcements = new ArrayList<>();
        for (int i = 0; i < lim; i++) {
            annId = rnd.nextSessionId();
            annMessage = rnd.nextSessionId();
            annPostedAt = rnd.nextSessionId();
            annTitle = rnd.nextSessionId();
            annUnread = i % 2 == 0;
            annUserName = rnd.nextSessionId();
            announcements.add(new Announcement(annId, annTitle, annUserName, annPostedAt, annMessage, annUnread));
        }
    }
}