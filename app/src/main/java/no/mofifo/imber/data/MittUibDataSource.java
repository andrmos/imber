package no.mofifo.imber.data;

import java.util.List;

import no.mofifo.imber.models.Announcement;
import no.mofifo.imber.models.Course;

/**
 * Created by andre on 07.04.17.
 */

public interface MittUibDataSource {

    interface Callback<T> {

        void onSuccess(T result);

        void onFailure();
    }

    void loadFavoriteCourses(Callback<List<Course>> callback);

    void loadAnnouncements(int courseId, Callback<List<Announcement>> callback);

}
