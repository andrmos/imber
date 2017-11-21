package no.mofifo.imber.courseDetail;

import android.os.Bundle;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import no.mofifo.imber.data.MittUibDataSource;
import no.mofifo.imber.models.Announcement;
import no.mofifo.imber.models.CalendarEvent;
import no.mofifo.imber.models.Course;

/**
 * Created by andre on 19.10.17.
 */

class CourseDetailPresenter {

    private CourseDetailView view;
    private MittUibDataSource repository;
    private Course course;

    @Inject
    CourseDetailPresenter(CourseDetailView view, MittUibDataSource repository) {
        this.view = view;
        this.repository = repository;
    }


    void initialize(Bundle arguments) {
        if (arguments != null) {
            if (arguments.containsKey("course")) {
                String json = arguments.getString("course");
                course = new Gson().fromJson(json, Course.class);
            }
        }
    }

    void onCreateView() {
        view.setTitle(course.getTrimmedName());
        this.loadAnnouncements();
        this.loadEvents();
    }

    void loadAnnouncements() {
        // TODO: show when all is loaded?
        view.showLoading();
        repository.loadAnnouncements(course.getId(), new MittUibDataSource.Callback<List<Announcement>>() {
            @Override
            public void onSuccess(List<Announcement> result) {
                if (view.isAdded()) {
                    ArrayList<String> announcementTitles = toNonNullAnnouncementTitles(result);
                    view.displayAnnouncements(announcementTitles);
                    view.hideLoading();
                }
            }

            @Override
            public void onFailure() {
                if (view.isAdded()) {
                    view.displayAnnouncementsError();
                    view.hideLoading();
                }
            }
        });
    }

    void showAnnouncements() {
        view.showAnnouncementsUi(course.getId(), course.getCourseCode());
    }

    void loadEvents() {
        view.showLoading();

        //Course ids for context_codes in url
        ArrayList<String> contextCodes = new ArrayList<>();
        contextCodes.add("course_" + course.getId());

        //Type - event/assignment
        String type = "event";

        //Only 3 agendas for one course
        int perPage = 3;

        //Get today's date in right format
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date today = Calendar.getInstance().getTime();
        String startDate = df.format(today);
        String endDate = df.format(today);

        repository.loadEvents(startDate, endDate, contextCodes, null, type, perPage, new MittUibDataSource.Callback<List<CalendarEvent>>() {
            @Override
            public void onSuccess(List<CalendarEvent> result) {
                if (view.isAdded()) {
                    // TODO: 21.11.17 If result is empty
                    ArrayList<String> eventTitles = toNonNullEventTitles(result);
                    view.displayEvents(eventTitles);
                    view.hideLoading();
                }
            }

            @Override
            public void onFailure() {
                if (view.isAdded()) {
                    view.displayEventsError();
                }
            }
        });
    }

    private ArrayList<String> toNonNullAnnouncementTitles(List<Announcement> announcements) {
        ArrayList<String> announcementTitles = new ArrayList<>();
        announcementTitles.add("");
        announcementTitles.add("");
        announcementTitles.add("");
        for (int i = 0; i < announcements.size() && i < 3; i++) {
            announcementTitles.set(i, announcements.get(i).getTitle());
        }
        return announcementTitles;
    }

    private ArrayList<String> toNonNullEventTitles(List<CalendarEvent> events) {
        ArrayList<String> eventTitles = new ArrayList<>();
        eventTitles.add("");
        eventTitles.add("");
        eventTitles.add("");
        for (int i = 0; i < events.size() && i < 3; i++) {
            eventTitles.set(i, events.get(i).getTitle());
        }
        return eventTitles;
    }

    void showFiles() {
        view.showFilesUi(course);
    }
}
