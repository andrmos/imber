package no.mofifo.imber.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import no.mofifo.imber.R;
import no.mofifo.imber.models.Announcement;

import java.util.Locale;

import static no.mofifo.imber.utils.Constants.DATETIME_FORMAT_EN;
import static no.mofifo.imber.utils.Constants.DATETIME_FORMAT_NO;

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

        if (getArguments() != null && getArguments().containsKey("announcement")) {
            String json = getArguments().getString("announcement");
            Announcement announcement = new Gson().fromJson(json, Announcement.class);

            // Set text to every TextView in fragment_announcement.xml
            title_tv.setText(announcement.getTitle());
            message_tv.setText(announcement.getMessageHtmlEscaped().trim());
            sender_tv.setText(announcement.getUserName());
            Locale locale = Locale.getDefault();
            if (locale.getCountry().equalsIgnoreCase("no")) {
                date_tv.setText(announcement.getPostedAt().format(DATETIME_FORMAT_NO, locale));
            } else {
                date_tv.setText(announcement.getPostedAt().format(DATETIME_FORMAT_EN, locale));
            }
        }

        return view;
    }

}
