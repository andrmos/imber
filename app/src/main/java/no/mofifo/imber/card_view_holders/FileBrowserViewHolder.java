package no.mofifo.imber.card_view_holders;

import android.view.View;
import android.widget.TextView;

import no.mofifo.imber.R;

/**
 * Created by andre on 24.01.17.
 */

public class FileBrowserViewHolder extends GeneralViewHolder {

    public TextView fileBrowserText;

    public FileBrowserViewHolder(View v) {
        super(v);
        fileBrowserText = (TextView) v.findViewById(R.id.fileBrowserText);
    }
}
