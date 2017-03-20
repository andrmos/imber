package com.mossige.finseth.follo.inf219_mitt_uib.retrofit;

import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.AssignmentEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.File;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Folder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.models.SendMessage;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    @GET("courses?per_page=1000")
    Call<List<Course>> getCourses(@Query("include[]") List<String> includes,
                                  @Query("enrollment_state") String enrollmentState);

    /**
     * @return The favorite active courses of the logged in user.
     */
    @GET("users/self/favorites/courses")
    Call<List<Course>> getFavoriteCourses();

    /**
     * Add course to favorite list.
     * @return
     */
    @POST("users/self/favorites/courses/{id}")
    Call<Course> addFavortiteCourse(@Path("id") String courseId);

    /**
     * Delete course from favorite list.
     * @return
     */
    @DELETE("users/self/favorites/courses/{id}")
    Call<Course> removeFavortiteCourse(@Path("id") String courseId);

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
     * Retrieve the list of calendar events for the current user.
     *
     * @param startDate    Include events since this date, inclusive. Format: YYYY-MM-DD
     * @param end_date     Include events before this date, inclusive. Format: YYYY-MM-DD
     * @param contextCodes Array of context codes of courses/groups/users you want. Format ex. "course_<code>"
     * @param excludes     Array of attributes to exclude. “description”, “child_events” or “assignment”
     * @param type         Type of event, "event" or "assignment"
     * @return
     */
    @GET("calendar_events")
    Call<List<CalendarEvent>> getEvents(@Query("start_date") String startDate,
                                        @Query("end_date") String end_date,
                                        @Query("context_codes[]") List<String> contextCodes,
                                        @Query("excludes[]") List<String> excludes,
                                        @Query("type") String type,
                                        @Query("per_page") Integer perPage);


    @GET
    Call<List<CalendarEvent>> getEventsPaginate(@Url String url);

    /**
     * Retrieve the list of assignment events for the current user.
     *
     * @param startDate Include events since this date, inclusive. Format: YYYY-MM-DD
     * @param end_date Include events before this date, inclusive. Format: YYYY-MM-DD
     * @param contextCodes Array of context codes of courses/groups/users you want. Format ex. "course_<code>"
     * @param excludes Array of attributes to exclude. “description”, “child_events” or “assignment”
     * @return
     */
    @GET("calendar_events?per_page=50&type=\"assignment\"")
    Call<List<AssignmentEvent>> getAssignmentEvents(@Query("start_date") String startDate,
                                                    @Query("end_date") String end_date,
                                                    @Query("context_codes[]") List<String> contextCodes,
                                                    @Query("excludes[]") List<String> excludes,
                                                    @Query("page_num") Integer pageNum); // TODO remove page num

    /**
     * Retrieve valid recipients (users, courses and groups) that the current user can send messages to.
     *
     * @param search The search terms, seperated via whitespace. Should be encoded.
     * @param context The context of the search. Ex: "course_<code>"
     * @return
     */
    @GET("search/recipients?per_page=10&permissions[]=send_messages_all&type=user")
    Call<List<Recipient>> getRecipients(@Query("search") String search,
                                        @Query("context") String context);

    @GET
    Call<List<Recipient>> getRecipientsPagination(@Url String url);


    /**
     * @return The number of unread conversations for the current user.
     */
    @GET("conversations/unread_count")
    Call<JSONObject> getUnreadCount();

    /**
     * Returns a list of files for the given course.
     *
     * @param courseId  The specified course.
     * @param only      Array of information to restrict to. Ex: 'names'.
     * @return
     */
    @GET("courses/{id}/files")
    Call<List<File>> getFiles(@Path("id") int courseId,
                              @Query("only[]") List<String> only);

    /**
     * Returns a list of files in the given folder.
     *
     * @param folderId  The specified folder.
     * @param only      Array of information to restrict to. Ex: 'names'.
     * @return
     */
    @GET("folders/{id}/files")
    Call<List<File>> getFilesByFolder(@Path("id") int folderId,
                                      @Query("only[]") List<String> only);

    /**
     * Returns the details for a file.
     *
     * @param fileId The given file.
     * @return
     */
    @GET("files/{id}")
    Call<File> getFile(@Path("id") int fileId);

    @GET
    Call<List<File>> getFilesPaginate(@Url String url);

    @GET("folders/{id}/folders")
    Call<List<Folder>> getFolders(@Path("id") int folderId);

    /**
     * Returns a list of all folders for the given course.
     *
     * @param courseId  The given course id.
     * @param only      Array of information to restrict to. Ex: 'names'.
     * @return
     */
    @GET("courses/{id}/folders")
    Call<List<Folder>> getFoldersByCourse(@Path("id") int courseId,
                                          @Query("only[]") List<String> only);

    /**
     * Returns the details for a folder.
     *
     * @param folderId The given folder.
     * @return
     */
    @GET("folders/{id}")
    Call<Folder> getFolder(@Path("id") int folderId);

    /**
     * Returns the details for the root folder in a given course.
     *
     * @param courseId The given course.
     * @return
     */
    @GET("courses/{id}/folders/root")
    Call<Folder> getRootFolder(@Path("id") int courseId);

    @GET
    Call<List<Folder>> getFoldersPaginate(@Url String url);
}
