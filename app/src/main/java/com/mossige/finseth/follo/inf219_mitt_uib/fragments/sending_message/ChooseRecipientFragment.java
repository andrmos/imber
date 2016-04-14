package com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.SectionIndexer;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseListRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.RecipientRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.models.RecipientGroup;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.PrivateConstants;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.SectionIndicator;
import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * Created by Follo on 20.03.2016.
 */
public class ChooseRecipientFragment extends Fragment {

    private static final String TAG = "ChooseRecipientFragment";

    private View rootView;
    private ProgressBar progressBar;
    private ProgressBar progressBarRecipient;

    private ArrayList<RecipientGroup> recipientGroups;
    private ArrayList<Recipient> recipients;
    private ArrayList<Recipient> choosenRecipients;

    //Dropdownlists
    private Spinner course_spinner;
    private Spinner group_spinner;
    private Spinner recipient_spinner;

    //Adapters to the dropdownlists above
    private ArrayAdapter<String> courseAdapter;
    private ArrayAdapter<String> groupAdapter;
    private ArrayAdapter<String> recipientsAdapter;

    private ArrayList<String> courseCodes;
    private ArrayList<String> groups;
    private ArrayList<String> recipients_string;

    private boolean loaded;
    private ArrayList<Course> courses;

    private ArrayList<String> nextLinks;

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public ChooseRecipientFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipientGroups = new ArrayList<>();

        courseCodes = new ArrayList<>();
        groups = new ArrayList<>();
        recipients = new ArrayList<>();
        recipients_string = new ArrayList<>();

        loaded = false;

        requestCourses();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.choose_recipient, container, false);
        getActivity().setTitle("Velg mottaker");

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBarRecipient = (ProgressBar) rootView.findViewById(R.id.progressBarRecipient);
        if (loaded) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        initRecycleView();
        initSpinners();


        return rootView;
    }

//    @Override
//    public void onPause() {
        // Cancel all recipients requests when navigating away from fragment
//        RequestQueueHandler.getInstance(getContext()).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
//            @Override
//            public boolean apply(Request<?> request) {
//                if (request.getTag() != null) {
//                    return request.getTag().equals("recipient");
//                }
//                return false;
//            }
//        });
//
//        super.onPause();
//    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView");
        super.onDestroyView();
    }

    private void initSpinners() {

        initCourseSpinner();
        initGroupSpinner();
//        initRecipientSpinner();

        course_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                int course_id = courses.get(parent.getSelectedItemPosition()).getId();
                requestRecipientGroups(course_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });

        group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RecipientGroup recipientGroup = recipientGroups.get(parent.getSelectedItemPosition());

                String url = UrlEndpoints.getRecipientsByGroup(null, recipientGroup.getId());
                requestRecipients(recipientGroup, url);

                progressBarRecipient.setVisibility(View.VISIBLE);

                // Clear content of recipients spinner, to allow filling with new recipients
                recipients_string.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initCourseSpinner() {
        course_spinner = (Spinner) rootView.findViewById(R.id.course_selector);

        courseAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, courseCodes);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        course_spinner.setAdapter(courseAdapter);
    }

    private void initGroupSpinner() {
        group_spinner = (Spinner) rootView.findViewById(R.id.group_selector);

        groupAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group_spinner.setAdapter(groupAdapter);
        group_spinner.setEnabled(false);
    }

    private void initRecipientSpinner() {
        recipient_spinner = (Spinner) rootView.findViewById(R.id.recepient_selector);

        recipientsAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, recipients_string);
        recipientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipient_spinner.setAdapter(recipientsAdapter);
        recipient_spinner.setEnabled(false);
    }

    private void initRecycleView() {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) rootView.findViewById(R.id.fastScroller);

        fastScroller.setRecyclerView(mainList);
        fastScroller.setScrollbarFadingEnabled(true);

        mainList.setOnScrollListener(fastScroller.getOnScrollListener());

        SectionTitleIndicator sectionTitleIndicator =
                (SectionTitleIndicator) rootView.findViewById(R.id.fast_scroller_section_title_indicator);

        fastScroller.setSectionIndicator(sectionTitleIndicator);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new RecipientRecyclerViewAdapter(recipients_string);
        mainList.setAdapter(mAdapter);
    }

    private void requestCourses() {

        final JsonArrayRequest coursesReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCoursesListUrl(), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    boolean filterInstituteCourses = false;
                    courses = JSONParser.parseAllCourses(response, filterInstituteCourses);

                    courseCodes.clear();
                    for (Course c : courses) {
                        courseCodes.add(c.getCourseCode());
                    }

                    courseAdapter.notifyDataSetChanged();
                    loaded = true;

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                }

                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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

                    groups.clear();
                    for (RecipientGroup g : recipientGroups) {
                        groups.add(g.getName());
                    }

                    groupAdapter.notifyDataSetChanged();
                    group_spinner.setEnabled(true);

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                showToast();
            }

        });

        recipientGroupReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(recipientGroupReq);
    }

    private void requestRecipients(final RecipientGroup rg, String url) {

        nextLinks = new ArrayList<>();

        final JsonArrayRequest recipientsReq = new JsonArrayRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.i(TAG, "onResponse");
                    ArrayList<Recipient> tmp = JSONParser.parseAllRecipients(response);

                    // If there exists a link to the next recipients page, start request with new url
                    if (nextLinks.size() > 0) {

                        // TODO Fetches ALL recipients in a group. Might be a lot of data to fetch.
                        // TODO Cancel the requests when going to a new activity/chooses a recipient to send to
                        requestRecipients(rg, nextLinks.get(0));
                    }

                    for (Recipient r : tmp) {
                        r.setGroup(rg.getName());
                        recipients_string.add(r.getName());
                    }

                    progressBarRecipient.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error.toString());
                // TODO Is run when canceling requests.
                // TODO Cannot show toast when in another fragment, so this will throw a nullpointer exception
                showToast();
            }

        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {

                // TODO alt 1:
                // - return super.parseNetworkResponse(response)
                // - Field boolean containsLink = true, when response.headers.containsKey("Link") or contains "rel=next" ??
                // - containsLink = false start of request method

                // TODO alt 2:
                // - return super.parseNetworkResponse(response)
                // - Field String page = null at start of request method
                // - page = response.headers.get("Link") -> parse to get "bookmark:x8x80x860"
                // - use page as parameter in request url

                // "rel=next" does not occur in the Link tag of the last page

                // TODO new link do not return JSONArray

                nextLinks.clear();

                String linkKey = "Link";
                if (response.headers.containsKey(linkKey)) {

                    String result = response.headers.get(linkKey);
                    String[] links = result.split(",");

                    for (int i = 0; i < links.length; i++) {
                        String line = links[i];
                        // If link contains rel=next
                        if (line.contains("rel=\"next\"")) {

                            // Get next link url and add access token
                            String link = line.substring(line.indexOf("<") + 1);
                            link = link.substring(0, link.indexOf(">"));
                            link += "&access_token=" + PrivateConstants.ACCESS_TOKEN;

                            nextLinks.add(link);
                        }
                    }
                }

                return super.parseNetworkResponse(response);
            }
        };

        recipientsReq.setTag("recipient");
        recipientsReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(recipientsReq);
    }

    private void showToast() {
        Toast.makeText(getContext(), R.string.error_conversation, Toast.LENGTH_SHORT).show();
    }
}
