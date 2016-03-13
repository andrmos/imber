package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.SingleAnnouncementViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;

import java.util.ArrayList;

/**
 * Created by PatrickFinseth on 13.03.16.
 */
public class AnnouncementRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private static final String TAG = "AnnouncementRecyclerViewAdapter: ";

    private ArrayList<String> data;

    public AnnouncementRecyclerViewAdapter (ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // agenda card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_card, parent, false);
        GeneralViewHolder holder = new SingleAnnouncementViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        SingleAnnouncementViewHolder announcementHolder = (SingleAnnouncementViewHolder) holder;
        announcementHolder.title.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
