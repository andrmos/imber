package com.mossige.finseth.follo.inf219_mitt_uib.network.downloads;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AgendaFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.network.CalendarParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class DownloadCalendarTask extends AsyncTask<URL, Integer, ArrayList<CalendarEvent>> {

    private static final String TAG = "DownloadCalendar";

    private ArrayList<CalendarEvent> calendarEvents;
    private AgendaFragment agendaFragment;

    public DownloadCalendarTask(AgendaFragment agendaFragment) {
        this.agendaFragment = agendaFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
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
        agendaFragment.setAgendas(calendarEvents);
    }
}