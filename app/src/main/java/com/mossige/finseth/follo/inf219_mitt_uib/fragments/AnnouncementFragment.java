package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.AnnouncementRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CourseMenuRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;

import java.util.ArrayList;

/**
 * Fragment for announcement listing
 *
 * Created by Patrick Finseth on 13.03.16
 */
public class AnnouncementFragment extends Fragment {

    private static final String TAG = "AnnouncementFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private View rootView;
    private ArrayList<String> announcements;

    public AnnouncementFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_course_list, container, false);

        announcements = new ArrayList<>();

        getActivity().setTitle("Kunngjøringer");

        announcements = getArguments().getStringArrayList("announcements");

        initRecycleView(rootView);

        initOnClickListener();

        return rootView;

    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.mainList);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new AnnouncementRecyclerViewAdapter(announcements);
        mainList.setAdapter(mAdapter);

    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SingleAnnouncementFragment singleAnnouncementFragment = new SingleAnnouncementFragment();
                transaction.replace(R.id.content_frame, singleAnnouncementFragment);

                Bundle args = new Bundle();
                singleAnnouncementFragment.setArguments(args);

                transaction.commit();
            }
        });
    }

}
