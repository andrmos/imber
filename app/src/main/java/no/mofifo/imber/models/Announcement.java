package no.mofifo.imber.models;

import android.text.Html;

import com.google.gson.annotations.SerializedName;

import hirondelle.date4j.DateTime;

/**
 * Created by PatrickFinseth on 16.02.16.
 */
public class Announcement {

    private int id;
    private String title;
    private String message;
    @SerializedName("posted_at")
    private DateTime postedAt;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("unread_count")
    private int unreadCount;
    private boolean locked;

    public Announcement(int id, String title, String message, DateTime postedAt, String userName, int unreadCount, boolean locked) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.postedAt = postedAt;
        this.userName = userName;
        this.unreadCount = unreadCount;
        this.locked = locked;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Returns the HTML escaped announcement message.
     */
    public String getMessageHtmlEscaped() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            //noinspection deprecation
            return Html.fromHtml(message).toString();
        }
    }

    public DateTime getPostedAt() {
        return postedAt;
    }

    public String getUserName() {
        return userName;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public boolean isLocked() {
        return locked;
    }
}
