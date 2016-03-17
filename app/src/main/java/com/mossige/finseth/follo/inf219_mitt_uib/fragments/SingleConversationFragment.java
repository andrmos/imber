package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.MessageRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Message;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Follo on 15.03.2016.
 */
public class SingleConversationFragment extends Fragment {

    private static final String TAG = "ConversationFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View rootView;
    private ProgressBar spinner;

    private Conversation conversation;
    private ArrayList<Message> messages;

    public SingleConversationFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_course_list, container, false);

        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
        messages = new ArrayList<>();

        initRecycleView();
        requestSingleConversation();

        return rootView;
    }

    private void requestSingleConversation() {
        spinner.setVisibility(View.VISIBLE);

        JsonObjectRequest coursesReq = new JsonObjectRequest(Request.Method.GET,
                UrlEndpoints.getSingleConversationUrl(getArguments().getString("conversationID")),
                (String) null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    conversation = JSONParser.parseSingleConversation(response);

                    messages.clear();
                    for(Message m : conversation.getMessages()) {
                        messages.add(m);
                    }

                    getActivity().setTitle(conversation.getSubject());

                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                }

                spinner.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error response");
                spinner.setVisibility(View.GONE);
            }
        });

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(coursesReq);
    }

    private void initRecycleView() {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.mainList);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());

        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new MessageRecyclerViewAdapter(messages);
        mainList.setAdapter(mAdapter);
    }
}
