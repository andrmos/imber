package com.mossige.finseth.follo.inf219_mitt_uib;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View rootView;
    private ArrayList<String> titles;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        RequestQueue rq = Volley.newRequestQueue(getActivity());

        titles = new ArrayList<>();

        String access_token = "s144usYRrSLV35kyGLwdsSwnfb5bDqjll4eK0JCYFQNADR7PsT9442W3TZvQOAWG"; // TODO Add access token
        String url = "https://mitt.uib.no/api/v1/courses?access_token=" + access_token;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject resp = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        resp = response.getJSONObject(i);
                        titles.add(resp.getString("name"));
                        Log.i("VolleyTest", titles.get(i));
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

        rq.add(jsonArrayRequest);

        while(!jsonArrayRequest.hasHadResponseDelivered()) {
            SystemClock.sleep(200);
        }

        initRecycleView(rootView);


        return rootView;
    }


    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.mainList);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        ArrayList<String> testData = initData(10);

        // Create adapter that binds the views with some content
        mAdapter = new MainListAdapter(testData);
        mainList.setAdapter(mAdapter);
    }

    public ArrayList<String> initData(int amount) {
        String title = "Tittel";
        ArrayList<String> testData = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            testData.add(title + i);
        }

        return testData;
    }

}
