package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * View holder defining the agenda RecyclerView card for a users next agendas.
 *
 * Created by Andre on 22/02/2016.
 */
public class AgendaViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView agenda_title;
    public TextView agenda_next_agenda;

    public AgendaViewHolder(View v) {
        super(v);
        agenda_title = (TextView) v.findViewById(R.id.agenda_title);
        agenda_next_agenda = (TextView) v.findViewById(R.id.agenda_next_agenda);
    }
}
