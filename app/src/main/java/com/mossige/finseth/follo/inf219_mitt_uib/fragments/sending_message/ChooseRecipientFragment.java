package com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.ChooseRecipientViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.models.RecipientGroup;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Follo on 20.03.2016.
 */
public class ChooseRecipientFragment extends Fragment {

    private static final String TAG = "ChooseRecipientFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View rootView;
    private ProgressBar progressBar;

    private Spinner spinner;

    private ArrayList<Course> courses;
    private ArrayList<RecipientGroup> recipientGroups;
    private ArrayList<Recipient> recipients;

    public ChooseRecipientFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.choose_recipient, container, false);

        courses = new ArrayList<>();
        recipientGroups = new ArrayList<>();
        recipients = new ArrayList<>();

        getActivity().setTitle("Velg mottaker");

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        requestCourses();
       // initRecycleView();


        return rootView;
    }

    private void initSpinner() {
        spinner = (Spinner) rootView.findViewById(R.id.course_selector);

        List<String> courseCodes = new ArrayList<>();

        for (Course c : courses) {
            courseCodes.add(c.getCourseCode());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, courseCodes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                Toast.makeText(parent.getContext(), "OnItemSelectedListener" + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();
                loadGroups(courses.get(parent.getSelectedItemPosition()).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void requestCourses() {
//        progressBar.setVisibility(View.VISIBLE);

        final JsonArrayRequest coursesReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCoursesListUrl(), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    courses.clear();
                    recipientGroups.clear();
                    recipients.clear();

                    ArrayList<Course> temp = JSONParser.parseAllCourses(response, true);
                    for (Course c : temp) {
                        courses.add(c);
                    }

                    Log.i(TAG, "onResponse: courses size = " + courses.size());

                    initSpinner();

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                }

//                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                showToast();
            }

        });

        coursesReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(coursesReq);
    }

    private void requestRecipientGroups(final int courseID) {

        final JsonArrayRequest recipientGroupReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getRecipientGroups(courseID), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    recipientGroups.clear();
                    recipientGroups = JSONParser.parseAllRecipientGroups(response);

                    loadParticipants();

                    Log.i(TAG, "onResponse: groups parsed " + recipientGroups.size());

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                }

//                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                error.printStackTrace();
                showToast();
            }

        });

        recipientGroupReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(recipientGroupReq);
    }

    private void loadParticipants() {
        Log.i(TAG, "loadParticipants: starting");

        recipients.clear();

        for (RecipientGroup r : recipientGroups) {
            requestRecipients(r);
        }
    }

    private void requestRecipients(final RecipientGroup rg) {
//        progressBar.setVisibility(View.VISIBLE);

        Log.i(TAG, "requestRecipients: " + rg.getId());

        JsonArrayRequest recipientsReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getRecipientsByGroup(null, rg.getId()), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    ArrayList<Recipient> tmp = JSONParser.parseAllRecipients(response);

                    for (Recipient r : tmp) {
                        r.setGroup(rg.getName());
                        Log.i(TAG, "onResponse: Recipient " + r.getName());
                    }

                    recipients.addAll(tmp);

                    Log.i(TAG, "onResponse: recipients parsed " + recipients.size());

                    initRecycleView();
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                    e.printStackTrace();
                }

//                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
                showToast();
            }

        });

        recipientsReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(recipientsReq);
    }

    private void loadGroups(int id) {
        Log.i(TAG, "loadGroups: starting ");
        requestRecipientGroups(id);
    }

    private void showToast() {
        Toast.makeText(getContext(), R.string.error_conversation, Toast.LENGTH_SHORT).show();
    }

    private void initRecycleView() {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Create the LayoutManager that holds all the views
        Log.i(TAG, "initRecycleView: " + getActivity().toString());
        mLayoutManager = new LinearLayoutManager(getActivity());
        Log.i(TAG, "initRecycleView: " + mLayoutManager.toString());
        Log.i(TAG, "initRecycleView: " + mainList);
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new ChooseRecipientViewAdapter(recipients);
        mainList.setAdapter(mAdapter);

        initOnClickListener();
    }


    private void initOnClickListener() {
//        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
//            @Override
//            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                SingleConversationFragment singleConversationFragment = new SingleConversationFragment();
//                transaction.replace(R.id.content_frame, singleConversationFragment);
//
//                transaction.addToBackStack(null);
//
//                //Bundles all parameters needed for showing one announcement
//                Bundle args = new Bundle();
//                args.putString("conversationID", conversationIDs.get(position));
//                singleConversationFragment.setArguments(args);
//
//                transaction.commit();
//
//            }
//        });
    }
}
