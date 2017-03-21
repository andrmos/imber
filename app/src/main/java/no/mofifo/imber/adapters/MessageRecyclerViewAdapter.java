package no.mofifo.imber.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import no.mofifo.imber.R;
import no.mofifo.imber.card_view_holders.GeneralViewHolder;
import no.mofifo.imber.card_view_holders.MessageViewHolder;
import no.mofifo.imber.models.Message;
import no.mofifo.imber.models.Participant;

import java.util.ArrayList;
import java.util.Locale;

import static no.mofifo.imber.utils.Constants.DATETIME_FORMAT_EN;
import static no.mofifo.imber.utils.Constants.DATETIME_FORMAT_NO;

/**
 * Created by Follo on 15.03.2016.
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private ArrayList<Message> data;
    private ArrayList<Participant> participants;

    public MessageRecyclerViewAdapter(ArrayList<Message> data) {
        this.data = data;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card, parent, false);
        GeneralViewHolder holder = new MessageViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        MessageViewHolder singleConversationHolder = (MessageViewHolder) holder;
        singleConversationHolder.conversation_author.setText(getAuthorName(data.get(position).getAuthorId()));
        Locale locale = Locale.getDefault();
        if (locale.getCountry().equalsIgnoreCase("no")) {
            singleConversationHolder.conversation_time.setText(data.get(position).getCreatedAt().format(DATETIME_FORMAT_NO, locale));
        } else {
            singleConversationHolder.conversation_time.setText(data.get(position).getCreatedAt().format(DATETIME_FORMAT_EN, locale));
        }
        singleConversationHolder.conversation_message.setText(data.get(position).getBody().trim());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private String getAuthorName(int authorId) {
        if (participants != null) {
            for (Participant p : participants) {
                if (p.getId() == authorId) {
                    return p.getName();
                }
            }
        }
        return Integer.toString(authorId);
    }
}