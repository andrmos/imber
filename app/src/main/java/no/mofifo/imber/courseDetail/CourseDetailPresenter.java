package no.mofifo.imber.courseDetail;

import android.os.Bundle;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import no.mofifo.imber.data.MittUibDataSource;
import no.mofifo.imber.models.Announcement;
import no.mofifo.imber.models.Course;

/**
 * Created by andre on 19.10.17.
 */

public class CourseDetailPresenter {

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

    void onCreateView() {
        view.setTitle(course.getTrimmedName());
        view.initRecyclerView();
        this.loadAnnouncements();
    }

    void loadEvents() {

    }

}
