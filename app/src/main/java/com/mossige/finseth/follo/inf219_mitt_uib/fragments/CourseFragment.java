package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.User;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseFragment extends Fragment {

    private static final String TAG = "CourseFragment";

    public CourseFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_course, container, false);

        String course_id = getArguments().getString("id");
        requestAnnouncements(course_id);

        return rootView;
    }

    private void requestAnnouncements(String course_id) {

        JsonArrayRequest announcementsRequest = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCourseAnnouncementsUrl(course_id), (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, "Got response");

                try {

                    Log.i(TAG, "" + response.get(0).toString());
                    //String text = response.getJSONObject(0).getString("message");
                    //Log.i(TAG, "message " + text);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.toString());

            }
        });

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(announcementsRequest);
    }

}
