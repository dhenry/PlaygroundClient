package com.hackathon.playground.app.activity;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hackathon.playground.app.R;
import com.hackathon.playground.app.adapter.PlayDateAdapter;
import com.hackathon.playground.app.loader.PlaygroundServerLoaderTask;
import com.hackathon.playground.app.model.PlayDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Dave
 */
public class PlayDatesListFragment extends ListFragment {

    private PlayDateAdapter adapter;
    private List<PlayDate> playDates;
    private String filterValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_dates_list, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new PlaygroundServerLoaderTask(this).execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        adapter = new PlayDateAdapter(activity, R.layout.row_play_date_summary);
        setListAdapter(adapter);
    }

    public void setPlayDates(List<PlayDate> playDates) {
        this.playDates = playDates;

        if (filterValue != null && !filterValue.equals("")) {
            List<PlayDate> filteredResults = new ArrayList<>();

            for(PlayDate playDate : playDates) {
                if (playDate.getLocation().equals(filterValue)) {
                    filteredResults.add(playDate);
                }
            }
            adapter.clear();
            adapter.addAll(filteredResults);
        } else {
            adapter.clear();
            adapter.addAll(playDates);
        }
    }

    public void setFilterValue(Editable locationName) {
        this.filterValue = locationName.toString();

        if (playDates == null) {
            new PlaygroundServerLoaderTask(this).execute();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        PlayDate selectedPlayDate = adapter.getItem(position);

        Intent playDateDetailsIntent = new Intent(getActivity(), PlayDateDetailsActivity.class);
        playDateDetailsIntent.putExtra(PlayDateDetailsActivity.SELECTED_PLAY_DATE, selectedPlayDate);
        startActivity(playDateDetailsIntent);
    }
}
