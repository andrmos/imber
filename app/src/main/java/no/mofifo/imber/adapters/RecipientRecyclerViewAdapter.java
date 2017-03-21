package no.mofifo.imber.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.mofifo.imber.R;
import no.mofifo.imber.card_view_holders.GeneralViewHolder;
import no.mofifo.imber.card_view_holders.RecipientViewHolder;
import no.mofifo.imber.models.Recipient;

import java.util.ArrayList;

/**
 * Adapter for the RecyclerView which will hold the cards with titles of the different courses.
 * Responsible for deciding what elements are shown in the RecyclerView, and filling them with information.
 *
 * Created by André on 12.02.2016.
 */
public class RecipientRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder>{

    private final static String TAG = "RecipientAdapter";

    private ArrayList<Recipient> data;

    public RecipientRecyclerViewAdapter(ArrayList<Recipient> data) {
        this.data = data;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipient_card, parent, false);
        GeneralViewHolder holder = new RecipientViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        RecipientViewHolder recipientHolder = (RecipientViewHolder) holder;
        recipientHolder.name.setText(data.get(position).getName());
        recipientHolder.checkBox.setChecked(data.get(position).getChecked());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}