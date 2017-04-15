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

    /**
     * Displays an error message in a snackbar with a given message.
     */
    void displayCoursesError();

    /**
     * Shows the loading indicator.
     */
    void showLoading();

    /**
     * Hides the loading indicator.
     */
    void hideLoading();

    /**
     * Changes view to CourseDetailFragment, with details about the specified course.
     * @param course The course to show details for.
     */
    void showCourseDetails(Course course);
}
