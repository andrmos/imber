package no.mofifo.imber.courseDetail;

import java.util.List;

import no.mofifo.imber.models.Announcement;

/**
 * Created by andre on 19.10.17.
 */

public interface CourseDetailView {

    void showLoading();

    void hideLoading();

    void displayAnnouncements(List<Announcement> announcements);

    void displayAnnouncementsError();

    boolean isAdded();

    void setTitle(String title);

    void initRecyclerView();

}
