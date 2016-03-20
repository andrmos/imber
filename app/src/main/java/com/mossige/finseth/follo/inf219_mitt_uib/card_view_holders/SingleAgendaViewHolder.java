package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

import org.w3c.dom.Text;

/**
 * View holder defining the agenda RecyclerView card for a users next agendas.
 *
 * Created by Andre on 22/02/2016.
 */
public class SingleAgendaViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView title;
    public TextView time;
    public TextView location;

    public SingleAgendaViewHolder(View v) {
        super(v);
        title = (TextView) v.findViewById(R.id.agenda_title);
        time = (TextView) v.findViewById(R.id.agenda_time);
        location = (TextView) v.findViewById(R.id.agenda_location);
    }
}
