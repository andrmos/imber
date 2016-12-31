package com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit;

import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
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

    /**
     * @param courseId
     * @return The announcements for the specified course.
     */
    @GET("courses/{id}/discussion_topics?only_announcements=true")
    Call<List<Announcement>> getAnnouncements(@Path("id") int courseId);

    /**
     * @return The list of conversations for the logged in user.
     */
    @GET("conversations")
    Call<List<Conversation>> getConversations();

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
     * // TODO Not yet used
     * @return The number of unread conversations for the current user
     */
    @GET("conversations/unread_count")
    Call<JSONObject> getUnreadCount();
}
