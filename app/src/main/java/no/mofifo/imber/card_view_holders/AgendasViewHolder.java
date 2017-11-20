package no.mofifo.imber.card_view_holders;

import android.view.View;
import android.widget.TextView;

import no.mofifo.imber.R;

/**
 * View holder defining the agenda RecyclerView card for a users next agendas.
 *
 * Created by Andre on 22/02/2016.
 */
public class AgendasViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView title;
    public TextView event1;
    public TextView event2;
    public TextView event3;
    public View dividerAgenda1;
    public View dividerAgenda2;

    public AgendasViewHolder(View v) {
        super(v);
        title = (TextView) v.findViewById(R.id.agendas_title);
        event1 = (TextView) v.findViewById(R.id.agenda1);
        event2 = (TextView) v.findViewById(R.id.agenda2);
        event3 = (TextView) v.findViewById(R.id.agenda3);
        dividerAgenda1 = v.findViewById(R.id.dividerAgenda1);
        dividerAgenda2 = v.findViewById(R.id.dividerAgenda2);
    }
}
