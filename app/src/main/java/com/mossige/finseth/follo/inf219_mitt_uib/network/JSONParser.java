package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.courseBank.CourseBank;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Message;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Participant;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.models.RecipientGroup;
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

    public JSONParser() {}

    /**
     * Parses all the recipients within a group
     * @param unParsed JSONArray you get onResponse with the request
     * @return an ArrayList containing all the {@link Recipient recipients}
     * @throws JSONException
     */
    public static ArrayList<Recipient> parseAllRecipients(JSONArray unParsed) throws JSONException {
        ArrayList<Recipient> parsed = new ArrayList<>();

        for (int i = 0; i < unParsed.length(); i++) {
            JSONObject obj = unParsed.getJSONObject(i);
            parsed.add(parseOneRecipient(obj));
        }

        return parsed;
    }

    /**
     * Parsing all calendar events from unparsed param
     * @param unparsed
     * @return
     * @throws JSONException
     */
    public static ArrayList<CalendarEvent> parseAllCalendarEvents(JSONArray unparsed) throws JSONException {
        ArrayList<CalendarEvent> parsed = new ArrayList<>();

        for(int i = 0; i < unparsed.length(); i++){
            JSONObject obj = unparsed.getJSONObject(i);

            if (obj.has("hidden")) {
                boolean hidden = obj.getBoolean("hidden");
                if (!hidden) {
                    parsed.add(parseOneCalendarEvent(obj));
                } else {
                    // Do not add events that are hidden from the calendar
                }
            } else {
                parsed.add(parseOneCalendarEvent(obj));
            }

        }

        return parsed;
    }

    /**
     * Parses the different recipient groups
     * @param unParsed JSONArray you get onResponse with the request
     * @return An ArrayList containing the different {@link RecipientGroup recipient groups}
     * @throws JSONException
     */
    public static ArrayList<RecipientGroup> parseAllRecipientGroups(JSONArray unParsed) throws JSONException {
        ArrayList<RecipientGroup> parsed = new ArrayList<>();

        for (int i = 0; i < unParsed.length(); i++) {
            JSONObject obj = unParsed.getJSONObject(i);

            RecipientGroup recipientGroup = parseOneRecipientsGroup(obj);
            if (recipientGroup != null) {
                parsed.add(recipientGroup);
            }
        }

        return parsed;
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
        ArrayList<Message> messages = getMessages(unParsed.getJSONArray("messages"),participants);

        return new Conversation(id, subject, participants, messages);
    }

    /**
     * Parses all courses
     * Used when all the courses are requested
     * @param unParsed JSONArray you get onResponse with the request
     * @return An ArrayList containing all the users {@link Course courses}
     * @throws JSONException
     */
    public static ArrayList<Course> parseAllCourses(JSONArray unParsed, boolean instituteFilter) throws JSONException, FileNotFoundException {

        ArrayList<Course> parsed = new ArrayList<>();

        for (int i = 0; i < unParsed.length(); i++) {
            parsed.add(getSingleCourse(unParsed.getJSONObject(i)));
        }

        if(instituteFilter){
            return getCoursesWithInstituteFilter(parsed);
        }

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

    private static Recipient parseOneRecipient(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String name = obj.getString("name");

        return new Recipient(id, name);
    }

    private static RecipientGroup parseOneRecipientsGroup(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String name = obj.getString("name");

        // Only parse chosen groups
        if (name.equals("Forelesere") || name.equals("Læringsassistenter") || name.equals("Studenter")) {
            String size = obj.getString("user_count");
            return new RecipientGroup(id, name, size);
        } else {
            return null;
        }
    }

    private static Conversation getLastMessage(JSONObject obj) throws JSONException {
        String id = obj.getString("id");
        String subject = obj.getString("subject");
        ArrayList<Participant> participants = getParticipants(obj.getJSONArray("participants"));
        String lastMessage = obj.getString("last_message");

        return new Conversation(id,subject, participants, lastMessage);
    }

    private static ArrayList<Message> getMessages(JSONArray unParsed, ArrayList<Participant> participants) throws JSONException {
        ArrayList<Message> parsed = new ArrayList<>();

        for (int i = 0; i < unParsed.length(); i++) {
            parsed.add(getSingleMessage(unParsed.getJSONObject(i),participants));
        }

        return parsed;
    }

    private static Message getSingleMessage(JSONObject obj, ArrayList<Participant> participants) throws JSONException {

        String author = "";
        String authorID = obj.getString("author_id");
        String date = obj.getString("created_at");
        String message = obj.getString("body");

        for(Participant p : participants){
            if(p.getId().equals(authorID)){
                author = p.getName();
            }
        }
        return new Message(authorID, date, message, author);
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

    /**
     * Parsing one calendar event
     * @param obj
     * @return
     * @throws JSONException
     */
    private static CalendarEvent parseOneCalendarEvent(JSONObject obj) throws JSONException {
        String title = obj.getString("title");
        String location = obj.getString("location_name");
        String start = obj.getString("start_at");
        String stop = obj.getString("end_at");

        TimeZone timeZone = TimeZone.getTimeZone("UTC");

        return new CalendarEvent(title, parseDateString(start), parseDateString(stop), location, timeZone);
    }

    public static int parseUnreadCount(JSONObject obj) throws JSONException{
        return obj.getInt("unread_count");
    }
    private static DateTime parseDateString(String date){
        int year = Integer.parseInt(date.substring(0,4));
        int month = Integer.parseInt(date.substring(5,7));
        int day = Integer.parseInt(date.substring(8,10));
        int hour = Integer.parseInt(date.substring(11,13));
        int min = Integer.parseInt(date.substring(14,16));
        // TODO can use new DateTime(date)

        return new DateTime(year, month, day, hour, min, 0, 0);
    }


    private static ArrayList<Course> getCoursesWithInstituteFilter(ArrayList<Course> courses) throws FileNotFoundException {
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
