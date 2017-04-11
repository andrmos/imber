package no.mofifo.imber.course;

import no.mofifo.imber.data.MittUibDataSource;

/**
 * Created by andre on 08.04.17.
 */

public class CoursesFragmentPresenter {

    private CoursesFragmentView view;
    private MittUibDataSource repository;

    public CoursesFragmentPresenter(CoursesFragmentView view, MittUibDataSource repository) {
        this.view = view;
        this.repository = repository;
    }
}
