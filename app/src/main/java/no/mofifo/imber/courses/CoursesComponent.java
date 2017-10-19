package no.mofifo.imber.courses;

import dagger.Component;
import no.mofifo.imber.injection.ApiComponent;
import no.mofifo.imber.injection.FragmentScoped;

/**
 * Created by andre on 12.04.17.
 */
@FragmentScoped
@Component(dependencies = ApiComponent.class, modules = CoursesPresenterModule.class)
public interface CoursesComponent {

    void inject(CoursesFragment coursesFragment);

}
