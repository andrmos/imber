package com.mossige.finseth.follo.inf219_mitt_uib.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mossige.finseth.follo.inf219_mitt_uib.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.Map;

import hirondelle.date4j.DateTime;

/**
 * Created by Andre on 27/04/2016.
 */
public class CustomCaldroidAdapter extends CaldroidGridAdapter {

    private static final String TAG = "CaldroidAdapter";

    public CustomCaldroidAdapter(Context context, int month, int year, Map<String, Object> caldroidData, Map<String, Object> extraData) {
        super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.calendar_cell, null);
        }

        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();

        // Get date for this cell
        DateTime dateTime = this.datetimeList.get(position);

        TextView tvDayNumber = (TextView) cellView.findViewById(R.id.dayNumber);
        tvDayNumber.setTextColor(Color.BLACK);

        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tvDayNumber.setTextColor(ContextCompat.getColor(context, R.color.caldroid_darker_gray));
        }

        // Show circle on dates that contains an event
        String key = dateTime.getYear() + "-" + dateTime.getMonth() + "-" + dateTime.getDay();
        if (extraData.containsKey(key)) {
            boolean hasEvent = (boolean) extraData.get(key);
            if (hasEvent) {
                ImageView hasEventCircle = (ImageView) cellView.findViewById(R.id.hasEventCircle);
                hasEventCircle.setVisibility(View.VISIBLE);
            }
        }else{
            ImageView hasEventCircle = (ImageView) cellView.findViewById(R.id.hasEventCircle);
            hasEventCircle.setVisibility(View.INVISIBLE);
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tvDayNumber.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
            cellView.setBackgroundColor(ContextCompat.getColor(context, R.color.caldroid_sky_blue));
            tvDayNumber.setTextColor(Color.BLACK);

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
            } else {
                cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
            }
        }

        tvDayNumber.setText("" + dateTime.getDay());

        // From Caldroid: Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

        // Set custom color if required
        setCustomResources(dateTime, cellView, tvDayNumber);

        return cellView;
    }
}
