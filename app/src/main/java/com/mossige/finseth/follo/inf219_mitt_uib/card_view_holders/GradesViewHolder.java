package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * View holder defining the agenda RecyclerView card for a users next agendas.
 *
 * Created by Andre on 22/02/2016.
 */
public class GradesViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView title;
    public TextView grade1;
    public TextView grade2;
    public TextView grade3;

    // TODO models in viewholder?


    public GradesViewHolder(View v) {
        super(v);
        title = (TextView) v.findViewById(R.id.grades_title);
        grade1 = (TextView) v.findViewById(R.id.grade1);
        grade2 = (TextView) v.findViewById(R.id.grade2);
        grade3 = (TextView) v.findViewById(R.id.grade3);
    }
}
