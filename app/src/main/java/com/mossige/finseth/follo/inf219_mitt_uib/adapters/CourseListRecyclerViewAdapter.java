package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.CourseViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView which will hold the cards with titles of the different courses.
 * Responsible for deciding what elements are shown in the RecyclerView, and filling them with information.
 *
 * Created by André on 12.02.2016.
 */
public class CourseListRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private ArrayList<Course> data;

    public CourseListRecyclerViewAdapter(ArrayList<Course> data) {
        this.data = data;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // course card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card, parent, false);
        GeneralViewHolder holder = new CourseViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        CourseViewHolder courseHolder = (CourseViewHolder) holder;
        courseHolder.course_code.setText(data.get(position).getCourseCode());
        courseHolder.course_title.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}