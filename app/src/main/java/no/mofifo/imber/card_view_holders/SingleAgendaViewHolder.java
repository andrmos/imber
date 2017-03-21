package no.mofifo.imber.card_view_holders;

import android.view.View;
import android.widget.TextView;

import no.mofifo.imber.R;

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
