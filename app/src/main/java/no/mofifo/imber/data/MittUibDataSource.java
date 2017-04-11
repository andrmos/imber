package no.mofifo.imber.data;

import no.mofifo.imber.models.Course;

/**
 * Created by andre on 07.04.17.
 */

public interface MittUibDataSource {

    interface Callback<T> {

        void onSuccess(T result);

        void onFailure();
    }

    void getCourses(Callback<Course> callback);
}
