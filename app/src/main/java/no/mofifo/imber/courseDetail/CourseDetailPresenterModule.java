package no.mofifo.imber.courseDetail;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import no.mofifo.imber.injection.FragmentScoped;
import no.mofifo.imber.models.Announcement;
import no.mofifo.imber.models.CalendarEvent;

/**
 * Dagger module defining dependencies injectable within the fragment displaying a list of courses.
 * Created by andre on 12.04.17.
 */
@Module
public class CourseDetailPresenterModule {

    private CourseDetailView view;

    public CourseDetailPresenterModule(CourseDetailView view) {
        this.view = view;
    }

    @Provides
    @FragmentScoped
    CourseDetailView provideView() {
        return view;
    }

    @Provides
    @FragmentScoped
    CourseDetailAdapter provideCourseDetailAdapter() {
        return new CourseDetailAdapter(new ArrayList<Announcement>(), new ArrayList<CalendarEvent>());
    }

}
