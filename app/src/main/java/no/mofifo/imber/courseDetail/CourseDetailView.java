package no.mofifo.imber.courseDetail;

import java.util.List;

import no.mofifo.imber.models.Announcement;
import no.mofifo.imber.models.CalendarEvent;
import no.mofifo.imber.models.Course;

/**
 * Created by andre on 19.10.17.
 */

interface CourseDetailView {

    void showLoading();

    void hideLoading();

    void displayAnnouncements(List<String> announcementTitles);

    void displayAnnouncementsError();

    boolean isAdded();

    void setTitle(String title);

    void displayEvents(List<String> eventTitles);

    void displayEventsError();

    void showAnnouncementsUi(int courseId, String courseCode);

    void showFilesUi(Course course);

}
