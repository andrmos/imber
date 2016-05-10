package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Andre on 19/02/2016.
 *
 * Helper class for getting the URL endpoints for the Canvas API.
 */
public class UrlEndpoints {

    public static final String TAG = "URLEndpoints";

    // Parameters
    public static final String ACCESS_TOKEN_KEY = "access_token=";
    public static final String ONLY_ANNOUNCEMENTS_KEY = "only_announcements=";

    // Endpoint parts
    public static final String BASE_URL = "https://mitt.uib.no/api/v1/";
    public static final String COURSES = "courses/";
    public static final String CONVERSATIONS = "conversations/";
    public static final String DISCUSSION_TOPICS = "discussion_topics";
    public static final String USERS = "users/";
    public static final String ENROLLMENTS = "enrollments";
    public static final String SELF = "self";
    public static final String PROFILE = "profile";
    public static final String SEARCH = "search/";
    public static final String RECIPIENTS = "recipients";
    // Sending message arguments
    public static final String SEARCH_ARG = "search=";

    public static final String PER_PAGE = "per_page=";
    public static final String PERMISSIONS = "permissions[]=send_messages_all";
    public static final String CONTEXT = "context=";
    public static final String COURSE_PREFIX = "course_";
    public static final String AND = "&";
    public static final String CALENDAR_EVENTS = "calendar_events";
    public static final String CONTEXT_CODE_KEY = "context_codes[]=";
    public static final String EXCLUDES_KEY = "excludes[]=";
    public static final String ALL_EVENTS_KEY = "all_events=";
    public static final String START_DATE_KEY = "start_date=";
    public static final String END_DATE_KEY = "end_date=";
    public static final String TYPE_KEY = "type=";
    public static final String PAGE = "page=";
    public static final String UNREAD_COUNT = "unread_count";
    public static final String TYPE = "type=";

    public static String getRecipients(String search, int courseId) {
        if (search == null) {
            search = "";
        } else {
            search = getHex(search);
        }

        return BASE_URL + SEARCH + RECIPIENTS + "?" + SEARCH_ARG + search + AND + PER_PAGE  + "50" + AND + PERMISSIONS + AND + CONTEXT + COURSE_PREFIX + courseId + AND + TYPE + "user" + AND + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    /**
     * @return Request URL for getting the users profile
     */
    public static String getUserProfileURL() {
        return BASE_URL + USERS + SELF + "/" + PROFILE + "?" + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    /**
     * @return Request URL for a getting list of active courses for current user.
     */
    public static String getCoursesListUrl() {
        return BASE_URL + COURSES + "?" + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    /**
     * @return Request URL for getting a list of conversations, with last sent messages only, for current user.
     */
    public static String getConversationsUrl() {
        return BASE_URL + CONVERSATIONS + "?" + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    /**
     * @param conversation_id The id of the conversation.
     * @return Request URL for getting a single conversation with all its messages.
     */
    public static String getSingleConversationUrl(String conversation_id) {
        return BASE_URL + CONVERSATIONS + conversation_id + "?" + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    /**
     * @param course_id The id of the course.
     * @return Request URL for getting a list of announcements for a course.
     */
    public static String getCourseAnnouncementsUrl(String course_id) {
        return BASE_URL + COURSES + course_id + "/" + DISCUSSION_TOPICS + "?" + ONLY_ANNOUNCEMENTS_KEY + "true" +
                "&" + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    /**
     * @param user_id The id of the user.
     * @return Request URL for getting grades for a user.
     */
    public static String getUserGradesUrl(String user_id) {
        return BASE_URL + USERS + user_id + "/" + ENROLLMENTS + "?" + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    /**
     * @return Request URL posting new message.
     */
    public static String postNewMessageUrl() {
        return BASE_URL + CONVERSATIONS + "?" + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    /**
     * Return url for getting calendar events url.
     * @param context_codes Code of courses/groups to include. In format: "course_330" or "group_42"
     * @param excludes Exlude information
     * @param type Events or Assignments
     * @param start_date Format: YYYY-MM-DD
     * @param end_date Format: YYYY-MM-DD
     * @return
     */
    public static String getCalendarEventsUrl(ArrayList<String> context_codes, ArrayList<String> excludes, String type, String start_date, String end_date, String per_page, int page_num) {
        String url = BASE_URL + CALENDAR_EVENTS + "?" + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN + "&";

        for (String context_code : context_codes) {
            url += CONTEXT_CODE_KEY + context_code + "&";
        }

        for (String e : excludes) {
            url += EXCLUDES_KEY + e + "&";
        }

        url += ALL_EVENTS_KEY + "false&";
        url += TYPE_KEY + type + "&";
        url += START_DATE_KEY + start_date + "&";
        url += END_DATE_KEY + end_date + "&";
        url += PER_PAGE + per_page;

        if(page_num != 1){
            url += "&" + PAGE + page_num;
        }

        return url;
    }

    /**
     * Return url for unread count
     * @return
     */
    public static String getUnreadCountURL(){
        return BASE_URL + CONVERSATIONS + UNREAD_COUNT + "?" + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    private static String getHex(String s) {

        String hex = "";

        try {
            hex = java.net.URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return hex;
    }

}
