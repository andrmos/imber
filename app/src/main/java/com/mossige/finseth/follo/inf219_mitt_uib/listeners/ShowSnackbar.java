package com.mossige.finseth.follo.inf219_mitt_uib.listeners;

import android.view.View;

/**
 * Created by patrickfinseth on 03.05.2016.
 */
public class ShowSnackbar{

    public interface ShowToastListener {
        void showSnackbar(String toastMessage, View.OnClickListener listener);
    }

}
