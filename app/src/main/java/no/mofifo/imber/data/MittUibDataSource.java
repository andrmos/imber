package no.mofifo.imber.data;

import java.util.List;

import no.mofifo.imber.models.Course;

/**
 * Created by andre on 07.04.17.
 */

public interface MittUibDataSource {

    interface Callback<T> {

        void onSuccess(T result);

        void onFailure();
    }

    void getFavoriteCourses(Callback<List<Course>> callback);
}