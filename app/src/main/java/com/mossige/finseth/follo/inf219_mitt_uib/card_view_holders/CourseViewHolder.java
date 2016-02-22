package com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders;

import android.view.View;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * View holder defining the course RecyclerView card for a users courses.
 *
 * Created by Andre on 22/02/2016.
 */
public class CourseViewHolder extends GeneralViewHolder {
    // each data item is just a string
    public TextView course_id;
    public TextView course_title;
    public CourseViewHolder(View v) {
        super(v);
        course_id = (TextView) v.findViewById(R.id.course_id);
        course_title = (TextView) v.findViewById(R.id.course_title);
    }
}