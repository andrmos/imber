package com.mossige.finseth.follo.inf219_mitt_uib;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by André on 12.02.2016.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.GeneralViewHolder> {

    private ArrayList<String> data;

    public static class GeneralViewHolder extends RecyclerView.ViewHolder {
        public GeneralViewHolder(View view) {
            super(view);
        }
    }

    public static class CardViewHolder extends GeneralViewHolder {
        // each data item is just a string
        public TextView course_id;
        public TextView course_title;
        public TextView course_calendar_url;
        public CardViewHolder(View v) {
            super(v);
            course_id = (TextView) v.findViewById(R.id.course_id);
            course_title = (TextView) v.findViewById(R.id.course_title);
            course_calendar_url = (TextView) v.findViewById(R.id.course_calendar_url);
        }
    }

    public static class AgendaViewHolder extends GeneralViewHolder {
        // each data item is just a string
        public TextView agenda_title;
        public TextView agenda_next_agenda;

        public AgendaViewHolder(View v) {
            super(v);
            agenda_title = (TextView) v.findViewById(R.id.agenda_title);
            agenda_next_agenda = (TextView) v.findViewById(R.id.agenda_next_agenda);
        }
    }

    public MainListAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Implement logic deciding what card will come next
        // Return either 1 or 100, 1: course card, 100: agenda card
        if (position == 1) {
            return 1;
        } else {
            return 100;
        }
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        GeneralViewHolder holder;
        View v;

        if (viewType == 1) { // course card
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_card, parent, false);
            holder = new CardViewHolder(v);

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
            CardViewHolder holder1 = (CardViewHolder) holder;
            //Example: holder1.course_id.setText(data.get(position));
            // Set information for course card via getViewById()
        } else {
            AgendaViewHolder holder1 = (AgendaViewHolder) holder;
            //Example: holder1.agenda_title.setText(data.get(position));
            // Set information for agenda card via getViewById()
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}