package no.mofifo.imber.courseDetail;

import java.util.List;

import no.mofifo.imber.models.Announcement;
import no.mofifo.imber.models.CalendarEvent;

/**
 * Created by andre on 19.10.17.
 */

interface CourseDetailView {

    void showLoading();

    void hideLoading();

    void displayAnnouncements(List<Announcement> announcements);

    void displayAnnouncementsError();

    boolean isAdded();

    void setTitle(String title);

    void initRecyclerView();

    void displayEvents(List<CalendarEvent> events);

    void displayEventsError();

}
