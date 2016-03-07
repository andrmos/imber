package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.AgendaViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCal;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView which will hold the cards with titles of the different courses.
 * Responsible for deciding what elements are shown in the RecyclerView, and filling them with information.
 *
 * Created by André on 12.02.2016.
 */
public class AgendaRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private static final String TAG = "AgendaRecuclerViewAdapter: ";

    private ArrayList<CalendarEvent> data;

    public AgendaRecyclerViewAdapter(ArrayList<CalendarEvent> data) {
        this.data = data;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // agenda card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_card, parent, false);
        GeneralViewHolder holder = new AgendaViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        AgendaViewHolder agendaHolder = (AgendaViewHolder) holder;
        agendaHolder.title.setText(data.get(position).getSummary());
        agendaHolder.time.setText(getFormatDate(position));
        // TODO set summary
        //agendaHolder.summary.setText(data.get(position).getName());
    }

    private String getFormatDate(int position){
        String date = String.format("%02d",data.get(position).getStartDate().getHours()+1) + ":";
        date += String.format("%02d",data.get(position).getStartDate().getMinutes()) + "-";
        date += String.format("%02d",data.get(position).getEndDate().getHours()+1) + ":";
        date += String.format("%02d",data.get(position).getEndDate().getMinutes());

        return date;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}