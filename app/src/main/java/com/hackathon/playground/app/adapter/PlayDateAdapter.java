package com.hackathon.playground.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hackathon.playground.app.R;
import com.hackathon.playground.app.model.PlayDate;

import java.util.List;

/**
 * Author: Dave
 */
public class PlayDateAdapter extends ArrayAdapter<PlayDate> {

    public PlayDateAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public PlayDateAdapter(Context context, int resource, List<PlayDate> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_play_date_summary, null);

        }

        PlayDate playDate = getItem(position);

        if (playDate != null) {

            TextView location = (TextView) v.findViewById(R.id.location);
            TextView description = (TextView) v.findViewById(R.id.description);
            TextView organizer = (TextView) v.findViewById(R.id.organizer);

            if (location != null) {
                location.setText(playDate.getLocation());
            }
            if (description != null) {

                description.setText(playDate.getDescription());
            }
            if (organizer != null) {

                organizer.setText("@" +playDate.getOrganiser());
            }
        }

        return v;

    }
}
