package no.mofifo.imber.data;

import java.util.List;

import hirondelle.date4j.DateTime;
import no.mofifo.imber.models.Announcement;
import no.mofifo.imber.models.CalendarEvent;
import no.mofifo.imber.models.Course;
import no.mofifo.imber.retrofit.MittUibClient;

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

    void loadEvents(String startDate,
                    String endDate,
                    List<String> contextCodes,
                    List<String> excludes,
                    String eventType,
                    int perPage,
                    Callback<List<CalendarEvent>> callback);

}
