package com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit;

import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.models.SendMessage;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by andre on 29.12.16.
 */

public interface MittUibClient {

    /**
     * @return The logged in users profile data.
     */
    @GET("users/self/profile")
    Call<User> getProfile();

    /**
     * @return The active courses of the logged in user.
     */
    @GET("courses")
    Call<List<Course>> getCourses();

    @GET
    Call<List<Course>> getCoursesPagination(@Url String url);

    /**
     * @param courseId
     * @return The announcements for the specified course.
     */
    @GET("courses/{id}/discussion_topics?only_announcements=true")
    Call<List<Announcement>> getAnnouncements(@Path("id") int courseId);

    @GET
    Call<List<Announcement>> getAnnouncementsPagination(@Url String url);

    /**
     * @return The list of conversations for the logged in user.
     */
    @GET("conversations")
    Call<List<Conversation>> getConversations();

    @GET
    Call<List<Conversation>> getConversationsPagination(@Url String url);

    /**
     * @param conversationId
     * @return Information for the specified conversation.
     */
    @GET("conversations/{id}")
    Call<Conversation> getConversation(@Path("id") int conversationId);

    /**
     * Create a new conversation with one or more recipients.
     * @param message
     * @return
     */
    @POST("conversations")
    Call<List<SendMessage>> createConversation(@Body SendMessage message);

    /**
     * Retrieve the list of calendar events or assignments for the current user.
     *
     * @param startDate Include events since this date, inclusive. Format: YYYY-MM-DD
     * @param end_date Include events before this date, inclusive. Format: YYYY-MM-DD
     * @param contextCodes Array of context codes of courses/groups/users you want. Format ex. "course_<code>"
     * @param excludes Array of attributes to exclude. “description”, “child_events” or “assignment”
     * @param type Type of event, "event", or "assignment"
     * @return
     */
    @GET("calendar_events?per_page=50")
    Call<List<CalendarEvent>> getCalendarEvents(@Query("start_date") String startDate,
                                                @Query("end_date") String end_date,
                                                @Query("context_codes[]") List<String> contextCodes,
                                                @Query("excludes[]") List<String> excludes,
                                                @Query("type") String type,
                                                @Query("page_num") Integer pageNum);


    /**
     * // TODO Not yet used
     * @return The number of unread conversations for the current user
     */
    @GET("conversations/unread_count")
    Call<JSONObject> getUnreadCount();

}
