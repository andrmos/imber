package com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.fragments.ConversationFragment;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.SendMessage;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.ServiceGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

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

    MainActivityListener mCallback;

    public ComposeMessageFragment() { }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        menu.findItem(R.id.search).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.send) {
            item.setEnabled(false);
            hideSoftKeyboard(rootView);
            postMessageRequest(getArguments().getStringArrayList("recipientIDs"), subject.getText().toString(), body.getText().toString());
            return true;
        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_compose_message, container, false);

        setHasOptionsMenu(true);

        //TODO add group checkbox
        subject = (EditText) rootView.findViewById(R.id.subject);
        body = (EditText) rootView.findViewById(R.id.message_body);

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MainActivityListener) context;
        }catch (ClassCastException e){
            //Do nothing
        }
    }

    private void cleanTextFields(){
        subject.setText("");
        body.setText("");
    }

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void postMessageRequest(final ArrayList<String> recipients, final String subject, final String body) {

        if (validateMessage(subject, body)) {
            SendMessage message = new SendMessage(subject, body, recipients);

            MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());
            Call<List<SendMessage>> call = client.createConversation(message);
            call.enqueue(new Callback<List<SendMessage>>() {
                @Override
                public void onResponse(Call<List<SendMessage>> call, retrofit2.Response<List<SendMessage>> response) {
                    if (response.isSuccessful()) {
                        mCallback.showSnackbar(getString(R.string.message_sent), null);
                        cleanTextFields();
                        replaceFragment();
                    } else {
                        showSnackbar(recipients, subject, body);
                    }
                }

                @Override
                public void onFailure(Call<List<SendMessage>> call, Throwable t) {
                    showSnackbar(recipients, subject, body);
                }
            });

        }

    }

    private void showSnackbar(final ArrayList<String> recipients, final String subject, final String body) {
        mCallback.showSnackbar(getString(R.string.error_sending_message), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postMessageRequest(recipients,subject,body);
            }
        });
    }

    private void replaceFragment (){
        FragmentManager fm = getActivity().getSupportFragmentManager();
        boolean popped = fm.popBackStackImmediate("inbox", 0);

        if (!popped){
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, new ConversationFragment());
            ft.commit();
        }

    }

}
