package no.mofifo.imber.courses;

import java.util.List;

import javax.inject.Inject;

import no.mofifo.imber.data.MittUibDataSource;
import no.mofifo.imber.models.Course;

/**
 * Created by andre on 08.04.17.
 */

class CoursesPresenter {

    private CoursesView view;
    private MittUibDataSource repository;

    @Inject
    CoursesPresenter(CoursesView view, MittUibDataSource repository) {
        this.view = view;
        this.repository = repository;
    }

    void loadFavoriteCourses() {
        view.showLoading();
        repository.loadFavoriteCourses(new MittUibDataSource.Callback<List<Course>>() {
            @Override
            public void onSuccess(List<Course> result) {
                if (view.isAdded()) {
                    view.displayCourses(result);
                    view.hideLoading();
                }
            }

            @Override
            public void onFailure() {
                if (view.isAdded()) {
                    view.displayCoursesError();
                    view.hideLoading();
                }
            }
        });
    }

    void onCourseClicked(Course course) {
        view.showCourseDetails(course);
    }

    void loadMore() {

    }
}
