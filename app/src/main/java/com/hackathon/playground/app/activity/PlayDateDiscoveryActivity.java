package com.hackathon.playground.app.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hackathon.playground.app.R;

/**
 * Author: Dave
 */
public class PlayDateDiscoveryActivity extends FragmentActivity {

    public static final String SEARCH_QUERY = "SEARCH_QUERY";

    private EditText playDateSearchField;
    private ImageButton playDateFilterBtn;
    private PlayDatesListFragment listFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_date_discovery);
        this.playDateSearchField = (EditText)findViewById(R.id.playdate_search_field);
        this.playDateFilterBtn = (ImageButton)findViewById(R.id.filter_play_dates_button);

        // load the fragments!
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            listFragment = new PlayDatesListFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.add(R.id.play_date_list_fragment, listFragment);

            transaction.commit();
        }

        String searchQuery = getIntent().getStringExtra(SEARCH_QUERY);

        if (searchQuery != null) {
            this.playDateSearchField.setText(searchQuery);
            listFragment.setFilterValue(playDateSearchField.getText());
        }

        this.playDateFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listFragment.setFilterValue(playDateSearchField.getText());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_map:
                Intent mapIntent = new Intent(PlayDateDiscoveryActivity.this, MapsActivity.class);
                startActivity(mapIntent);
                return true;
            case R.id.action_playdate:
                Intent createPlayDateIntent = new Intent(PlayDateDiscoveryActivity.this, PlayDateCreateActivity.class);
                startActivity(createPlayDateIntent);
                return true;
            case R.id.action_playdates:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
