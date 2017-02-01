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
import android.widget.Spinner;
import android.widget.Toast;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.RecipientRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.EndlessRecyclerViewScrollListener;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Recipient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.PaginationUtils;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.CancelableCallback;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Follo on 20.03.2016.
 */
public class ChooseRecipientFragment extends Fragment {

    private static final String TAG = "ChooseRecipientFragment";

    private View rootView;

    private SmoothProgressBar progressBar;

    private ArrayList<Recipient> recipients;

    private ArrayList<Course> courses;
    private ArrayList<String> courseCodes;
    //Adapter to course spinner
    private ArrayAdapter<String> courseAdapter;

    private boolean loaded;

    private String nextPage;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private HashMap<String,Boolean> recipientsChecked;
    private MainActivityListener mCallback;

    private int courseId;
    private MittUibClient mittUibClient;
    private EndlessRecyclerViewScrollListener endlessScrollListener;

    public ChooseRecipientFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (MainActivityListener) context;
        } catch (ClassCastException e) {
            //Do nothing
        }

        mittUibClient = ServiceGenerator.createService(MittUibClient.class, context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_menu, menu);
        initSearchView(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.send) {
            initComposeMessageFragment();
            return true;
        }

        if (id == R.id.search) {
            // Do nothing
            return true;
        }

        return false;
    }

    private void initComposeMessageFragment() {
        Iterator hashMapIterator = recipientsChecked.entrySet().iterator();
        ArrayList<String> tmpList = new ArrayList<>();

        //Make arraylist with ids from hashmap
        while (hashMapIterator.hasNext()) {
            Map.Entry<String, Boolean> pair = (Map.Entry<String, Boolean>) hashMapIterator.next();

            if (pair.getValue()) {
                tmpList.add(pair.getKey());
            }
        }

        //If recipients are choosen, write new message. If not show toast message
        if(tmpList.size() > 0) {
            initComposeMessageFragment(tmpList);
        }else{
            Toast.makeText(getContext(), R.string.no_recipients_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void initComposeMessageFragment(ArrayList<String> tmpList) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        ComposeMessageFragment composeMessageFragment = new ComposeMessageFragment();
        transaction.replace(R.id.content_frame, composeMessageFragment);

        transaction.addToBackStack(null);

        //Bundles all parameters needed for showing one announcement
        Bundle args = new Bundle();
        args.putStringArrayList("recipientIDs", tmpList);
        composeMessageFragment.setArguments(args);

        transaction.commit();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        courses = new ArrayList<>();
        courseCodes = new ArrayList<>();
        recipients = new ArrayList<>();
        recipientsChecked = new HashMap<>();

        loaded = false;

        nextPage = "";
        requestCourses();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.choose_recipient, container, false);
        getActivity().setTitle("Velg mottaker");
        initCourseSpinner();
        initRecycleView();

        progressBar = (SmoothProgressBar) rootView.findViewById(R.id.progressbar);
        if (loaded) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void initSearchView(Menu menu) {
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setVisibility(View.VISIBLE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return onQueryTextChange(query);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Cancel old request
                if (newText.length() > 1) {
                    String oldTag = newText.substring(0, newText.length() - 1);
                    CancelableCallback.cancel(oldTag);
                }

                endlessScrollListener.resetState();
                // Clear list to fill with new data
                int size = mAdapter.getItemCount();
                recipients.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
                requestRecipients(newText, true);

                return true;
            }
        });
    }


    @Override
    public void onPause() {
        CancelableCallback.cancelAll();
        super.onPause();
    }

    private void initCourseSpinner() {
        Spinner courseSpinner = (Spinner) rootView.findViewById(R.id.course_selector);

        courseAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, courseCodes);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                CancelableCallback.cancelAll();
                int size = mAdapter.getItemCount();
                recipients.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
                endlessScrollListener.resetState();

                courseId = courses.get(parent.getSelectedItemPosition()).getId();
                requestRecipients("", true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });
    }

    private void initRecycleView() {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Create the LayoutManager that holds all the views
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.addOnScrollListener(initEndlessScrollListener(mLayoutManager));

        // Create adapter that binds the views with some content
        mAdapter = new RecipientRecyclerViewAdapter(recipients);
        recyclerView.setAdapter(mAdapter);

        initOnClickListener();
    }

    private EndlessRecyclerViewScrollListener initEndlessScrollListener(final LinearLayoutManager mLayoutManager) {
        endlessScrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!nextPage.isEmpty()) {
                    requestRecipients("", false);
                }
            }
        };
        return endlessScrollListener;
    }


    private void requestCourses() {
        Call<List<Course>> call = mittUibClient.getCourses(null);
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, retrofit2.Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    courses.clear();
                    courses.addAll(response.body());
                    courseCodes.clear();
                    for (Course c : courses) {
                        courseCodes.add(c.getCourseCode());
                    }

                    courseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                if(progressBar != null){
                    progressBar.setVisibility(View.GONE);
                }

                mCallback.showSnackbar(getString(R.string.error_course_list), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestCourses();
                    }
                });
            }
        });
    }

    private void requestRecipients(final String searchTerms, final boolean firstPage) {
        String context = "course_" + courseId;

        Call<List<Recipient>> call;
        if (firstPage) {
            call = mittUibClient.getRecipients(searchTerms, context);
        } else {
            call = mittUibClient.getRecipientsPagination(nextPage);
        }

        call.enqueue(new CancelableCallback<List<Recipient>>(searchTerms) {
            @Override
            public void onSuccess(Call<List<Recipient>> call, retrofit2.Response<List<Recipient>> response) {
                if (response.isSuccessful()) {
                    int currentSize = mAdapter.getItemCount();

                    // Check already checked recipients
                    for (Recipient r : response.body()) {
                        if (recipientsChecked.containsKey(r.getId())) {
                            r.setChecked(recipientsChecked.get(r.getId()));
                        }
                        recipients.add(r);
                    }

                    progressBar.setVisibility(View.GONE);
                    mAdapter.notifyItemRangeInserted(currentSize, recipients.size());

                    loaded = true;

                    nextPage = PaginationUtils.getNextPageUrl(response.headers());

                } else {
                    showSnackbar(searchTerms, firstPage);
                }
            }

            @Override
            public void onError(Call<List<Recipient>> call, Throwable t) {
                showSnackbar(searchTerms, firstPage);
            }
        });
    }

    private void showSnackbar(final String searchTerms, final boolean firstPage) {
        if(progressBar != null){
            progressBar.setVisibility(View.GONE);
        }

        mCallback.showSnackbar(getString(R.string.error_loading_recipients), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestRecipients(searchTerms, firstPage);
            }
        });
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);

                // Ensure position is not out of bounds
                if (position >= 0 && position < recipients.size()) {
                    boolean checked = !checkBox.isChecked();
                    checkBox.setChecked(checked);
                    recipients.get(position).setChecked(checked);
                    recipientsChecked.put(recipients.get(position).getId(), checked);
                }
            }
        });
    }
}
