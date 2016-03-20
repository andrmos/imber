package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.util.Log;

import com.mossige.finseth.follo.inf219_mitt_uib.models.CalendarEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by PatrickFinseth on 23.02.16.
 */
public class CalendarParser {

    private static final String TAG = "CalendarParser";

    private static final String BEGIN_EVENT = "BEGIN:VEVENT";
    private static final String END_EVENT = "END:VEVENT";

    public static ArrayList<CalendarEvent> parseCalendar(URL url) throws IOException {

        InputStream fil = url.openStream();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(fil));

        ArrayList<CalendarEvent> events = new ArrayList<>();

        ArrayList<String> event = new ArrayList<>();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {

            if(inputLine.equals(BEGIN_EVENT)) {
                while (!(inputLine = in.readLine()).equals(END_EVENT)) {
                    event.add(inputLine);
                }
                event.add(END_EVENT);
            }

            if(eventComplete(event)) {
                events.add(parseOneEvent(event));
            }
            event.clear();
        }
        in.close();

        return events;
    }

    private static boolean eventComplete(ArrayList<String> event) {
        boolean startOk = false;
        boolean endOk = false;
        boolean summaryOk = false;

        for (String e : event) {
            if(e.startsWith("DTSTART:")) {
                startOk = true;
            }
            if(e.startsWith("DTEND:")) {
                endOk = true;
            }
            if(e.startsWith("SUMMARY:")) {
                summaryOk = true;
            }
        }

        return startOk && endOk && summaryOk;
    }

    private static CalendarEvent parseOneEvent(ArrayList<String> event) {

        String courseName = "";
        Times start = null, stop = null;
        String key;
        String summary = "";
        String time;
        String location = "";

        for (String line : event) {
            if (line.startsWith("X-WR-CALNAME:")) {
                courseName = getCourseName(line);
            } else if (line.startsWith("DTEND:") || line.startsWith("DTSTART:")) {
                key = setKey(line);
                time = getTimeFromLine(line);

                if (key.equals(Times.END)) {
                    stop = parseTimes(time);
                } else {
                    start = parseTimes(time);
                }

            } else if (line.startsWith("SUMMARY:")) {
                summary = getSummary(line);
            } else if (line.startsWith("LOCATION:")) {
                location = getLocation(line);
            }
        }

        return addCalendarEvent(courseName, summary, start, stop, location);
    }

    private static String getLocation(String line) {
        String[] sum = line.split(" ");
        StringBuilder sb = new StringBuilder();
        for (char c : line.toCharArray()) {
            if(c == '(' || c == ')' || c == 92 || c == ',') {
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private static CalendarEvent addCalendarEvent(String courseName, String summary, Times start, Times stop, String location) {
        Date startDate = start.toDate();
        Date stopDate = stop.toDate();
        Log.i(TAG, "addCalendarEvent: location parsed " + location);
        return new CalendarEvent(courseName, summary, startDate, stopDate, location);
    }

    private static String getSummary(String line) {
        String[] sum = line.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < sum.length; i++) {
            if(sum[i].equals("/"))
                break;
            sb.append(sum[i]);
            sb.append(" ");
        }
        return sb.toString();
    }

    private static String getTimeFromLine(String line) {
        String[] tmp = line.split(":");
        return tmp[1];
    }

    private static String setKey(String line) {
        if(line.startsWith("DTEND")) {
            return Times.END;
        } else {
            return Times.START;
        }
    }

    private static String getCourseName(String line) {
        String[] name = line.split(":");
        return name[1];
    }

    private static Times parseTimes(String time) {
        int year = parseIntSub(time, SubStringIntervals.getYearInterval());
        int month = parseIntSub(time, SubStringIntervals.getMonthInterval());
        int day = parseIntSub(time, SubStringIntervals.getDayInterval());
        int hour = parseIntSub(time, SubStringIntervals.getHourInterval());
        int min = parseIntSub(time, SubStringIntervals.getMinuteInterval());
        return new Times(year, month, day, hour, min);
    }

    private static int parseIntSub(String time, SubStringIntervals inter) {
        return Integer.parseInt(time.substring(inter.first, inter.second));
    }

    private static class Times {

        public static final String START = "start";
        public static final String END = "end";

        int year, month, day, hour, minute;

        public Times(int year, int month, int day, int hour, int min) {
            this.year = year-1900;
            this.month = month;
            this.day = day;
            this.hour = hour+1;
            this.minute = min;
        }

        public Date toDate() {
            int year = this.year;
            int month = this.month;
            int day = this.day;
            int hour = this.hour;
            int min = this.minute;
            return new Date(year, month, day, hour, min);
        }

    }

    private static class SubStringIntervals {

        int first;
        int second;

        public SubStringIntervals(int f, int s) {
            first = f;
            second = s;
        }

        public static SubStringIntervals getYearInterval() {
            return new SubStringIntervals(0, 4);
        }

        public static SubStringIntervals getMonthInterval() {
            return new SubStringIntervals(4, 6);
        }

        public static SubStringIntervals getDayInterval() {
            return new SubStringIntervals(6, 8);
        }

        public static SubStringIntervals getHourInterval() {
            return new SubStringIntervals(9, 11);
        }

        public static SubStringIntervals getMinuteInterval() {
            return new SubStringIntervals(11, 13);
        }
    }
}