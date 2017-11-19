package no.mofifo.imber.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import no.mofifo.imber.R;
import no.mofifo.imber.adapters.AnnouncementRecyclerViewAdapter;
import no.mofifo.imber.listeners.EndlessRecyclerViewScrollListener;
import no.mofifo.imber.listeners.ItemClickSupport;
import no.mofifo.imber.listeners.MainActivityListener;
import no.mofifo.imber.models.Announcement;
import no.mofifo.imber.retrofit.MittUibClient;
import no.mofifo.imber.retrofit.PaginationUtils;
import no.mofifo.imber.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Fragment for announcement listing
 * <p>
 * Created by Patrick Finseth on 13.03.16
 */
public class AnnouncementFragment extends Fragment {

    private static final String TAG = "AnnouncementFragment";

    private RecyclerView mainList;

    private ArrayList<Announcement> announcements;
    private String nextPage;
    private RecyclerView.Adapter mAdapter;
    private int courseId;
    private MainActivityListener mCallback;
    private MittUibClient mittUibClient;
    private boolean loaded;
    private SmoothProgressBar progressBar;

    public AnnouncementFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        progressBar = (SmoothProgressBar) rootView.findViewById(R.id.progressBar);
        // Hide progress bar if data is already loaded
        if (loaded) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        // Set label for toolbar
        getActivity().setTitle(getString(R.string.announcements_title));

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (MainActivityListener) context;
        } catch (ClassCastException e) {
            Log.i(TAG, "onAttach: " + e.toString());
        }
        mittUibClient = ServiceGenerator.createService(MittUibClient.class, context);
    }

    private void requestAnnouncements(final int course_id) {
        Call<List<Announcement>> call;
        boolean firstPage = nextPage.isEmpty();
        if (firstPage) {
            call = mittUibClient.getAnnouncements(course_id);
        } else {
            call = mittUibClient.getAnnouncementsPagination(nextPage);
        }

        call.enqueue(new Callback<List<Announcement>>() {
            @Override
            public void onResponse(Call<List<Announcement>> call, retrofit2.Response<List<Announcement>> response) {

                if (isAdded()) {

                    if (!loaded) {
                        progressBar.progressiveStop();
                    }

                    if (response.isSuccessful()) {
                        if (mAdapter != null) {
                            int currentSize = mAdapter.getItemCount();
                            announcements.addAll(response.body());
                            mAdapter.notifyItemRangeInserted(currentSize, response.body().size());
                        }
                        nextPage = PaginationUtils.getNextPageUrl(response.headers());
                        loaded = true;
                        mainList.setVisibility(View.VISIBLE);

                    } else {
                        showSnackbar(course_id);
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Announcement>> call, Throwable t) {
                if (isAdded()) {
                    progressBar.progressiveStop();
                    showSnackbar(course_id);
                }
            }
        });
    }

    private void showSnackbar(final int course_id) {
        if (isAdded()) {
            mCallback.showSnackbar(getString(R.string.error_requesting_assignments), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestAnnouncements(course_id);
                }
            });
        }
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

                Bundle bundle = new Bundle();
                String json = new Gson().toJson(announcements.get(position));
                bundle.putString("announcement", json);
                singleAnnouncementFragment.setArguments(bundle);
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
