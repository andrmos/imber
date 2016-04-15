package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * View holder defining the course RecyclerView card for a users courses.
 *
 * Created by Andre on 22/02/2016.
 */
public class RecipientViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView name;
    public CheckBox sendTo;

    // TODO Textviews name and surname instead?

    public RecipientViewHolder(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.name);
        sendTo = (CheckBox) v.findViewById(R.id.sendTo);
    }
}