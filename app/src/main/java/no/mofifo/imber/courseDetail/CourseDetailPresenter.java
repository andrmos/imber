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
        view.initRecyclerView();
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
                    view.displayAnnouncements(result);
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
                    ArrayList<String> eventTitles = new ArrayList<>();
                    eventTitles.add("");
                    eventTitles.add("");
                    eventTitles.add("");
                    for (int i = 0; i < result.size(); i++) {
                        eventTitles.set(i, result.get(i).getTitle());
                    }

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

}
