package no.mofifo.imber.course;

import dagger.Module;
import dagger.Provides;
import no.mofifo.imber.injection.FragmentScoped;

/**
 * Dagger module defining dependencies injectable within the fragment displaying a list of courses.
 * Created by andre on 12.04.17.
 */
@Module
public class CoursesPresenterModule {

    private CoursesView view;

    public CoursesPresenterModule(CoursesView view) {
        this.view = view;
    }

    @Provides
    @FragmentScoped
    CoursesView provideView() {
        return view;
    }

}
