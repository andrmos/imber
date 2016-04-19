package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.AgendasViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.SingleAgendaViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.AnnouncementsViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;

import java.util.ArrayList;
import java.util.Date;

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

    public CourseRecyclerViewAdapter(ArrayList<Announcement> announcements, ArrayList<CalendarEvent> agendas) {
        this.announcements = announcements;
        this.agendas = agendas;
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
        }

        return holder;
    }


    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        if(getItemViewType(position) == 0) {
            setAnnouncements(holder);
        } else if (getItemViewType(position) == 1){
            setAgendas(holder);
        }
    }

    private void setAgendas(GeneralViewHolder holder) {
        AgendasViewHolder agendasViewHolder = (AgendasViewHolder) holder;

        if (agendas.size() >= 1) {
            agendasViewHolder.agenda1.setText(getEvent(0));
        }

        if (agendas.size() >= 2) {
            agendasViewHolder.agenda2.setText(getEvent(1));
        }

        if (agendas.size() >= 3) {
            agendasViewHolder.agenda3.setText(getEvent(2));
        }
    }

    /**
     * Formatting string representation for a course
     * @param i
     * @return
     */
    private String getEvent(int i) {
        Date start = agendas.get(i).getStartDate();
        Date end = agendas.get(i).getEndDate();

        String summary = agendas.get(i).getName() + " ";

        //Gives time two digit representation
        summary += start.getDate() + ".";
        summary += start.getMonth() + " ";
        summary += String.format("%02d",start.getHours()) + ":";
        summary += String.format("%02d",start.getMinutes()) + "-";
        summary += String.format("%02d",end.getHours()) + ":";
        summary += String.format("%02d",end.getMinutes());

        return summary;
    }

    private void setAnnouncements(GeneralViewHolder holder) {
        AnnouncementsViewHolder announcementsViewHolder = (AnnouncementsViewHolder) holder;

        if (announcements.size() >= 1) {
            announcementsViewHolder.announcements1.setText(announcements.get(0).getTitle());
        }

        if (announcements.size() >= 2) {
            announcementsViewHolder.announcements2.setText(announcements.get(1).getTitle());
        }

        if (announcements.size() >= 3) {
            announcementsViewHolder.announcements3.setText(announcements.get(2).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}