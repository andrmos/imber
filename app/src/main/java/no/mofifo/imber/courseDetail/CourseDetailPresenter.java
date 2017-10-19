package no.mofifo.imber.courseDetail;

import javax.inject.Inject;

import no.mofifo.imber.data.MittUibDataSource;

/**
 * Created by andre on 19.10.17.
 */

public class CourseDetailPresenter {

    private CourseDetailView view;
    private MittUibDataSource repository;

    @Inject
    public CourseDetailPresenter(CourseDetailView view, MittUibDataSource repository) {
        this.view = view;
        this.repository = repository;
    }
}
