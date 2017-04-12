package no.mofifo.imber.course;

import no.mofifo.imber.data.MittUibDataSource;

/**
 * Created by andre on 08.04.17.
 */

public class CoursesPresenter {

    private CoursesView view;
    private MittUibDataSource repository;

    public CoursesPresenter(CoursesView view, MittUibDataSource repository) {
        this.view = view;
        this.repository = repository;
    }
}
