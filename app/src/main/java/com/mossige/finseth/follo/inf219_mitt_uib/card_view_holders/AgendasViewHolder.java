package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * View holder defining the agenda RecyclerView card for a users next agendas.
 *
 * Created by Andre on 22/02/2016.
 */
public class AgendasViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView title;
    public TextView agenda1;
    public TextView agenda2;
    public TextView agenda3;
    //public ListView dividerAgenda1;
    //public ListView dividerAgenda2;

    public AgendasViewHolder(View v) {
        super(v);
        title = (TextView) v.findViewById(R.id.agendas_title);
        agenda1 = (TextView) v.findViewById(R.id.agenda1);
        agenda2 = (TextView) v.findViewById(R.id.agenda2);
        agenda3 = (TextView) v.findViewById(R.id.agenda3);
        //dividerAgenda1 = (ListView) v.findViewById(R.id.dividerAgenda1);
        //dividerAgenda2 = (ListView) v.findViewById(R.id.dividerAgenda2);
    }
}
