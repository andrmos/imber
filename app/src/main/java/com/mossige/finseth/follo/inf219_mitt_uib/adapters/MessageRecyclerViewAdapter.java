package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.MessageViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Message;

import java.util.ArrayList;

/**
 * Created by Follo on 15.03.2016.
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private ArrayList<Message> data;

    public MessageRecyclerViewAdapter(ArrayList<Message> data) {
        this.data = data;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // course card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card, parent, false);
        GeneralViewHolder holder = new MessageViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        MessageViewHolder singleConversationHolder = (MessageViewHolder) holder;
        // TODO Change to actual author name
        singleConversationHolder.conversation_author.setText(data.get(position).getAuthorID());
        singleConversationHolder.conversation_time.setText(data.get(position).getCreatedAt());
        singleConversationHolder.conversation_message.setText(data.get(position).getBody().trim());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}