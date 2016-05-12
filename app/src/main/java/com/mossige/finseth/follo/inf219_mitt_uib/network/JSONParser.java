package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.content.Context;
import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.courseBank.CourseBank;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Message;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Participant;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

/**
 * Parser for different JSONRequests
 *
 * @author Øystein Follo
 * @date 21.02.2016
 */
public class JSONParser {

    private static final String TAG = "JSONParser";

    private static CourseBank courseBank;

    //Standard variables for if the parser fails
    public static final String fail = "FAILED";
    public static final CalendarEvent FAILED_CALENDAR_EVENT = new CalendarEvent(fail, new DateTime(1970, 1, 1, 0, 0, 0, 0), new DateTime(1970, 1, 1, 0, 0, 0, 0), fail, TimeZone.getTimeZone("UTC"));
    public static final Message FAILED_MESSAGE = new Message(fail, fail, fail, fail);
    public static final Course FAILED_COURSE = new Course(-1, fail, fail, fail);
    public static final Announcement FAILED_ANNOUNCEMENT = new Announcement(fail, fail, fail, fail, fail, false);
    public static final Participant FAILED_PARTICIPANT = new Participant(fail, fail);
    public static final Conversation FAILED_CONVERSATION = new Conversation(fail, fail, new ArrayList<Participant>(), fail);
    public static final Recipient FAILED_RECIPIENT = new Recipient(fail, fail);
    public static final User FAILED_USER = new User(fail, fail, fail, fail, fail);

    public JSONParser() {}

    /**
     * Parses all the recipients within a group
     * @param unParsed JSONArray you get onResponse with the request
     * @return a list containing successfully parsed {@link Recipient recipients}
     */
    public static ArrayList<Recipient> parseAllRecipients(JSONArray unParsed) {
        ArrayList<Recipient> parsed = new ArrayList<>();

        Recipient current;
        for (int i = 0; i < unParsed.length(); i++) {
            try {
                current = parseOneRecipient(unParsed.getJSONObject(i));
            } catch(JSONException e) {
                current = FAILED_RECIPIENT;
                Log.e(TAG, "parseAllRecipients: ", e);
                Log.i(TAG, "parseAllRecipients: failed to parse a recipient due to a JSONException");
            }

            if(current.equals(FAILED_RECIPIENT)) {
                Log.i(TAG, "parseAllRecipients: failed to parse recipient " + i);
            } else {
                parsed.add(current);
            }
        }

        return parsed;
    }

    /**
     * Parsing all calendar events from unparsed param
     * @param unParsed JSONArray containing calendar events
     * @return A list containing successfully parsed {@link CalendarEvent calendar events}
     */
    public static ArrayList<CalendarEvent> parseAllCalendarEvents(JSONArray unParsed) {
        ArrayList<CalendarEvent> parsed = new ArrayList<>();

        CalendarEvent current;
        for(int i = 0; i < unParsed.length(); i++){
            try {
                JSONObject obj = unParsed.getJSONObject(i);

                if (obj.has("hidden")) {
                    boolean hidden = obj.getBoolean("hidden");
                    if (!hidden) {
                        current = parseOneCalendarEvent(obj);
                    } else {
                        continue;
                    }
                } else {
                    current = parseOneCalendarEvent(obj);
                }
            } catch (JSONException e) {
                current = FAILED_CALENDAR_EVENT;
                Log.e(TAG, "parseAllCalendarEvents: ", e);
                Log.i(TAG, "parseAllCalendarEvents: failed to parse a calendar event due to a JSONException");
            }

            if (current.equals(FAILED_CALENDAR_EVENT)) {
                Log.i(TAG, "parseAllCalendarEvents: failed to parse calendar event " + i);
            } else {
                parsed.add(current);
            }
        }

        return parsed;
    }

    /**
     * Parses a user profile
     * @param unParsed JSONObject you get onResponse with the request
     * @return if successful: a {@link User user}
     * @return if not successful: a {@link User user} with the format of {@link #FAILED_USER}
     */
    public static User parseUserProfile(JSONObject unParsed) {
        try {
            String id = unParsed.getString("id");
            String name = unParsed.getString("name");
            String email = unParsed.getString("primary_email");
            String loginID = unParsed.getString("login_id");
            String calendar = unParsed.getJSONObject("calendar").getString("ics");

            return new User(id, name, email, loginID, calendar);
        } catch (JSONException e) {
            Log.e(TAG, "parseUserProfile: ", e);
            Log.i(TAG, "parseUserProfile: failed to parse user due to a JSONException");
        }
        return FAILED_USER;
    }

    /**
     * Parses all conversations
     * Used when the latest conversations are requested
     * @param unParsed JSONArray you get onResponse with the request
     * @return a list containing successfully parsed newest {@link Message messages} in previous {@link Conversation conversations}
     */
    public static ArrayList<Conversation> parseAllConversations(JSONArray unParsed) {
        ArrayList<Conversation> parsed = new ArrayList<>();

        Conversation current;
        for (int i = 0; i < unParsed.length(); i++) {
            try {
                current = getLastMessage(unParsed.getJSONObject(i));
            } catch (JSONException e) {
                current = FAILED_CONVERSATION;
                Log.e(TAG, "parseAllConversations: ", e);
                Log.i(TAG, "parseAllConversations: failed to load a conversation due to a JSONException");
            }

            if(current.equals(FAILED_CONVERSATION)) {
                Log.i(TAG, "parseAllConversations: failed to parse conversation " + i);
            } else {
                parsed.add(current);
            }
        }

        return parsed;
    }

    /**
     * Parses a single conversation
     * Used when a whole conversation is requested
     * @param unParsed JSONObject you get onResponse with the request
     * @return if successful: a {@link Conversation conversation}
     * @return if not successful: a {@link Conversation conversation} with the format {@link #FAILED_CONVERSATION}
     */
    public static Conversation parseSingleConversation(JSONObject unParsed) {
        try {
            String id = unParsed.getString("id");
            String subject = unParsed.getString("subject");
            ArrayList<Participant> participants = getParticipants(unParsed.getJSONArray("participants"));
            //TODO Don't want to deny the whole conversation if one message is missing
            ArrayList<Message> messages = getMessages(unParsed.getJSONArray("messages"), participants);

            return new Conversation(id, subject, participants, messages);
        } catch (JSONException e) {
            Log.e(TAG, "parseSingleConversation: ", e);
            Log.i(TAG, "parseSingleConversation: failed to parse a conversation due to a JSONException");
        }

        return FAILED_CONVERSATION;
    }

    /**
     * Parses all courses
     * Used when all the courses are requested
     * @param unParsed JSONArray you get onResponse with the request
     * @return A list containing successfully parsed {@link Course courses}
     */
    public static ArrayList<Course> parseAllCourses(JSONArray unParsed, boolean instituteFilter, Context context) throws FileNotFoundException {

        ArrayList<Course> parsed = new ArrayList<>();

        Course current;
        for (int i = 0; i < unParsed.length(); i++) {
            try {
                current = getSingleCourse(unParsed.getJSONObject(i));
            } catch (JSONException e) {
                current = FAILED_COURSE;
                Log.e(TAG, "parseAllCourses: ", e);
                Log.i(TAG, "parseAllCourses: failed to load a course due to JSONException");
            }

            if(current.equals(FAILED_COURSE)) {
                Log.i(TAG, "parseAllCourses: failed to parse course " + i);
            } else {
                parsed.add(current);
            }
        }

        if(instituteFilter){
            return getCoursesWithInstituteFilter(parsed,context);
        }

        return parsed;
    }

    /**
     * Parses all announcements
     * Used when all the announcements are requested
     * @param unParsed JSONArray you get onResponse with the request
     * @return A list containing successfully parsed {@link Announcement announcements}
     */
    public static ArrayList<Announcement> parseAllAnnouncements(JSONArray unParsed) {
        ArrayList<Announcement> parsed = new ArrayList<>();

        Announcement current;
        for (int i = 0; i < unParsed.length(); i++) {
            try {
                current = getSingleAnnouncement(unParsed.getJSONObject(i));
            } catch (JSONException e) {
                current = FAILED_ANNOUNCEMENT;
                Log.e(TAG, "parseAllAnnouncements: ", e);
                Log.i(TAG, "parseAllAnnouncements: failed to parse an announcement due to JSONException");
            }

            if (current.equals(FAILED_ANNOUNCEMENT)) {
                Log.i(TAG, "parseAllAnnouncements: failed to parse announcement " + i);
            } else {
                parsed.add(current);
            }
        }

        return parsed;
    }

    /**
     * Parses a recipient
     * @param obj JSONObject containing a recipient
     * @return if successful: a {@link Recipient recipient}
     * @return if not successful: a {@link Recipient recipient} with the format {@link #FAILED_RECIPIENT}
     */
    private static Recipient parseOneRecipient(JSONObject obj) {
        try {
            String id = obj.getString("id");
            String name = obj.getString("name");

            return new Recipient(id, name);
        } catch (JSONException e) {
            Log.e(TAG, "parseOneRecipient: ", e);
            Log.i(TAG, "parseOneRecipient: failed to load one recipient");
        }

        return FAILED_RECIPIENT;
    }


    /**
     * Parses the last message
     * @param obj JSONObject containing a {@link Conversation conversation}
     * @return if successful: a {@link Conversation conversation}
     * @return if not successful: a {@link Conversation conversation} with the format {@link #FAILED_CONVERSATION}
     */
    private static Conversation getLastMessage(JSONObject obj) {
        try {
            String id = obj.getString("id");
            String subject = obj.getString("subject");
            ArrayList<Participant> participants = getParticipants(obj.getJSONArray("participants"));
            String lastMessage;
            if (obj.has("last_message")) {
                lastMessage = obj.getString("last_message");
            } else {
                //TODO Don't hardcode!
                lastMessage = "Kunne ikke laste melding";
            }

            return new Conversation(id, subject, participants, lastMessage);
        } catch (JSONException e) {
            Log.e(TAG, "getLastMessage: ", e);
            Log.i(TAG, "getLastMessage: failed to parse lastMessage due to JSONException");
        }

        return FAILED_CONVERSATION;
    }

    /**
     * Parses a list of messages
     * @param unParsed JSONArray containing a list of messages
     * @param participants List of participants for the conversation
     * @return a list containing of successfully parsed {@link Message messages}
     */
    public static ArrayList<Message> getMessages(JSONArray unParsed, ArrayList<Participant> participants) {
        ArrayList<Message> parsed = new ArrayList<>();

        Message current;
        for (int i = 0; i < unParsed.length(); i++) {
            try {
                current = getSingleMessage(unParsed.getJSONObject(i), participants);
            } catch (JSONException e) {
                current = FAILED_MESSAGE;
                Log.e(TAG, "getMessages: ", e);
                Log.i(TAG, "getMessages: failed to get message due to JSONException");
            }

            if(current.equals(FAILED_MESSAGE)) {
                Log.i(TAG, "getMessages: failed to load message " + i);
            } else {
                parsed.add(current);
            }
        }

        return parsed;
    }

    /**
     * Parses a single message
     * @param obj JSONObject containing a message
     * @param participants Participant list from conversation
     * @return if successful: a {@link Message message}
     * @return if not successful: a {@link Message message} with the format {@link #FAILED_MESSAGE}
     */
    private static Message getSingleMessage(JSONObject obj, ArrayList<Participant> participants) {
        try {
            String author = "";
            String authorID = obj.getString("author_id");
            String date = obj.getString("created_at");
            String message = obj.getString("body");

            for (Participant p : participants) {
                if (p.getId().equals(authorID)) {
                    author = p.getName();
                }
            }
            return new Message(authorID, date, message, author);
        } catch (JSONException e) {
            Log.e(TAG, "getSingleMessage: ", e);
            Log.i(TAG, "getSingleMessage: failed to load a message");
        }

        return FAILED_MESSAGE;
    }

    /**
     * Gets a list of participants
     * @param unParsed JSONArray Containing a list of participants
     * @return a list of successfully parsed participants
     */
    public static ArrayList<Participant> getParticipants(JSONArray unParsed) {
        ArrayList<Participant> participants = new ArrayList<>();

        Participant current;
        for (int i = 0; i < unParsed.length(); i++) {
            try {
                current = getSingleParticipant(unParsed.getJSONObject(i));
            } catch (JSONException e) {
                current = FAILED_PARTICIPANT;
                Log.e(TAG, "getParticipants: ", e);
                Log.i(TAG, "getParticipants: failed to load a participant due to JSONException");
            }

            if(current.equals(FAILED_PARTICIPANT)) {
                Log.i(TAG, "getParticipants: failed to load participant " + i);
            } else {
                participants.add(current);
            }
        }

        return participants;
    }

    /**
     * Parses a single participant
     * @param obj JSONObject containing a single participant
     * @return if successful: a {@link User user}
     * @return if not successful: a {@link User user} with the format {@link #FAILED_PARTICIPANT}
     */
    private static Participant getSingleParticipant(JSONObject obj) {
        try {
            String id = obj.getString("id");
            String name = obj.getString("name");
            return new Participant(id, name);
        } catch (JSONException e) {
            Log.e(TAG, "getSingleParticipant: ", e);
            Log.i(TAG, "getSingleParticipant: failed to load single participant");
        }

        return FAILED_PARTICIPANT;
    }

    /**
     * Parses a single announcement
     * @param obj JSONObject containing a single {@link Announcement announcement}
     * @return if successful: an {@link Announcement announcement}
     * @return if not successful: an {@link Announcement announcement} with the format {@link #FAILED_ANNOUNCEMENT}
     */
    private static Announcement getSingleAnnouncement(JSONObject obj) {
        try {
            String id = obj.getString("id");
            String title = obj.getString("title");
            String userName = obj.getString("user_name");
            String postedAt;
            if(obj.has("posted_at")) {
                postedAt = obj.getString("posted_at");
            } else {
                postedAt = "";
            }
            String message = obj.getString("message");
            boolean unread;
            if (obj.has("read_state")) {
                unread = obj.getString("read_state").equals("true");
            } else {
                unread = false;
            }

            return new Announcement(id, title, userName, postedAt, message, unread);
        } catch(JSONException e) {
            Log.e(TAG, "getSingleAnnouncement: ", e);
            Log.i(TAG, "getSingleAnnouncement: failed to load single announcement");
        }

        return FAILED_ANNOUNCEMENT;
    }

    /**
     * Parses a single course
     * @param obj one {@link Course course}
     * @return if successful: a {@link Course course}
     * @return if not successful: {@link Course course} with the format {@link #FAILED_COURSE}
     */
    private static Course getSingleCourse(JSONObject obj) {
        try {
            int id = obj.getInt("id");
            String name = obj.getString("name");
            String cal = obj.getJSONObject("calendar").getString("ics");
            String code = obj.getString("course_code");

            return new Course(id, name, cal, code);

        } catch (JSONException e) {
            Log.e(TAG, "getSingleCourse: ", e);
            Log.i(TAG, "getSingleCourse: failed to get course");
        }

        return FAILED_COURSE;
    }

    /**
     * Parsing one calendar event
     * @param obj one {@link CalendarEvent calendar event}
     * @return if successful: a {@link CalendarEvent calendarEvent}
     * @return if not successful: a {@link CalendarEvent calendarEvent} with the format {@link #FAILED_CALENDAR_EVENT}
     */
    private static CalendarEvent parseOneCalendarEvent(JSONObject obj) {
        try {
            String title = obj.getString("title");
            String location;
            if (obj.has("location_name")) {
                location = obj.getString("location_name");
            } else {
                location = "";
            }
            String start = obj.getString("start_at");
            String stop = obj.getString("end_at");

            TimeZone timeZone = TimeZone.getTimeZone("UTC");

            return new CalendarEvent(title, parseDateString(start), parseDateString(stop), location, timeZone);

        } catch (JSONException e) {
            Log.e(TAG, "parseOneCalendarEvent: ", e);
            Log.i(TAG, "parseOneCalendarEvent: failed to load calendarEvent");
        }

        return CalendarEvent.getFailedCalendarEvent();
    }

    /**
     * Parses the number of unread messages
     * @param obj JSONObject from request
     * @return if successful: number of unread messages
     * @return if not successful: -1
     */
    public static int parseUnreadCount(JSONObject obj) {
        try {
            return obj.getInt("unread_count");
        } catch (JSONException e) {
            Log.e(TAG, "parseUnreadCount: ", e);
            Log.i(TAG, "parseUnreadCount: " + "failed to get unreadCount");
        }
        return -1;
    }
    private static DateTime parseDateString(String date){
        int year, month, day, hour, min = 0;

        try {
            year = Integer.parseInt(date.substring(0, 4));
            month = Integer.parseInt(date.substring(5, 7));
            day = Integer.parseInt(date.substring(8, 10));
            hour = Integer.parseInt(date.substring(11, 13));
            min = Integer.parseInt(date.substring(14, 16));
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
            year = 1970;
            month = 1;
            day = 1;
            hour = 0;
            min = 0;
        }
        // TODO can use new DateTime(date)

        return new DateTime(year, month, day, hour, min, 0, 0);
    }


    private static ArrayList<Course> getCoursesWithInstituteFilter(ArrayList<Course> courses, Context context) throws FileNotFoundException {
        courseBank = new CourseBank(context);
        List<String> mLines = courseBank.readLine("Courses_without_number.txt");
        ArrayList<Course> coursesWithFilter = new ArrayList<>();

        //Checking for number inn course code
        for (Course c : courses) {
            if (c.getCourseCode().matches("[a-zA-Z ]*\\d+.*")) {
                coursesWithFilter.add(c);
            } else {
                //Checking for match in text file
                for (String s : mLines) {
                    if (c.getCourseCode().equals(s)) {
                        coursesWithFilter.add(c);
                    }
                }
            }
        }

        return coursesWithFilter;
    }
}
