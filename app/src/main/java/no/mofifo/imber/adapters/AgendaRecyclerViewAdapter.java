package no.mofifo.imber.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.mofifo.imber.R;
import no.mofifo.imber.card_view_holders.SingleAgendaViewHolder;
import no.mofifo.imber.card_view_holders.GeneralViewHolder;
import no.mofifo.imber.models.CalendarEvent;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView which will hold the cards with titles of the different courses.
 * Responsible for deciding what elements are shown in the RecyclerView, and filling them with information.
 *
 * Created by André on 12.02.2016.
 */
public class AgendaRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private static final String TAG = "AgendaRecyclerViewAdapter: ";

    private ArrayList<CalendarEvent> data;

    public AgendaRecyclerViewAdapter(ArrayList<CalendarEvent> data) {
        this.data = data;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // agenda card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.agenda_card, parent, false);
        GeneralViewHolder holder = new SingleAgendaViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        SingleAgendaViewHolder agendaHolder = (SingleAgendaViewHolder) holder;
        agendaHolder.title.setText(data.get(position).getTrimmedTitle());
        agendaHolder.time.setText(getFormatDate(position));
        agendaHolder.location.setText(data.get(position).getTrimmedLocation());
    }

    private String getFormatDate(int position){
        //Gives time two digits
        String date = "";

        if(data.get(position).getStartDate().equals(data.get(position).getEndDate())){
            date = String.format("%02d",data.get(position).getStartDate().getHour()) + ":";
            date += String.format("%02d",data.get(position).getStartDate().getMinute());
        }else {
            date = String.format("%02d", data.get(position).getStartDate().getHour()) + ":";
            date += String.format("%02d", data.get(position).getStartDate().getMinute()) + " - ";
            date += String.format("%02d", data.get(position).getEndDate().getHour()) + ":";
            date += String.format("%02d", data.get(position).getEndDate().getMinute());
        }

        return date;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}