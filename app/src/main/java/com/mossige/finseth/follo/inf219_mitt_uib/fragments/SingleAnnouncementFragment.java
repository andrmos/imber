package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.network.CalendarParser;

/**
 * Created by PatrickFinseth on 13.03.16.
 */
public class SingleAnnouncementFragment extends Fragment{


    private static final String TAG = "SingleAnnFragment";

    private Announcement announcement;

    public SingleAnnouncementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_announcement, container, false);

        TextView title_tv = (TextView) view.findViewById(R.id.single_announcement_title);
        TextView message_tv = (TextView) view.findViewById(R.id.single_announcement_message);
        TextView sender_tv = (TextView) view.findViewById(R.id.single_announcement_sender);
        TextView date_tv = (TextView) view.findViewById(R.id.single_announcement_date);

        // Set text to every TextView in fragment_announcement.xml
        title_tv.setText(""+ getArguments().get("title"));
        message_tv.setText(getArguments().get("message").toString());
        sender_tv.setText("" + getArguments().get("sender"));
        date_tv.setText(parseDate(getArguments().get("date").toString()));

        return view;
    }

    /**
     * Parser and formater for a date string on form XXXX-XX-XXTXX:XX
     */
    private String parseDate(String date){
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);
        String hour = date.substring(11,13);
        String minute = date.substring(14,16);

        return day + "." + month + "." + year + " - " + hour + ":" + minute;
    }


}
