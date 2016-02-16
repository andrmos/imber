package com.mossige.finseth.follo.inf219_mitt_uib.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Follo on 16.02.2016.
 */
public class RequestQueueHandler {

        private static RequestQueueHandler instance;
        private RequestQueue requestQueue;
        private static Context context;

        private RequestQueueHandler(Context context) {
            RequestQueueHandler.context = context;
            requestQueue = getRequestQueue();
        }


        public static synchronized RequestQueueHandler getInstance(Context context) {
            if (instance == null) {
                instance = new RequestQueueHandler(context);
            }
            return instance;
        }

        public RequestQueue getRequestQueue() {
            if (requestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            }
            return requestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            getRequestQueue().add(req);
        }
}
