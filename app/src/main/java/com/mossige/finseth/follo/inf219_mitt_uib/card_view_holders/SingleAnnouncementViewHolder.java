package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * Created by PatrickFinseth on 13.03.16.
 */
public class SingleAnnouncementViewHolder extends GeneralViewHolder {

    public TextView title;
    public TextView messagePeak;

    public SingleAnnouncementViewHolder(View v){
        super(v);

        title = (TextView) v.findViewById(R.id.announcement_title);
        messagePeak = (TextView) v.findViewById(R.id.announcement_messagePeak);
    }

}
