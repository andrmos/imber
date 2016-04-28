package com.mossige.finseth.follo.inf219_mitt_uib.fragments;

import com.mossige.finseth.follo.inf219_mitt_uib.adapters.CustomCaldroidAdapter;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class CustomCaldroidFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        return new CustomCaldroidAdapter(getActivity(), month, year, getCaldroidData(), extraData);
    }
}
