package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

    private ProgressBar spinner;

    private Conversation conversation;
    private ArrayList<Message> messages;

    private boolean loaded;

    public SingleConversationFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messages = new ArrayList<>();
        loaded = false;

        requestSingleConversation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        spinner = (ProgressBar) rootView.findViewById(R.id.progressBar);
        initRecycleView(rootView);

        if (loaded) {
            spinner.setVisibility(View.GONE);
        } else {
            spinner.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void requestSingleConversation() {

        JsonObjectRequest singleConversationRequest = new JsonObjectRequest(Request.Method.GET,
                UrlEndpoints.getSingleConversationUrl(getArguments().getString("conversationID")),
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            conversation = JSONParser.parseSingleConversation(response);
                            messages.clear();
                            messages.addAll(conversation.getMessages());

                            getActivity().setTitle(conversation.getSubject());

                            loaded = true;
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
                spinner.setVisibility(View.GONE);
                showToast();
            }
        });

        singleConversationRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(singleConversationRequest);
    }

    private void showToast() {
        Toast.makeText(getContext(), R.string.error_conversation, Toast.LENGTH_SHORT).show();
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Create the LayoutManager that holds all the views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new MessageRecyclerViewAdapter(messages);
        mainList.setAdapter(mAdapter);
    }
}
