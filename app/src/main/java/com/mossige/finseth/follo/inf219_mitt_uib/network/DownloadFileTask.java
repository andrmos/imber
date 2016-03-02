package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.adapters.AgendaRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.AgendaFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CourseFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.network.CalendarParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class DownloadFileTask extends AsyncTask<URL, Integer, ArrayList<CalendarEvent>> {

    private static final String TAG = "DownloadFileTask";

    protected ArrayList<CalendarEvent> cal;

    RecyclerView.Adapter adapter;

    public DownloadFileTask(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    protected ArrayList<CalendarEvent> doInBackground(URL... urls) {
        CalendarParser cp = new CalendarParser();
        Log.i(TAG, "doInBackground: " + "calendarparser made");
        try {
            cal = cp.parseCalendar(urls[0]);
            Log.i(TAG, "doInBackground: " + "cal.size()" + cal.size());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cal;
    }

    @Override
    protected void onPostExecute(ArrayList<CalendarEvent> calendarEvents) {
        super.onPostExecute(calendarEvents);
        this.adapter.notifyDataSetChanged();
        Log.i(TAG, "onPostExecute: " + calendarEvents.size());
    }
}