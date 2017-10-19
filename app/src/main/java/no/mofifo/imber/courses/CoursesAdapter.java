package no.mofifo.imber.courses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.mofifo.imber.R;
import no.mofifo.imber.card_view_holders.CourseViewHolder;
import no.mofifo.imber.card_view_holders.GeneralViewHolder;
import no.mofifo.imber.models.Course;

import java.util.List;

/**
 * Adapter for the RecyclerView which will hold the cards with titles of the different courses.
 * Responsible for deciding what elements are shown in the RecyclerView, and filling them with information.
 *
 * Created by André on 12.02.2016.
 */
public class CoursesAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private List<Course> courses;

    public CoursesAdapter(List<Course> courses) {
        this.courses = courses;
    }

    public void replaceCourses(List<Course> data) {
        courses = data;
    }

    public void addCourses(List<Course> data) {
        int oldAmount = courses.size();
        courses.addAll(data);
        notifyItemRangeInserted(oldAmount, data.size());
    }

    public Course getCourse(int position) {
        return courses.get(position);
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // course card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card, parent, false);
        return new CourseViewHolder(v);
    }


    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        CourseViewHolder courseHolder = (CourseViewHolder) holder;
        courseHolder.course_code.setText(courses.get(position).getCourseCode());
        courseHolder.course_title.setText(courses.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}