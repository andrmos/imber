package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * Created by Follo on 20.03.2016.
 */
public class RecipientCardViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView name;
    public TextView groupName;

    public RecipientCardViewHolder(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.recipient_name);
        groupName = (TextView) v.findViewById(R.id.recipient_group_name);
    }
}
