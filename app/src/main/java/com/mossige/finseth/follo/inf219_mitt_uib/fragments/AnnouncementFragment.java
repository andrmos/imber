package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.AnnouncementRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;

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
    private ArrayList<Announcement> announcements;

    public AnnouncementFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        // Set label for toolbar
        getActivity().setTitle(getString(R.string.announcements_title) + " - " + getArguments().getString("course_code"));

        initRecycleView(rootView);

        initOnClickListener();

        return rootView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        announcements = new ArrayList<>();

        ArrayList<String> announcementIds = getArguments().getStringArrayList("announcementIds");
        ArrayList<String> announcementTitles = getArguments().getStringArrayList("announcementTitles");
        ArrayList<String> announcementSender = getArguments().getStringArrayList("announcementSender");
        ArrayList<String> announcementDates= getArguments().getStringArrayList("announcementDates");
        ArrayList<String> announcementMessages = getArguments().getStringArrayList("announcementMessages");

        for(int i = 0; i < announcementTitles.size(); i++){
            announcements.add(new Announcement(announcementIds.get(i),announcementTitles.get(i), announcementSender.get(i), announcementDates.get(i),announcementMessages.get(i),true));
        }
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);

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
