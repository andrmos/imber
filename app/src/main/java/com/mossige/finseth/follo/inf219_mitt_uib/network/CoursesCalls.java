package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Follo on 16.02.2016.
 */
public class CoursesCalls {

    ArrayList<Course> courses;

    public ArrayList<Course> getCourses() {

        courses = new ArrayList<>();

        String access_token = "VlnlJJOuvVjFMIP2vsmucYXcKOVZrjFhLxzZjRsDX6S1BdQmmviW8OPGu2VBDHpl"; // TODO Add access token
        String url = "https://mitt.uib.no/api/v1/courses?access_token=" + access_token;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject resp = null;

                String id;
                String name;
                String calendar;
                ArrayList<String> announcements = new ArrayList<>();

//                JSONArray tmpAnnouncements;
//                JSONObject announcement;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        resp = response.getJSONObject(i);
                        id = resp.getString("id");
                        name = resp.getString("name");
                        calendar = resp.getString("calendar");
//                        tmpAnnouncements = new JSONArray(resp.getString("participants"));
//                        for (int j = 0; j < tmpAnnouncements.length(); j++) {
//                            announcement = tmpAnnouncements.getJSONObject(j);
//                            announcements.add(announcement.getString("name"));
//                        }
                    } catch (JSONException e) {
                        e.getStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }

        });

        while(!jsonArrayRequest.hasHadResponseDelivered()) {
            SystemClock.sleep(200);
        }

        return courses;
    }

}
