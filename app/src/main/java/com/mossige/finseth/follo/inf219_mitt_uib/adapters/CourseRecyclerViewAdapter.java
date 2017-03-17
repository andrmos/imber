package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.AgendasViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.AnnouncementsViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.FileBrowserViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;

import java.util.ArrayList;

import hirondelle.date4j.DateTime;

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
            if(announcements.size() == 0) {

                //Inflate no announcements picture
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_announcements,parent,false);
                TextView imberLogo = (TextView) v.findViewById(R.id.imberLogo);
                Typeface tf = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/Pattaya-Regular.ttf");
                imberLogo.setTypeface(tf);

            }else {
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcements_card, parent, false);
            }
            holder = new AnnouncementsViewHolder(v);

        } else if (viewType == 1) { // agendas card
            if(agendas.size() == 0) {

                //Inflate no agendas picture
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_agenda,parent,false);
                TextView imberLogo = (TextView) v.findViewById(R.id.imberLogo);
                Typeface tf = Typeface.createFromAsset(parent.getContext().getAssets(), "fonts/Pattaya-Regular.ttf");
                imberLogo.setTypeface(tf);

            }else{
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agendas_card, parent, false);
            }
            holder = new AgendasViewHolder(v);

        } else if (viewType == 2) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_browser_card, parent, false);
            holder = new FileBrowserViewHolder(v);
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

            if (agendas.size() >= 2) {
                agendasViewHolder.agenda2.setText(getEvent(1));

                if (agendas.size() >= 3) {
                    agendasViewHolder.agenda3.setText(getEvent(2));
                }else{
                    agendasViewHolder.dividerAgenda2.setVisibility(View.GONE);
                }

            }else{
                agendasViewHolder.dividerAgenda1.setVisibility(View.GONE);
                agendasViewHolder.dividerAgenda2.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Formatting string representation for a course
     * @param i
     * @return
     */
    private String getEvent(int i) {
        DateTime start = agendas.get(i).getStartDate();
        DateTime end = agendas.get(i).getEndDate();

        String summary = agendas.get(i).getTitle() + " ";

        //Gives time two digit representation
        summary += start.getDay() + ".";
        summary += start.getMonth() + " ";
        summary += String.format("%02d",start.getHour()) + ":";
        summary += String.format("%02d",start.getMinute()) + "-";
        summary += String.format("%02d",end.getHour()) + ":";
        summary += String.format("%02d",end.getMinute());

        return summary;
    }

    private void setAnnouncements(GeneralViewHolder holder) {
        AnnouncementsViewHolder announcementsViewHolder = (AnnouncementsViewHolder) holder;

        if (announcements.size() >= 1) {
            announcementsViewHolder.announcements1.setText(announcements.get(0).getTitle());

            if (announcements.size() >= 2) {
                announcementsViewHolder.announcements2.setText(announcements.get(1).getTitle());

                if (announcements.size() >= 3) {
                    announcementsViewHolder.announcements3.setText(announcements.get(2).getTitle());
                }else{
                    announcementsViewHolder.dividerAnn2.setVisibility(View.GONE);
                }

            }else{
                announcementsViewHolder.dividerAnn1.setVisibility(View.GONE);
                announcementsViewHolder.dividerAnn2.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}