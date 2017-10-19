package no.mofifo.imber.injection;

import javax.inject.Singleton;

import dagger.Component;
import no.mofifo.imber.courseDetail.CourseDetailPresenterModule;
import no.mofifo.imber.courses.CoursesPresenterModule;
import no.mofifo.imber.data.MittUibDataSource;

/**
 * Dagger component defining which classes have access to the different dependencies in the modules.
 * To inject a reference to a dependency into a class, the class has to be added to this component with an inject method.
 * Created by andre on 10.04.17.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class, CoursesPresenterModule.class, CourseDetailPresenterModule.class})
public interface ApiComponent {

    // MittUibDataSource is used in provider in CoursesComponent, so it has to be declared here.
    MittUibDataSource mittUibDataSource();

}
