package com.mossige.finseth.follo.inf219_mitt_uib.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.activities.LicenseActivity;


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
        getActivity().setTitle(R.string.about_title);
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.about_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.licenses) {
            showLicenses();
            return true;
        }

        return false;
    }

    /**
     * Show the licenses in the device web browser.
     */
    private void showLicenses() {
        Intent intent = new Intent(getContext(), LicenseActivity.class);
        startActivity(intent);
    }
}
