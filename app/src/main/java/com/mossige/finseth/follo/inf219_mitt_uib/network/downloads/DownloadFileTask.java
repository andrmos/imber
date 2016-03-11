package com.mossige.finseth.follo.inf219_mitt_uib.network.downloads;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCal;
import com.mossige.finseth.follo.inf219_mitt_uib.network.CalendarParser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


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

    public ArrayList<CalendarEvent> nextAgendas(URL... urls) {
        ArrayList<CalendarEvent> agendas = null;
        try {
            agendas = this.execute(urls).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        MyCal cal = new MyCal(agendas);
        Log.i(TAG, "nextagenda calendar size: " + cal.getCalendar().size());

        ArrayList<CalendarEvent> ret = cal.getEventsForDate(16, 3, 2016);
        Log.i(TAG, "nextAgendas: ret " + ret.size());
        onPostExecute(ret);
        Log.i(TAG, "nextAgendas: onpostexecute");

        return ret;
    }



    @Override
    protected void onPostExecute(ArrayList<CalendarEvent> calendarEvents) {
        super.onPostExecute(calendarEvents);
        this.adapter.notifyDataSetChanged();

        Log.i(TAG, "onPostExecute: " + calendarEvents.size());
    }
}