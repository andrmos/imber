package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.AgendaViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.CourseViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView.
 * Responsible for deciding what elements are shown in the RecyclerView, and filling them with information.
 *
 * Created by André on 12.02.2016.
 */
public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private ArrayList<Course> data;
    // private ArrayList<String/Agenda> agendaData;

    public CourseRecyclerViewAdapter(ArrayList<Course> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Implement logic deciding what card will come next
        // Return either 1 or 100, 1: course card, 100: agenda card
        // Log.i("adapter", "position in list: " + position);
        if (position == 1) {
            return 1;
        } else {
            return 1;
        }
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        GeneralViewHolder holder;
        View v;

        if (viewType == 1) { // course card
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card, parent, false);
            holder = new CourseViewHolder(v);

        } else { // agenda card
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_card, parent, false);
            holder = new AgendaViewHolder(v);
        }

        return holder;
    }


    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        if(getItemViewType(position) == 1) {
            CourseViewHolder courseHolder = (CourseViewHolder) holder;
            courseHolder.course_code.setText(data.get(position).getCourseCode());
            courseHolder.course_title.setText(data.get(position).getName());

            //Example: holder1.course_id.setText(data.get(position));
            // Set information for course card via getViewById()
        } else {
            AgendaViewHolder holder1 = (AgendaViewHolder) holder;
            //Example: holder1.title.setText(data.get(position));
            // Set information for agenda card via getViewById()
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}