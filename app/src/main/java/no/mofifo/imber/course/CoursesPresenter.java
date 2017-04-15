package no.mofifo.imber.course;

import java.util.List;

import javax.inject.Inject;

import no.mofifo.imber.data.MittUibDataSource;
import no.mofifo.imber.models.Course;

/**
 * Created by andre on 08.04.17.
 */

public class CoursesPresenter {

    private CoursesView view;
    private MittUibDataSource repository;

    @Inject
    public CoursesPresenter(CoursesView view, MittUibDataSource repository) {
        this.view = view;
        this.repository = repository;
    }

    public void loadFavoriteCourses() {
        view.showLoading();
        repository.getFavoriteCourses(new MittUibDataSource.Callback<List<Course>>() {
            @Override
            public void onSuccess(List<Course> result) {
                view.displayCourses(result);
                view.hideLoading();
            }

            @Override
            public void onFailure() {
                view.displayCoursesError();
                view.hideLoading();
            }
        });
    }

    public void onCourseClicked(Course course) {
        view.showCourseDetails(course);
    }
}
