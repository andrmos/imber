package no.mofifo.imber.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.mofifo.imber.R;
import no.mofifo.imber.card_view_holders.ConversationViewHolder;
import no.mofifo.imber.card_view_holders.GeneralViewHolder;
import no.mofifo.imber.models.Conversation;
import no.mofifo.imber.models.Participant;

import java.util.ArrayList;

/**
 * Created by Follo on 15.03.2016.
 */
public class ConversationRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private ArrayList<Conversation> data;

    public ConversationRecyclerViewAdapter(ArrayList<Conversation> data) {
        this.data = data;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // course card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_card, parent, false);
        GeneralViewHolder holder = new ConversationViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        ConversationViewHolder conversationHolder = (ConversationViewHolder) holder;

        //Capitalize first char in subject
        String capSubject = data.get(position).getSubject().substring(0,1).toUpperCase() + data.get(position).getSubject().substring(1);
        String participants = "";
        for(Participant p : data.get(position).getParticipants()){
            participants +=  p.getName() + ", ";
        }

        participants = participants.trim();
        participants = participants.substring(0,participants.length()-1);

        conversationHolder.conversation_subject.setText(capSubject);
        conversationHolder.conversation_participants.setText(participants);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}