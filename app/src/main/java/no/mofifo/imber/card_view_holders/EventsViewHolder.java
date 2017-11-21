package no.mofifo.imber.card_view_holders;

import android.view.View;
import android.widget.TextView;

import no.mofifo.imber.R;

/**
 * View holder defining the agenda RecyclerView card for a users next agendas.
 *
 * Created by Andre on 22/02/2016.
 */
public class EventsViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView title;
    public TextView event1;
    public TextView event2;
    public TextView event3;
    public View dividerEvent1;
    public View dividerEvent2;

    public EventsViewHolder(View v) {
        super(v);
        title = (TextView) v.findViewById(R.id.events_card_title);
        event1 = (TextView) v.findViewById(R.id.event1);
        event2 = (TextView) v.findViewById(R.id.event2);
        event3 = (TextView) v.findViewById(R.id.event3);
        dividerEvent1 = v.findViewById(R.id.dividerEvent1);
        dividerEvent2 = v.findViewById(R.id.dividerEvent2);
    }
}
