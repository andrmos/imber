package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.AgendasViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GradesViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.SingleAgendaViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.AnnouncementsViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView.
 * Responsible for deciding what elements are shown in the RecyclerView, and filling them with information.
 *
 * Created by André on 12.02.2016.
 */
public class CourseRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private static final String TAG = "CourseAdapter";

    private ArrayList<Announcement> announcements;
    private ArrayList<CalendarEvent> agendas;
    private ArrayList<String> grades;

    public CourseRecyclerViewAdapter(ArrayList<Announcement> announcements, ArrayList<CalendarEvent> agendas, ArrayList<String> grades) {
        this.announcements = announcements;
        this.agendas = agendas;
        this.grades = grades;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        GeneralViewHolder holder = null;
        View v;

        if (viewType == 0) { // Announcements card
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcements_card, parent, false);
            holder = new AnnouncementsViewHolder(v);

        } else if (viewType == 1) { // agendas card
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agendas_card, parent, false);
            holder = new AgendasViewHolder(v);

        } else { // grades card
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grades_card, parent, false);
            holder = new GradesViewHolder(v);
        }

        return holder;
    }


    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        if(getItemViewType(position) == 0) {
            // Announcements
            AnnouncementsViewHolder announcementsViewHolder = (AnnouncementsViewHolder) holder;

            // TODO // FIXME: 10/03/2016 ugle code, maybe fix with ArrayList<Announcemnt> or data model in view holder
            if (announcements.size() >= 1) {
                announcementsViewHolder.announcements1.setText(announcements.get(0).getTitle());
            }

            if (announcements.size() >= 2) {
                announcementsViewHolder.announcements2.setText(announcements.get(1).getTitle());
            }

            if (announcements.size() >= 3) {
                announcementsViewHolder.announcements3.setText(announcements.get(2).getTitle());
            }


        } else if (getItemViewType(position) == 1){
            // Agendas
            AgendasViewHolder agendasViewHolder = (AgendasViewHolder) holder;

            if (agendas.size() >= 1) {
                agendasViewHolder.agenda1.setText(agendas.get(0).getSummary() + "   " + agendas.get(0).getStartDate().toString() + " - " + agendas.get(0).getEndDate().toString());
            }

            if (agendas.size() >= 2) {
                agendasViewHolder.agenda2.setText(agendas.get(1).getSummary() + "   " + agendas.get(1).getStartDate().toString() + " - " + agendas.get(1).getEndDate().toString());
            }

            if (agendas.size() >= 3) {
                agendasViewHolder.agenda3.setText(agendas.get(2).getSummary() + "   " + agendas.get(2).getStartDate().toString() + " - " + agendas.get(2).getEndDate().toString());
            }

        } else if (getItemViewType(position) == 2) {
            // Grades
            GradesViewHolder gradesViewHolder = (GradesViewHolder) holder;

            if (grades.size() >= 1) {
                gradesViewHolder.grade1.setText(grades.get(0));
            }

            if (grades.size() >= 2) {
                gradesViewHolder.grade2.setText(grades.get(1));
            }

            if (grades.size() >= 3) {
                gradesViewHolder.grade3.setText(grades.get(2));
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}