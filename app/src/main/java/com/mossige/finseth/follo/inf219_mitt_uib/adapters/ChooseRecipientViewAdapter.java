package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.RecipientCardViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.SingleAnnouncementViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;

import java.util.ArrayList;

/**
 * Created by PatrickFinseth on 13.03.16.
 */
public class ChooseRecipientViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private static final String TAG = "ChooseRecipientViewAdapter: ";

    private ArrayList<Recipient> data;

    public ChooseRecipientViewAdapter (ArrayList<Recipient> data) {
        this.data = data;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // agenda card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_recipient, parent, false);
        GeneralViewHolder holder = new RecipientCardViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        RecipientCardViewHolder recipientCardViewHolder = (RecipientCardViewHolder) holder;
        recipientCardViewHolder.name.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
