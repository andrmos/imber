package com.mossige.finseth.follo.inf219_mitt_uib.network.downloads;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.activities.MainActivity;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AgendaFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.network.CalendarParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by patrickfinseth on 20.03.2016.
 */
public class DownloadDatesTask extends AsyncTask<URL, Integer, ArrayList<CalendarEvent>> {

    private static final String TAG = "DownloadCalendar";

    private ArrayList<CalendarEvent> calendarEvents;
    private MainActivity mainActivity;

    public DownloadDatesTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    protected ArrayList<CalendarEvent> doInBackground(URL... urls) {
        try {
            calendarEvents = CalendarParser.parseCalendar(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return calendarEvents;
    }

    @Override
    protected void onPostExecute(ArrayList<CalendarEvent> calendarEvents) {
        super.onPostExecute(calendarEvents);

        ArrayList<String> dates = new ArrayList<>();

        //Make string format for date for bundling
        for(CalendarEvent ce : calendarEvents){
            String date = "" + ce.getStartDate().getYear();
            date += String.format("%02d",ce.getStartDate().getMonth());
            date += String.format("%02d",ce.getStartDate().getDate());
            dates.add(date);
        }

        mainActivity.setEvents(dates);
    }
}