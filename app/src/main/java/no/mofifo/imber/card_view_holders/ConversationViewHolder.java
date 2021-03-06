package no.mofifo.imber.card_view_holders;

import android.view.View;
import android.widget.TextView;

import no.mofifo.imber.R;

/**
 * Created by Follo on 15.03.2016.
 */
public class ConversationViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView conversation_subject;
    public TextView conversation_participants;

    public ConversationViewHolder(View v) {
        super(v);
        conversation_subject = (TextView) v.findViewById(R.id.conversation_subject);
        conversation_participants = (TextView) v.findViewById(R.id.conversation_participants);

    }
}
