package no.mofifo.imber.card_view_holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import no.mofifo.imber.R;

/**
 * View holder defining the course RecyclerView card for a users courses.
 *
 * Created by Andre on 22/02/2016.
 */
public class RecipientViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView name;
    public CheckBox checkBox;

    public RecipientViewHolder(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.name);
        checkBox = (CheckBox) v.findViewById(R.id.checkBox);
    }
}