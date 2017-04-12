package no.mofifo.imber.course;

import java.util.List;

import no.mofifo.imber.models.Course;

/**
 * Created by andre on 08.04.17.
 */

public interface CoursesView {

    /**
     * Displays a list of courses in the views recycler view.
     * @param courses The courses to be displayed.
     */
    void displayCourses(List<Course> courses);
}
