package no.mofifo.imber.course;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.mofifo.imber.R;
import no.mofifo.imber.card_view_holders.CourseViewHolder;
import no.mofifo.imber.card_view_holders.GeneralViewHolder;
import no.mofifo.imber.models.Course;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView which will hold the cards with titles of the different courses.
 * Responsible for deciding what elements are shown in the RecyclerView, and filling them with information.
 *
 * Created by André on 12.02.2016.
 */
public class CoursesAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private ArrayList<Course> courses;

    public CoursesAdapter(ArrayList<Course> courses) {
        this.courses = courses;
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