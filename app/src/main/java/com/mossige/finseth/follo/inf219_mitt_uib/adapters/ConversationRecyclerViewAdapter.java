package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.ConversationViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.CourseViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.card_view_holders.GeneralViewHolder;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Conversation;
import com.mossige.finseth.follo.inf219_mitt_uib.models.Course;

import java.util.ArrayList;

/**
 * Created by Follo on 15.03.2016.
 */
public class ConversationRecyclerViewAdapter extends RecyclerView.Adapter<GeneralViewHolder> {

    private ArrayList<Conversation> data;

    public ConversationRecyclerViewAdapter(ArrayList<Conversation> data) {
        this.data = data;
    }

    @Override
    public GeneralViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // course card
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_card, parent, false);
        GeneralViewHolder holder = new ConversationViewHolder(v);

        return holder;
    }


    @Override
    public void onBindViewHolder(GeneralViewHolder holder, int position) {
        // set text of text view in card
        //TODO
        ConversationViewHolder conversationHolder = (ConversationViewHolder) holder;
        conversationHolder.conversation_subject.setText(data.get(position).getSubject());

//        CourseViewHolder courseHolder = (CourseViewHolder) holder;
//        courseHolder.course_code.setText(data.get(position).getCourseCode());
//        courseHolder.course_title.setText(data.get(position).getName());
//        courseHolder.course_id.setText("" + data.get(position).getId());
//        courseHolder.calendar_url.setText(data.get(position).getCalenderUrl());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}