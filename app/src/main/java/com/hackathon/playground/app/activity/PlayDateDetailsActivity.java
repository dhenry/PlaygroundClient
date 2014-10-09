package com.hackathon.playground.app.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hackathon.playground.app.R;
import com.hackathon.playground.app.model.PlayDate;
import com.hackathon.playground.app.orm.PlaygroundResolver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: Dave
 */
public class PlayDateDetailsActivity extends FragmentActivity {

    public final static String LOG_TAG = PlayDateDetailsActivity.class.getCanonicalName();
    public static final String SELECTED_PLAY_DATE = "SELECTED_PLAY_DATE";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yy");

    private PlaygroundResolver resolver;

    private PlayDate playDate;
    private TextView location;
    private ImageButton displayPointOfInterestButton;
    private TextView description;
    private TextView organizer;
    private TextView startsAt;
    private TextView endsAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_date_details);
        location = (TextView)findViewById(R.id.location);
        displayPointOfInterestButton = (ImageButton)findViewById(R.id.displayPointOfInterest);
        description = (TextView)findViewById(R.id.description);
        organizer = (TextView)findViewById(R.id.organizer);
        startsAt = (TextView)findViewById(R.id.startsAt);
        endsAt = (TextView)findViewById(R.id.endsAt);

        playDate = getIntent().getParcelableExtra(SELECTED_PLAY_DATE);

        if (playDate != null) {
            location.setText(playDate.getLocation());
            description.setText(playDate.getDescription());
            organizer.setText(playDate.getOrganiser());
            startsAt.setText(dateFormat.format(new Date(playDate.getStartTime())));
            endsAt.setText(dateFormat.format(new Date(playDate.getEndTime())));
        }

        // load the amenities fragment!
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {

            Bundle playDateBundle = new Bundle();
            playDateBundle.putParcelable(SELECTED_PLAY_DATE, playDate);

            AttendeesFragment attendeesFragment = new AttendeesFragment();
            attendeesFragment.setArguments(playDateBundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.add(R.id.attendees_fragment, attendeesFragment);

            transaction.commit();
        }

        resolver = new PlaygroundResolver(this);

        displayPointOfInterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent pointOfInterestDetailsIntent =
                            new Intent(PlayDateDetailsActivity.this, PointOfInterestDetailsActivity.class);
                    pointOfInterestDetailsIntent.putExtra(PointOfInterestDetailsActivity.POINT_OF_INTEREST,
                            resolver.getPointOfInterestByName(playDate.getLocation()));
                    startActivity(pointOfInterestDetailsIntent);

                } catch (RemoteException e) {
                    Log.e(LOG_TAG, e.getMessage());
                    Toast.makeText(PlayDateDetailsActivity.this,
                                   "Couldn't find location details for " + playDate.getLocation(),
                                   Toast.LENGTH_LONG).show();
                }
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
                Intent mapIntent = new Intent(PlayDateDetailsActivity.this, MapsActivity.class);
                startActivity(mapIntent);
                return true;
            case R.id.action_playdate:
                Intent createPlayDateIntent = new Intent(PlayDateDetailsActivity.this, PlayDateCreateActivity.class);
                startActivity(createPlayDateIntent);
                return true;
            case R.id.action_playdates:
                Intent playDateDiscoveryIntent = new Intent(PlayDateDetailsActivity.this, PlayDateDiscoveryActivity.class);
                startActivity(playDateDiscoveryIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
