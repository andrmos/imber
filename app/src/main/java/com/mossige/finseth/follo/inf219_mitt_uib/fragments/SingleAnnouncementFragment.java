package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * Created by PatrickFinseth on 13.03.16.
 */
public class SingleAnnouncementFragment extends Fragment{


    private static final String TAG = "SingleAnnFragment";

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
        sender_tv.setText("" + getArguments().get("userName"));
        date_tv.setText("" + getArguments().get("postedAt"));

        return view;
    }

}
