package no.mofifo.imber.card_view_holders;

import android.view.View;
import android.widget.TextView;

import no.mofifo.imber.R;

/**
 * View holder defining the agenda RecyclerView card for a users next agendas.
 *
 * Created by Andre on 22/02/2016.
 */
public class AnnouncementsViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView title;
    public TextView announcements1;
    public TextView announcements2;
    public TextView announcements3;
    public View dividerAnn1;
    public View dividerAnn2;


    public AnnouncementsViewHolder(View v) {
        super(v);
        title = (TextView) v.findViewById(R.id.announcements_title);
        announcements1 = (TextView) v.findViewById(R.id.announcement1);
        announcements2 = (TextView) v.findViewById(R.id.announcement2);
        announcements3 = (TextView) v.findViewById(R.id.announcement3);
        dividerAnn1 = v.findViewById(R.id.dividerAnn1);
        dividerAnn2 = v.findViewById(R.id.dividerAnn2);
    }
}
