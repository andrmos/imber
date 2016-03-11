package com.mossige.finseth.follo.inf219_mitt_uib.network.downloads;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCal;
import com.mossige.finseth.follo.inf219_mitt_uib.network.CalendarParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class DownloadCourseCalFileTask extends AsyncTask<URL, Integer, ArrayList<CalendarEvent>> {

    private static final String TAG = "DownloadCourseCalFileTa";

    protected ArrayList<CalendarEvent> cal;

    RecyclerView.Adapter adapter;

    public DownloadCourseCalFileTask(RecyclerView.Adapter adapter) {
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

        MyCal cal2 = new MyCal(cal);


        Log.i(TAG, "doInBackground: " + new Date());
        Date curDate = new Date();

        return cal2.getNextEvents(curDate);
    }

    @Override
    protected void onPostExecute(ArrayList<CalendarEvent> calendarEvents) {
        super.onPostExecute(calendarEvents);
        this.adapter.notifyDataSetChanged();
        Log.i(TAG, "onPostExecute: " + calendarEvents.size());
    }
}