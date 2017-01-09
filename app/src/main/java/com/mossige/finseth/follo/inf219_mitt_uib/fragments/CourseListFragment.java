package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseListRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.ServiceGenerator;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A placeholder fragment containing a simple view.
 */
public class CourseListFragment extends Fragment {

    private static final String TAG = "CourseListFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<Course> courses;
    private SmoothProgressBar smoothProgressBar;

    /* If data is loaded */
    private boolean loaded;

    MainActivityListener mCallback;

    public CourseListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loaded = false;
        courses = new ArrayList<>();

        requestCourses();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (MainActivityListener) context;
        }catch (ClassCastException e){
            Log.i(TAG, "onAttach: " + e.toString());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        getActivity().setTitle(R.string.course_list_title);

        smoothProgressBar = (SmoothProgressBar) rootView.findViewById(R.id.progressbar);
        initRecycleView(rootView);

        // Hide progress bar if data is already loaded
        if (loaded) {
            smoothProgressBar.setVisibility(View.GONE);
        } else {
            smoothProgressBar.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mainList.setVisibility(View.VISIBLE);

        // Create the LayoutManager that holds all the views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new CourseListRecyclerViewAdapter(courses);
        mainList.setAdapter(mAdapter);

        initOnClickListener();
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                CourseFragment courseFragment = new CourseFragment();
                transaction.replace(R.id.content_frame, courseFragment);

                transaction.addToBackStack(null);

                Bundle bundle = new Bundle();
                bundle.putInt("id", courses.get(position).getId());
                bundle.putString("calendar_url", courses.get(position).getCalenderUrl());
                bundle.putString("name", courses.get(position).getName());
                bundle.putString("course_code", courses.get(position).getCourseCode());
                courseFragment.setArguments(bundle);

                transaction.commit();
            }
        });
    }

    private void requestCourses() {

        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());
        Call<List<Course>> call = client.getFavoriteCourses();
        call.enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, retrofit2.Response<List<Course>> response) {
                if (response.isSuccessful()) {
                    courses.clear();
                    courses.addAll(response.body());

                    loaded = true;
                    if (mAdapter != null) mAdapter.notifyDataSetChanged();

                    if (smoothProgressBar != null) smoothProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                if (smoothProgressBar != null) smoothProgressBar.setVisibility(View.GONE);
                mCallback.showSnackbar(getString(R.string.error_course_list), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestCourses();
                    }
                });
            }
        });

    }
}
