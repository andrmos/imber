package com.mossige.finseth.follo.inf219_mitt_uib;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private RecyclerView mainList;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        initRecycleView(rootView);

        return rootView;
    }


    private void initRecycleView(View rootView) {
        // Create RecycleView
        // findViewById() belongs to Activity, so need to access it from the root view of the fragment
        mainList = (RecyclerView) rootView.findViewById(R.id.mainList);

        // Create the LayoutManager that holds all the views
        mLayoutManager = new LinearLayoutManager(getActivity());
        mainList.setLayoutManager(mLayoutManager);

        ArrayList<String> testData = initData(10);

        // Create adapter that binds the views with some content
        mAdapter = new MainListAdapter(testData);
        mainList.setAdapter(mAdapter);
    }

    public ArrayList<String> initData(int amount) {
        String title = "Tittel";
        ArrayList<String> testData = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            testData.add(title + i);
        }

        return testData;
    }

}
