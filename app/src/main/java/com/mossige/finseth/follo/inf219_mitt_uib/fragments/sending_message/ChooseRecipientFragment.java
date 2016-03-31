package com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
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

/**
 * Created by Follo on 20.03.2016.
 */
public class ChooseRecipientFragment extends Fragment {

    private static final String TAG = "ChooseRecipientFragment";

    private View rootView;
    private ProgressBar progressBar;

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

    private int course_counter_check;
    private int group_counter_check;
    private int recipient_counter_check;

    private ArrayList<String> nextLinks;

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
        if (loaded) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        initSpinners();


        return rootView;
    }

    private void initSpinners() {

        initCourseSpinner();
        initGroupSpinner();
        initRecipientSpinner();

        course_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                Toast.makeText(parent.getContext(), "OnItemSelectedListener" + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_SHORT).show();

                // TODO not able to select default course on first

                course_counter_check++;
                // Keeps listener from running when initialized
                if (course_counter_check > 1) {
                    int course_id = courses.get(parent.getSelectedItemPosition()).getId();
                    requestRecipientGroups(course_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });

        group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                group_counter_check++;
                if (group_counter_check > 1) {
                    RecipientGroup recipientGroup = recipientGroups.get(parent.getSelectedItemPosition());

                    String url = UrlEndpoints.getRecipientsByGroup(null, recipientGroup.getId());
                    requestRecipients(recipientGroup, url);

                    // Clear content of recipients spinner, to allow filling with new recipients
                    recipients_string.clear();
                }
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

        course_counter_check = 0;
    }

    private void initGroupSpinner() {
        group_spinner = (Spinner) rootView.findViewById(R.id.group_selector);

        groupAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group_spinner.setAdapter(groupAdapter);
        group_spinner.setEnabled(false);

        group_counter_check = 0;
    }

    private void initRecipientSpinner() {
        recipient_spinner = (Spinner) rootView.findViewById(R.id.recepient_selector);

        recipientsAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, recipients_string);
        recipientsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipient_spinner.setAdapter(recipientsAdapter);
        recipient_spinner.setEnabled(false);

        recipient_counter_check = 0;
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

                    course_spinner.setSelection(2);

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

                    ArrayList<Recipient> tmp = JSONParser.parseAllRecipients(response);

                    // If there exists a link to the next 50 recipients, start request with new url
                    if (nextLinks.size() > 0) {

                        // TODO This fetches ALL recipients in a group. Might be a lot of data to fetch.
                        // TODO Cancel the requests when going to a new activity/chooses a recipient to send to
                        requestRecipients(rg, nextLinks.get(0));
                    }

                    for (Recipient r : tmp) {
                        r.setGroup(rg.getName());
                        recipients_string.add(r.getName());
                    }

                    recipientsAdapter.notifyDataSetChanged();
                    recipient_spinner.setEnabled(true);

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

                // Print links
                for (int i = 0; i < nextLinks.size(); i++) {
                    Log.i(TAG, nextLinks.get(i));
                }

                return super.parseNetworkResponse(response);
            }
        };

        recipientsReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(recipientsReq);
    }

    private void showToast() {
        Toast.makeText(getContext(), R.string.error_conversation, Toast.LENGTH_SHORT).show();
    }

}
