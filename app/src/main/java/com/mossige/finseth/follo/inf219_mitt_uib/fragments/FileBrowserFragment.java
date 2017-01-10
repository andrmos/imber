package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.FileBrowserRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.EndlessRecyclerViewScrollListener;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.MainActivityListener;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.models.File;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Folder;
import com.mossige.finseth.follo.inf219_mitt_uib.network.PaginationUtils;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.MittUibClient;
import com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by andre on 09.01.2017.
 */
public class FileBrowserFragment extends Fragment {

    private static final String TAG = "FileBrowserFragment";

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<File> files;
    private ArrayList<Folder> folders;

    private Course course;
    private MainActivityListener callback;

    private String nextPageFiles;
    private String nextPageFolders;

    public FileBrowserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callback = (MainActivityListener) context;
        } catch (ClassCastException e) {
            //Do nothing
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        files = new ArrayList<>();
        folders = new ArrayList<>();
        nextPageFiles = "";
        nextPageFolders = "";

        if (getArguments() != null) {
            if (getArguments().containsKey("course")) {
                String json = getArguments().getString("course");
                course = new Gson().fromJson(json, Course.class);
            }
        }

        getFolders();
        getFiles();
    }

    private void getFolders() {
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());

        Call<List<Folder>> call;
        boolean firstPage = nextPageFolders.isEmpty();
        if (firstPage) {
            call = client.getFolders(course.getId(), null);
        } else {
            call = client.getFoldersPaginate(nextPageFolders);
        }

        Log.i(TAG, "url: " + call.request().url());

        call.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {
                if (response.isSuccessful()) {
                    int currentSize = folders.size();
                    folders.addAll(response.body());
                    mAdapter.notifyItemRangeInserted(currentSize, response.body().size());

                    nextPageFolders = PaginationUtils.getNextPageUrl(response.headers());
                } else {
                    showSnackbar(R.string.error_getting_files);
                }
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {
                showSnackbar(R.string.error_getting_files);
            }
        });

    }

    private void getFiles() {
        MittUibClient client = ServiceGenerator.createService(MittUibClient.class, getContext());

        Call<List<File>> call;
        boolean firstPage = nextPageFiles.isEmpty();
        if (firstPage) {
            call = client.getFiles(course.getId(), null);
        } else {
            call = client.getFilesPaginate(nextPageFiles);
        }

        call.enqueue(new Callback<List<File>>() {
            @Override
            public void onResponse(Call<List<File>> call, Response<List<File>> response) {
                if (response.isSuccessful()) {
                    int currentSize = mAdapter.getItemCount();
                    files.addAll(response.body());
                    mAdapter.notifyItemRangeInserted(currentSize, response.body().size());

                    nextPageFiles = PaginationUtils.getNextPageUrl(response.headers());
                } else {
                    showSnackbar(R.string.error_getting_files);
                }
            }

            @Override
            public void onFailure(Call<List<File>> call, Throwable t) {
                showSnackbar(R.string.error_getting_files);
            }
        });
    }

    private void showSnackbar(int error_getting_files) {
        callback.showSnackbar(getString(error_getting_files), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFiles();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_file_browser, container, false);
        getActivity().setTitle(R.string.btn_file_browser);

        initRecycleView(rootView);
        mainList.setVisibility(View.VISIBLE);

        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mainList.setVisibility(View.GONE);

        // Create the LayoutManager that holds all the views
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(layoutManager);

        mainList.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!nextPageFolders.isEmpty()) {
                    getFolders();
                }
                if (!nextPageFiles.isEmpty()) {
                    getFiles();
                }
            }
        });

        // Create adapter that binds the views with some content
        mAdapter = new FileBrowserRecyclerViewAdapter(files, folders);
        mainList.setAdapter(mAdapter);

        initOnClickListener();
    }

    private void initOnClickListener() {
        ItemClickSupport.addTo(mainList).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });
    }
}
