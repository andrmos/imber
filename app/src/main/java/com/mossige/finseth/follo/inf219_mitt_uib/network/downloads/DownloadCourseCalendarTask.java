package com.mossige.finseth.follo.inf219_mitt_uib.network.downloads;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.fragments.CourseFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;
import com.mossige.finseth.follo.inf219_mitt_uib.models.MyCalendar;
import com.mossige.finseth.follo.inf219_mitt_uib.network.CalendarParser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;


public class DownloadCourseCalendarTask extends AsyncTask<URL, Integer, ArrayList<CalendarEvent>> {

    private static final String TAG = "DownloadCourseCalFileTa";

    private CourseFragment courseFragment;
    private ArrayList<CalendarEvent> events;
    //private ProgressDialog progressDialog;

    public DownloadCourseCalendarTask(CourseFragment courseFragment) {
        this.courseFragment = courseFragment;
        //this.progressDialog = new ProgressDialog(courseFragment.getContext());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog.setMessage("Laster faginformasjon...");
        //progressDialog.show();
    }

    protected ArrayList<CalendarEvent> doInBackground(URL... urls) {
        try {
            events = CalendarParser.parseCalendar(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MyCalendar calendar = new MyCalendar(events);
        Date curDate = new Date();
        ArrayList<CalendarEvent> ret = calendar.getThreeNextEvents(curDate);
        Log.i(TAG, "doInBackground: ret size " + ret.size());
        return ret;
    }

    @Override
    protected void onPostExecute(ArrayList<CalendarEvent> calendarEvents) {
        super.onPostExecute(calendarEvents);
        courseFragment.setAgendas(calendarEvents);
        //progressDialog.dismiss();
    }


}