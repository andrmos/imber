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
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.EndlessRecyclerViewScrollListener;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;
import com.mossige.finseth.follo.inf219_mitt_uib.network.HeaderLinksHelper;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Fragment for announcement listing
 *
 * Created by Patrick Finseth on 13.03.16
 */
public class AnnouncementFragment extends Fragment {

    private static final String TAG = "AnnouncementFragment";

    private RecyclerView mainList;

    private ArrayList<Announcement> announcements;
    private String nextPage;
    private RecyclerView.Adapter mAdapter;
    private int courseId;

    public AnnouncementFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        // Set label for toolbar
        getActivity().setTitle(getString(R.string.announcements_title) + " - " + getArguments().getString("courseCode"));

        initRecycleView(rootView);

        initOnClickListener();

        return rootView;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        announcements = new ArrayList<>();
        nextPage = "";
        courseId = getArguments().getInt("courseId");
        requestAnnouncements(courseId);
    }

    private void requestAnnouncements(final int course_id) {
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());

        Call<List<Announcement>> call;
        boolean firstPage = nextPage.isEmpty();
        if (firstPage) {
            call = client.getAnnouncements(course_id);
        } else {
            call = client.getAnnouncementsPagination(nextPage);
        }

        call.enqueue(new Callback<List<Announcement>>() {
            @Override
            public void onResponse(Call<List<Announcement>> call, retrofit2.Response<List<Announcement>> response) {

                if (response.isSuccessful()) {
                    int currentSize = mAdapter.getItemCount();
                    announcements.addAll(response.body());
                    mAdapter.notifyItemRangeInserted(currentSize, response.body().size());

                    nextPage = HeaderLinksHelper.getNextPageUrl(response.headers().get("Link"));

                    mainList.setVisibility(View.VISIBLE);
                } else {
                    // TODO
                }

            }

            @Override
            public void onFailure(Call<List<Announcement>> call, Throwable t) {
                // TODO Implement
//                mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        requestAnnouncements(course_id);
//                    }
//                });
            }
        });
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // Create the LayoutManager that holds all the views
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        mainList.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // If there is a next link
                if (!nextPage.isEmpty()) {
                    requestAnnouncements(courseId);
                }
            }
        });

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
                Announcement announcement = announcements.get(position);
                args.putString("title", announcement.getTitle());
                args.putString("message", announcement.getMessage());
                args.putString("userName", announcement.getUserName());
                args.putString("postedAt", announcement.getPostedAt());
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
