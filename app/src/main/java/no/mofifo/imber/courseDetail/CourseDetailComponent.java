package no.mofifo.imber.courseDetail;

import dagger.Component;
import no.mofifo.imber.injection.ApiComponent;
import no.mofifo.imber.injection.FragmentScoped;

/**
 * Created by andre on 12.04.17.
 */
@FragmentScoped
@Component(dependencies = ApiComponent.class, modules = CourseDetailPresenterModule.class)
public interface CourseDetailComponent {

    void inject(CourseDetailFragment courseDetailFragment);

}
