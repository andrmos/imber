package com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message;

import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.RecipientGroup;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.PrivateConstants;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by folb 31.03.16
 */
public class ComposeMessageFragment extends Fragment {

    private static final String TAG = "ComposeMessageFragment";

    private View rootView;

    private EditText subject;
    private EditText message;
    private Button sendMessageBtn;

    public ComposeMessageFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_compose_message, container, false);

        sendMessageBtn = (Button) rootView.findViewById(R.id.sendButton);

        //Set click listener for send message button
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject = (EditText) rootView.findViewById(R.id.subject);
                message = (EditText) rootView.findViewById(R.id.message);

                try {

                    //Call method with fields and arguments
                    postMessageRequest(getArguments().getStringArrayList("recipientIDs"),subject.getText().toString(),message.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    private void postMessageRequest(final ArrayList<String> recipients, final String subject, final String message) throws JSONException {

        //Create json onject
        JSONObject obj = new JSONObject();
        obj.put("recipients[]",recipients.get(0));
        obj.put("subject", subject);
        obj.put("body", message);

        //Request post method
        final JsonObjectRequest postMessage = new JsonObjectRequest(Request.Method.POST, UrlEndpoints.postNewMessageUrl(), obj, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "onResponse: got resonse" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.toString());
            }
        });


        //Add to queue
        RequestQueueHandler.getInstance(this.getContext()).addToRequestQueue(postMessage);
    }

}
