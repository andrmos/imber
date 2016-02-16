package com.mossige.finseth.follo.inf219_mitt_uib;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by André on 12.02.2016.
 */
public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    private ArrayList<String> data;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.title);
        }
    }

    public MainListAdapter(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public MainListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);

        // TODO set padding, size, margins etc
//        v.setPadding(10, 5, 5, 10);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(MainListAdapter.ViewHolder holder, int position) {
        // set text of text view in card
        holder.mTextView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}