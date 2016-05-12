package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.activities.MainActivity;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.MessageRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message.ChooseRecipientFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message.ComposeMessageFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Message;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Participant;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
 * Created by Follo on 15.03.2016.
 */
public class SingleConversationFragment extends Fragment {

    private static final String TAG = "ConversationFragment";

    private RecyclerView.Adapter mAdapter;

    private SmoothProgressBar progressbar;

    private Conversation conversation;
    private ArrayList<Message> messages;

    private boolean loaded;

    MainActivityListener mCallback;
    private View rootView;
    private FloatingActionButton fab;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (MainActivityListener) context;
        }catch(ClassCastException e){
            Log.i(TAG, "onAttach: " + e.toString());
        }
    }

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
        rootView = inflater.inflate(R.layout.fragment_conversation, container, false);

        progressbar = (SmoothProgressBar) rootView.findViewById(R.id.progressbar);
        initRecycleView();

        if (loaded) {
            progressbar.setVisibility(View.GONE);
        } else {
            progressbar.setVisibility(View.VISIBLE);
        }



        initFabButton();

        return rootView;
    }

    private void requestSingleConversation() {

        JsonObjectRequest singleConversationRequest = new JsonObjectRequest(Request.Method.GET,
                UrlEndpoints.getSingleConversationUrl(getArguments().getString("conversationID")),
                (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        conversation = JSONParser.parseSingleConversation(response);

                        setFabListener();

                        messages.clear();
                        messages.addAll(conversation.getMessages());

                        getActivity().setTitle(conversation.getSubject());

                        loaded = true;
                        mAdapter.notifyDataSetChanged();

                        //Update unread count in navigation drawer
                        mCallback.requestUnreadCount();


                        progressbar.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressbar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(rootView.findViewById(R.id.coordinatorLayout), getString(R.string.error_conversation), Snackbar.LENGTH_LONG);
                snackbar.setDuration(4000); // Gives false syntax error
                snackbar.setAction(getString(R.string.snackback_action_text), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestSingleConversation();
                    }
                });
                if (!snackbar.isShown()) {
                    snackbar.show();
                }
            }
        });

        singleConversationRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(singleConversationRequest);
    }

    private void initFabButton() {
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_reply_white_24dp));
    }

    private void setFabListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ComposeMessageFragment composeMessageFragment = new ComposeMessageFragment();
                transaction.replace(R.id.content_frame, composeMessageFragment);

                Bundle bundle = new Bundle();

                ArrayList<String> ids = new ArrayList<>();
                for (Participant p : conversation.getParticipants()) {
                    Log.i(TAG, "onClick: added " + p.getName());
                    ids.add(p.getId());
                }

                bundle.putStringArrayList("recipientIDs", ids);
                composeMessageFragment.setArguments(bundle);

                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    private void initRecycleView() {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Create the LayoutManager that holds all the views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new MessageRecyclerViewAdapter(messages);
        recyclerView.setAdapter(mAdapter);
    }
}
