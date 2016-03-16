package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.AnnouncementRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;

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
    private ArrayList<String> announcementTitles;
    private ArrayList<String> announcementMessages;
    private ArrayList<String> announcementSender;
    private ArrayList<String> announcementDates;

    public AnnouncementFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        announcementTitles = new ArrayList<>();
        announcementMessages = new ArrayList<>();
        announcementSender = new ArrayList<>();
        announcementDates = new ArrayList<>();

        // Set label for toolbar
        getActivity().setTitle(R.string.announcements_title);

        announcementTitles = getArguments().getStringArrayList("announcementTitles");
        announcementMessages = getArguments().getStringArrayList("announcementMessages");
        announcementSender = getArguments().getStringArrayList("announcementSender");
        announcementDates = getArguments().getStringArrayList("announcementDates");

        initRecycleView(rootView);

        initOnClickListener();

        return rootView;

    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new AnnouncementRecyclerViewAdapter(announcementTitles);
        mainList.setAdapter(mAdapter);

    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                SingleAnnouncementFragment singleAnnouncementFragment = new SingleAnnouncementFragment();
                transaction.replace(R.id.content_frame, singleAnnouncementFragment);

                transaction.addToBackStack(null);

                //Bundles all parameters needed for showing one announcement
                Bundle args = new Bundle();
                args.putString("title", getArguments().getStringArrayList("announcementTitles").get(position));
                args.putString("message", getArguments().getStringArrayList("announcementMessages").get(position));
                args.putString("sender", getArguments().getStringArrayList("announcementSender").get(position));
                args.putString("date", getArguments().getStringArrayList("announcementDates").get(position));
                singleAnnouncementFragment.setArguments(args);

                transaction.commit();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
