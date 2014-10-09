package com.hackathon.playground.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.hackathon.playground.app.R;
import com.hackathon.playground.app.loader.PlayDateCreatorTask;
import com.hackathon.playground.app.model.PlayDate;
import com.hackathon.playground.app.model.PointOfInterest;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Author: Dave
 */
public class PlayDateCreateActivity extends FragmentActivity {

    static final int PICK_LOCATION_REQUEST = 777;
    static final String EMBEDDED_REQUEST_CODE = "EMBEDDED_REQUEST_CODE";

    private static final DateTimeZone tz = DateTimeZone.forTimeZone(TimeZone.getDefault());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd-MM-yy");

    private TextView location;
    private ImageButton selectLocationBtn;
    private EditText description;
    private EditText organizer;
    private TextView startsAt;
    private ImageButton setStartsAtBtn;
    private TextView endsAt;
    private ImageButton setEndsAtBtn;
    private Button saveBtn;

    private DateTime startsAtDateTime;
    private DateTime endsAtDateTime;

    private PlayDate playDate;
    private PointOfInterest pointOfInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_play_date);

        location = (TextView)findViewById(R.id.location);
        selectLocationBtn = (ImageButton)findViewById(R.id.selectPointOfInterest);
        description = (EditText)findViewById(R.id.descriptionInput);
        organizer = (EditText)findViewById(R.id.organizer_input);
        startsAt = (TextView)findViewById(R.id.startsAt);
        setStartsAtBtn = (ImageButton)findViewById(R.id.setStartsAt);
        endsAt = (TextView)findViewById(R.id.endsAt);
        setEndsAtBtn = (ImageButton)findViewById(R.id.setEndsAt);
        saveBtn = (Button)findViewById(R.id.save);

        selectLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(PlayDateCreateActivity.this, MapsActivity.class);
                mapIntent.putExtra(EMBEDDED_REQUEST_CODE, PICK_LOCATION_REQUEST);
                startActivity(mapIntent);
                startActivityForResult(mapIntent, PICK_LOCATION_REQUEST);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (buildValidPlayDate()) {
                    new PlayDateCreatorTask(PlayDateCreateActivity.this).execute(playDate);
                } else {
                    Toast.makeText(PlayDateCreateActivity.this,
                            "Please enter values for all inputs.", Toast.LENGTH_LONG).show();
                }
            }
        });

        setStartsAtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date =DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, final int year, final int month, final int day) {
                        TimePickerDialog time = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
                                startsAtDateTime = new DateTime(year, month + 1, day, hour, minute, 0, 0, tz);
                                startsAt.setText(dateFormat.format(startsAtDateTime.toDate()));
                            }
                        }, 0, 0, false);
                        time.show(getFragmentManager(), "Starts at Time");
                    }
                }, 2014, 0, 0, false);
                date.show(getFragmentManager(), "Starts at Date");
            }
        });

        setEndsAtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog date =DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog datePickerDialog, final int year, final int month, final int day) {
                        TimePickerDialog time = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
                                endsAtDateTime = new DateTime(year, month + 1, day, hour, minute, 0, 0, tz);
                                endsAt.setText(dateFormat.format(endsAtDateTime.toDate()));
                            }
                        }, 0, 0, false);
                        time.show(getFragmentManager(), "Ends at Time");
                    }
                }, 2014, 0, 0, false);
                date.show(getFragmentManager(), "Ends at Date");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_LOCATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                pointOfInterest = data.getParcelableExtra(PointOfInterestDetailsActivity.POINT_OF_INTEREST);
                if (pointOfInterest != null) {
                    location.setText(pointOfInterest.getName());
                }
            }
        }
    }

    private boolean buildValidPlayDate() {
        playDate = new PlayDate();
        try {
            if (startsAtDateTime == null
                    || endsAtDateTime == null
                    || pointOfInterest == null
                    || organizer.getText().length() == 0) {
                return false;
            } else {
                playDate.setStartTime(startsAtDateTime.getMillis());
                playDate.setEndTime(endsAtDateTime.getMillis());
                playDate.setOrganiser(organizer.getText().toString());
                playDate.setLocation(pointOfInterest.getLocation());
                playDate.setLatitude(pointOfInterest.getLatitude());
                playDate.setLongitude(pointOfInterest.getLongitude());
                playDate.setDescription(description.getText().toString());
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
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
                Intent mapIntent = new Intent(PlayDateCreateActivity.this, MapsActivity.class);
                startActivity(mapIntent);
                return true;
            case R.id.action_playdate:
                return true;
            case R.id.action_playdates:
                Intent playDateDiscoveryIntent = new Intent(PlayDateCreateActivity.this, PlayDateDiscoveryActivity.class);
                startActivity(playDateDiscoveryIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onPlayDateCreated(PlayDate result) {
        Toast.makeText(this, "Playdate created!", Toast.LENGTH_LONG).show();
        Intent playDateDetailsIntent = new Intent(PlayDateCreateActivity.this, PlayDateDetailsActivity.class);
        playDateDetailsIntent.putExtra(PlayDateDetailsActivity.SELECTED_PLAY_DATE, result);
        startActivity(playDateDetailsIntent);
    }

    public void onPlayDateCreationFailed() {
        Toast.makeText(this, "Playdate creation failed :(", Toast.LENGTH_LONG).show();
    }
}
