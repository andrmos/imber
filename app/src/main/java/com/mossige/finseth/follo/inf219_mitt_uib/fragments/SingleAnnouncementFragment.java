package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Announcement;

/**
 * Created by PatrickFinseth on 13.03.16.
 */
public class SingleAnnouncementFragment extends Fragment{


    private static final String TAG = "SingleAnnFragment";

    private Announcement announcement;

    public SingleAnnouncementFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.i(TAG, "created view");

        View view = inflater.inflate(R.layout.fragment_announcement, container, false);

        // TODO get arguments for announcement
        // TODO set arguments to layout file


        return view;
    }



}
