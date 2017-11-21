package no.mofifo.imber.courseDetail;

import dagger.Module;
import dagger.Provides;
import no.mofifo.imber.injection.FragmentScoped;

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
}
