package com.mossige.finseth.follo.inf219_mitt_uib.network.retrofit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Custom Callback implementing the retrofit2.Callback interface.
 * This Callback can be tagged canceled based on a tag.
 * The Callbacks onSuccess or onError methods will not be executed if the Callback is marked canceled.
 *
 * Created by andre on 06.01.17.
 */
public abstract class CancelableCallback<T> implements Callback<T> {

    private static List<CancelableCallback> callbacks = new ArrayList<>();

    private boolean isCanceled = false;
    private Object tag = null;

    public static void cancelAll() {
        Iterator<CancelableCallback> iterator = callbacks.iterator();
        while (iterator.hasNext()){
            iterator.next().isCanceled = true;
            iterator.remove();
        }
    }

    public static void cancel(Object tag) {
        if (tag != null) {
            Iterator<CancelableCallback> iterator = callbacks.iterator();
            CancelableCallback item;
            while (iterator.hasNext()) {
                item = iterator.next();
                if (tag.equals(item.tag)) {
                    item.isCanceled = true;
                    iterator.remove();
                }
            }
        }
    }

    public CancelableCallback() {
        callbacks.add(this);
    }

    public CancelableCallback(Object tag) {
        this.tag = tag;
        callbacks.add(this);
    }

    public void cancel() {
        isCanceled = true;
        callbacks.remove(this);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (!isCanceled) {
            onSuccess(call, response);
        }
        callbacks.remove(this);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (!isCanceled) {
            onError(call, t);
        }
        callbacks.remove(this);
    }

    public abstract void onSuccess(Call<T> call, Response<T> response);
    public abstract void onError(Call<T> call, Throwable t);
}
