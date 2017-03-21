package no.mofifo.imber.card_view_holders;

import android.view.View;
import android.widget.TextView;

import no.mofifo.imber.R;

/**
 * Created by Follo on 17.03.2016.
 */
public class MessageViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView conversation_message;
    public TextView conversation_author;
    public TextView conversation_time;

    public MessageViewHolder(View v) {
        super(v);
        conversation_author = (TextView) v.findViewById(R.id.message_author);
        conversation_time = (TextView) v.findViewById(R.id.message_time);
        conversation_message = (TextView) v.findViewById(R.id.message_body);

    }
}
