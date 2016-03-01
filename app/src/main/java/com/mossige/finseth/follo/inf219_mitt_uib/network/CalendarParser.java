package com.mossige.finseth.follo.inf219_mitt_uib.network;

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

    public static ArrayList<CalendarEvent> parseCalendar(URL url) throws IOException {

        InputStream fil = url.openStream();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(fil));

        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();

        String courseName = "";
        int hourStart = 0;
        int minuteStart = 0;
        int hourEnd = 0;
        int minuteEnd = 0;
        int yearStart = 0;
        int monthStart = 0;
        int dayStart = 0;
        int yearEnd = 0;
        int monthEnd = 0;
        int dayEnd = 0;


        String inputLine;
        while ((inputLine = in.readLine()) != null){

            if(inputLine.startsWith("X-WR-CALNAME:")){
                String[] name = inputLine.split(":");
                courseName = name[1];
            }else if(inputLine.startsWith("DTEND:")){
                String[] tid = inputLine.split(":");
                hourEnd = Integer.parseInt(tid[1].substring(9, 11));
                minuteEnd = Integer.parseInt(tid[1].substring(11, 13));
                yearEnd = Integer.parseInt(tid[1].substring(0, 4));
                monthEnd = Integer.parseInt(tid[1].substring(4, 6));
                dayEnd = Integer.parseInt(tid[1].substring(6,8));
            } else if(inputLine.startsWith("DTSTART:")){
                String[] tid = inputLine.split(":");
                hourStart = Integer.parseInt(tid[1].substring(9, 11));
                minuteStart = Integer.parseInt(tid[1].substring(11, 13));
                yearStart = Integer.parseInt(tid[1].substring(0, 4));
                monthStart = Integer.parseInt(tid[1].substring(4, 6));
                dayStart = Integer.parseInt(tid[1].substring(6,8));

            } else if(inputLine.startsWith("SUMMARY:")){
                String[] sum = inputLine.split(" ");
                String summary = sum[1];

                events.add(new CalendarEvent(courseName,summary,new Date(yearStart-1900,monthStart-1,dayStart,hourStart,minuteStart),new Date(yearEnd-1900,monthEnd-1,dayEnd,hourEnd,minuteEnd)));

            }

        }
        in.close();

        return events;
    }
}
