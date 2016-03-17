package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * Created by Follo on 15.03.2016.
 */
public class ConversationViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView conversation_subject;
    public TextView conversation_lastMessageFrom;
    public TextView course_title;
    public TextView course_id;
    public TextView calendar_url;

    public ConversationViewHolder(View v) {
        super(v);
        conversation_subject = (TextView) v.findViewById(R.id.conversation_subject);
        conversation_lastMessageFrom = (TextView) v.findViewById(R.id.conversation_lastMessageFrom);
//        course_title = (TextView) v.findViewById(R.id.course_title);
//        course_id = (TextView) v.findViewById(R.id.course_id);
//        calendar_url = (TextView) v.findViewById(R.id.calendar_url);
    }
}
