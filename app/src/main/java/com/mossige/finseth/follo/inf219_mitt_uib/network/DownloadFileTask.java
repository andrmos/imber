package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.os.AsyncTask;

import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.network.CalendarParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class DownloadFileTask extends AsyncTask<URL, Integer, ArrayList<CalendarEvent>> {

    protected ArrayList<CalendarEvent> cal;

    protected ArrayList<CalendarEvent> doInBackground(URL... urls) {
        CalendarParser cp = new CalendarParser();
        try {
            cal = cp.parseCalendar(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cal;
    }

}
