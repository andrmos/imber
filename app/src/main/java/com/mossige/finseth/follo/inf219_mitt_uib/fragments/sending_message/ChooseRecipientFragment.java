package com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
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
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.RecipientRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ShowSnackbar;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.models.RecipientGroup;
import com.mossige.finseth.follo.inf219_mitt_uib.network.JSONParser;
import com.mossige.finseth.follo.inf219_mitt_uib.network.PrivateConstants;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;
import com.mossige.finseth.follo.inf219_mitt_uib.network.UrlEndpoints;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

    //Dropdownlists
    private Spinner course_spinner;
    private Spinner group_spinner;

    //Adapters to the dropdownlists above
    private ArrayAdapter<String> courseAdapter;
    private ArrayAdapter<String> groupAdapter;

    private ArrayList<String> courseCodes;
    private ArrayList<String> groups;

    private boolean loaded;
    private ArrayList<Course> courses;

    private ArrayList<String> nextLinks;

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private HashMap<String,Boolean> recipientsChecked;

    ShowSnackbar.ShowToastListener mCallback;
    private int courseId;
    private RecipientGroup recipientGroup;

    public ChooseRecipientFragment() {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.send) {
            Iterator hashMapIterator = recipientsChecked.entrySet().iterator();
            ArrayList<String> tmpList = new ArrayList<String>();

            //Make arraylist with ids from hashmap
            while (hashMapIterator.hasNext()) {
                Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) hashMapIterator.next();

                if (pair.getValue()) {
                    tmpList.add(pair.getKey());
                    Log.i(TAG, "onClick: added " + pair.getKey());
                }
            }

            //If recipients are choosen write new message. If not show toast message
            if(tmpList.size() > 0) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ComposeMessageFragment composeMessageFragment = new ComposeMessageFragment();
                transaction.replace(R.id.content_frame, composeMessageFragment);

                transaction.addToBackStack(null);

                //Bundles all parameters needed for showing one announcement
                Bundle args = new Bundle();
                args.putStringArrayList("recipientIDs", tmpList);
                composeMessageFragment.setArguments(args);


                transaction.commit();
            }else{
                Toast.makeText(getContext(), "Ingen mottakere valgt", Toast.LENGTH_SHORT).show();
            }

            return true;
        }


        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        recipientGroups = new ArrayList<>();
        courses = new ArrayList<>();
        courseCodes = new ArrayList<>();
        groups = new ArrayList<>();
        recipients = new ArrayList<>();
        recipientsChecked = new HashMap<>();

        loaded = false;

        requestCourses();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (ShowSnackbar.ShowToastListener) context;
        } catch (ClassCastException e) {
            Log.i(TAG, "Class cast exception");
        }
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
        initSearchView();

        return rootView;
    }

    private void initSearchView() {
        SearchView searchView = (SearchView) rootView.findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return onQueryTextChange(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (recipientGroup != null) {
                    String url = UrlEndpoints.getRecipientsByGroup(newText, recipientGroup.getId());

                    // Clear results to fill with new data
                    recipients.clear();

                    // ÆØÅ Not supported by API
                    requestRecipients(recipientGroup, url, newText);

                } else {
                    Log.i(TAG, "onQueryTextChange: recipientGroup is null");
                }

                // Do not cancel requests with empty tag
                if (newText.length() > 1) {
                    String oldTag = newText.substring(0, newText.length() - 1);
                    // Cancel old request
                    cancelRequest(oldTag);
                }
                return true;
            }
        });


    }


    @Override
    public void onPause() {
        // Cancel all recipients requests when navigating away from fragment
        cancelRequest("recipient");

        super.onPause();
    }

    private void cancelRequest(final String tag) {
        RequestQueueHandler.getInstance(getContext()).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                if (request.getTag() != null) {
                    return request.getTag().equals(tag);
                }
                return false;
            }
        });
    }

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
    }

    private void initCourseSpinner() {
        course_spinner = (Spinner) rootView.findViewById(R.id.course_selector);

        // TODO Make custom adapter so we don't need duplicate ArrayLists with String courseCodes
        courseAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, courseCodes);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        course_spinner.setAdapter(courseAdapter);

        course_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                courseId = courses.get(parent.getSelectedItemPosition()).getId();
                requestRecipientGroups(courseId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "onNothingSelected: ");
            }
        });
    }

    private void initGroupSpinner() {
        group_spinner = (Spinner) rootView.findViewById(R.id.group_selector);

        groupAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, groups);
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        group_spinner.setAdapter(groupAdapter);
        group_spinner.setEnabled(false);

        group_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recipientGroup = recipientGroups.get(parent.getSelectedItemPosition());
                String url = UrlEndpoints.getRecipientsByGroup(null, recipientGroup.getId());


                requestRecipients(recipientGroup, url, "recipient");

                progressBarRecipient.setVisibility(View.VISIBLE);

                // Clear content of recipients spinner, to allow filling with new recipients
                recipients.clear();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initRecycleView() {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new RecipientRecyclerViewAdapter(recipients);
        mainList.setAdapter(mAdapter);

        initOnClickListener();
    }

    private void requestCourses() {

        final JsonArrayRequest coursesReq = new JsonArrayRequest(Request.Method.GET, UrlEndpoints.getCoursesListUrl(), (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    boolean filterInstituteCourses = false;
                    courses = JSONParser.parseAllCourses(response, filterInstituteCourses, getContext());

                    courseCodes.clear();
                    for (Course c : courses) {
                        courseCodes.add(c.getCourseCode());
                    }

                    courseAdapter.notifyDataSetChanged();
                    loaded = true;

                } catch (JSONException e) {
                    // TODO handle exception
                    Log.i(TAG, "JSONException");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                //mCallback.showSnackbar(getString(R.string.error_course_list));
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
                //mCallback.showSnackbar("Error requesting recipient groups");
            }

        });

        recipientGroupReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(recipientGroupReq);
    }

    private void requestRecipients(final RecipientGroup rg, String url, String tag) {

        nextLinks = new ArrayList<>();

        final JsonArrayRequest recipientsReq = new JsonArrayRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<Recipient> tmp = JSONParser.parseAllRecipients(response);

                    // If there exists a link to the next recipients page, start request with new url
                    if (nextLinks.size() > 0) {
                        // TODO Fetches ALL recipients in a group. Might be a lot of data to fetch.
                        // TODO Cancel the requests when going to a new activity/chooses a recipient to send to
                        requestRecipients(rg, nextLinks.get(0), "recipient");
                    }

                    for (Recipient r : tmp) {
                        r.setGroup(rg.getName());
                        if (recipientsChecked.containsKey(r.getId())) {
                            r.setChecked(recipientsChecked.get(r.getId()));
                        }
                        recipients.add(r);
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
                //mCallback.showSnackbar("Error requesting recipients");
            }

        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                // TODO nextLinks should be a String
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

        recipientsReq.setTag(tag);
        recipientsReq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueueHandler.getInstance(getContext()).addToRequestQueue(recipientsReq);
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
                boolean checked = !checkBox.isChecked();
                checkBox.setChecked(checked);
                recipients.get(position).setChecked(checked);
                recipientsChecked.put(recipients.get(position).getId(), checked);
            }
        });
    }
}
