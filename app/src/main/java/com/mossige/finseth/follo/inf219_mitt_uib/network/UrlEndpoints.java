package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.print.PrinterCapabilitiesInfo;
import android.util.Log;

/**
 * Created by Andre on 19/02/2016.
 *
 * Helper class for getting the URL endpoints for the Canvas API.
 */
public class UrlEndpoints {

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
    public static final String SYNTHETIC_CONTEXTS = "synthetic_contexts=true";
    public static final String CONTEXT = "context=";
    public static final String COURSE = "course_";
    public static final String AND = "&";

    /**
     * @param search textinput (name)
     * @param courseID what course you wish to search within
     * @return Request URL for getting possible recipients
     */
    public static String getRecipients(String search, int courseID) {
        return BASE_URL + SEARCH + RECIPIENTS + "?" + SEARCH_ARG + search + AND + PER_PAGE  + AND + PERMISSIONS + AND + SYNTHETIC_CONTEXTS + AND + CONTEXT + COURSE + courseID + AND + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }
    public static String getRecipientGroups(int courseID) {
        return BASE_URL + SEARCH + RECIPIENTS + "?" + SEARCH_ARG + AND + PER_PAGE + AND + PERMISSIONS + AND + SYNTHETIC_CONTEXTS + AND + CONTEXT + COURSE + courseID + AND + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
    }

    /**
     * @param search textinput (name)
     * @param courseID which group you want to search within
     * @return Request URL for getting possible recipients
     */
    public static String getRecipientsByGroup(String search, String courseID) {
        return BASE_URL + SEARCH + RECIPIENTS + "?" + SEARCH_ARG + AND + PER_PAGE + AND + PERMISSIONS + AND + SYNTHETIC_CONTEXTS + AND + CONTEXT + courseID + AND + ACCESS_TOKEN_KEY + PrivateConstants.ACCESS_TOKEN;
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

}
