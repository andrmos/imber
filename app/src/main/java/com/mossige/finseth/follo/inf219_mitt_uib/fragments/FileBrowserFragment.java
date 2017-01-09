package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.adapters.FileBrowserRecyclerViewAdapter;
import com.mossige.finseth.follo.inf219_mitt_uib.listeners.ItemClickSupport;

/**
 * Created by andre on 09.01.2017.
 */
public class FileBrowserFragment extends Fragment {

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;

    public FileBrowserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_file_browser, container, false);
        getActivity().setTitle(R.string.btn_file_browser);

        initRecycleView(rootView);

        return rootView;
    }

    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mainList.setVisibility(View.GONE);

        // Create the LayoutManager that holds all the views
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        // Create adapter that binds the views with some content
        mAdapter = new FileBrowserRecyclerViewAdapter();
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
