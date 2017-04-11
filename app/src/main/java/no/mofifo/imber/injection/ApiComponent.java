package no.mofifo.imber.injection;

import javax.inject.Singleton;

import dagger.Component;
import no.mofifo.imber.course.CoursesFragment;

/**
 * Dagger component defining which classes have access to the different dependencies in the modules.
 * To inject a reference to a dependency into a class, the class has to be added to this component with an inject method.
 * Created by andre on 10.04.17.
 */
@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface ApiComponent {

    void inject(CoursesFragment coursesFragment);

}
