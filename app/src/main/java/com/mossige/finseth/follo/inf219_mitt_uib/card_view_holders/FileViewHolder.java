package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * Created by andre on 09.01.17.
 */

public class FileViewHolder extends GeneralViewHolder {

    public TextView name;
    public ImageView image;

    public FileViewHolder(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.name);
        image = (ImageView) v.findViewById(R.id.image);
    }

}
