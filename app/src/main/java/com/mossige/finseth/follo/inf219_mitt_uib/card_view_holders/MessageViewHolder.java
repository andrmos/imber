package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * Created by Follo on 17.03.2016.
 */
public class MessageViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView conversation_message;
    public TextView message_authorID;

    public MessageViewHolder(View v) {
        super(v);
        conversation_message = (TextView) v.findViewById(R.id.conversation_message);
        message_authorID = (TextView) v.findViewById(R.id.message_authorID);

    }
}
