package com.mossige.finseth.follo.inf219_mitt_uib.fragments.sending_message;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;

/**
 * Created by folb 31.03.16
 */
public class ComposeMessageFragment extends Fragment {

    public ComposeMessageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose_message, container, false);
    }

}
