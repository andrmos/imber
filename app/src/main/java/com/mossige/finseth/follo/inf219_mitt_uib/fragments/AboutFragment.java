package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;
import com.mossige.finseth.follo.inf219_mitt_uib.network.RequestQueueHandler;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private static final String TAG = "AboutFragment";

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "created about view");

        getActivity().setTitle(R.string.about_title);

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        return view;
    }





}
