package com.mossige.finseth.follo.inf219_mitt_uib.listeners;

import android.view.View;

/**
 * Created by patrickfinseth on 03.05.2016.
 */
public interface MainActivityListener {
        void showSnackbar(String toastMessage, View.OnClickListener listener);
        void initCalendar();
        void requestUnreadCount();
}
