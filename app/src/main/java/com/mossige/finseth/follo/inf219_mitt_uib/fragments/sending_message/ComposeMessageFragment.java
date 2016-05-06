package com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by folb 31.03.16
 */
public class ComposeMessageFragment extends Fragment {

    private static final String TAG = "ComposeMessageFragment";
    public static final int SUBJECT_MAX_LENGTH = 255;
    public static final int BODY_MIN_Length = 1;

    private View rootView;

    private EditText subject;
    private EditText body;
    private TextInputLayout subjectInputLayout;
    private TextInputLayout bodyInputLayout;

    public ComposeMessageFragment() { }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.send) {
            postMessageRequest(getArguments().getStringArrayList("recipientIDs"),subject.getText().toString(), body.getText().toString());
        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_compose_message, container, false);

        setHasOptionsMenu(true);

        //TODO add group checkbox
        subject = (EditText) rootView.findViewById(R.id.subject);
        body = (EditText) rootView.findViewById(R.id.body);

        subjectInputLayout = (TextInputLayout)rootView.findViewById(R.id.subject_input_layout);
        subjectInputLayout.setErrorEnabled(true);

        bodyInputLayout = (TextInputLayout) rootView.findViewById(R.id.body_input_layout);
        bodyInputLayout.setErrorEnabled(true);

        return rootView;
    }

    private boolean validateMessage(String subject, String body) {
        boolean subjectOk = subject.length() <= SUBJECT_MAX_LENGTH;
        boolean bodyOk = body.length() >= BODY_MIN_Length;

        if (!subjectOk) {
            subjectInputLayout.setError(getString(R.string.subject_length_error));
        }
        if (!bodyOk) {
            bodyInputLayout.setError(getString(R.string.body_length_error));
        }

        return subjectOk && bodyOk;
    }


    private void postMessageRequest(final ArrayList<String> recipients, final String subject, final String body) {

        if (validateMessage(subject, body)) {

            try {

                //Create json object
                JSONObject postJSONObject = new JSONObject();
                postJSONObject.put("body", body);
                postJSONObject.put("subject", subject);
                //Accumulate all recipients
                for (int i = 0; i < recipients.size(); i++) {
                    postJSONObject.accumulate("recipients", recipients.get(i));
                }
                //Request post method
                JsonArrayRequest postMessage = new JsonArrayRequest(Request.Method.POST, UrlEndpoints.postNewMessageUrl(), postJSONObject, new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "onErrorResponse: " + error.toString());
                    }
                });

                RequestQueueHandler.getInstance(this.getContext()).addToRequestQueue(postMessage);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }

}
