package com.hackathon.playground.app.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hackathon.playground.app.R;
import com.hackathon.playground.app.loader.ImageDownloaderTask;
import com.hackathon.playground.app.loader.YelpInfoLoaderTask;
import com.hackathon.playground.app.model.PointOfInterest;
import com.hackathon.playground.app.model.YelpPlaceInfo;
import com.hackathon.playground.app.orm.PlaygroundResolver;
import com.hackathon.playground.app.view.DropShadowImageView;

/**
 * Author: Dave
 */
public class PointOfInterestDetailsActivity extends FragmentActivity {

    public static final String POINT_OF_INTEREST = "POINT_OF_INTEREST";

    private TextView yelpRating;
    private TextView isOpen;
    private TextView nameLabel;
    private ImageButton searchForPlayDatesBtn;
    private DropShadowImageView thumbnail;
    private Bitmap thumbnailBitmap;
    private PointOfInterest pointOfInterest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_interest);
        this.yelpRating = (TextView)findViewById(R.id.yelpRating);
        this.isOpen = (TextView)findViewById(R.id.isOpen);
        this.thumbnail = (DropShadowImageView)findViewById(R.id.thumbnail);
        this.searchForPlayDatesBtn = (ImageButton)findViewById(R.id.show_playdates_btn);
        this.nameLabel = (TextView)findViewById(R.id.placeOfInterestName);
        tryRenderThumbnail();

        pointOfInterest = getIntent().getParcelableExtra(PointOfInterestDetailsActivity.POINT_OF_INTEREST);

        if (pointOfInterest != null ) {
            this.nameLabel.setText(pointOfInterest.getName());
            // load the yelp info!
            new YelpInfoLoaderTask(this, pointOfInterest).execute(pointOfInterest.getId());
        }

        // load the amenities fragment!
        if (getFragmentManager().findFragmentById(android.R.id.content) == null) {
            AmenitiesListFragment amenitiesListFragment = new AmenitiesListFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.add(R.id.amenities_fragment, amenitiesListFragment);

            transaction.commit();
        }

        this.searchForPlayDatesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playDateDiscoveryIntent = new Intent(PointOfInterestDetailsActivity.this, PlayDateDiscoveryActivity.class);
                playDateDiscoveryIntent.putExtra(PlayDateDiscoveryActivity.SEARCH_QUERY, pointOfInterest.getName());
                startActivity(playDateDiscoveryIntent);
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
                Intent mapIntent = new Intent(PointOfInterestDetailsActivity.this, MapsActivity.class);
                startActivity(mapIntent);
                return true;
            case R.id.action_playdate:
                Intent createPlayDateIntent = new Intent(PointOfInterestDetailsActivity.this, PlayDateCreateActivity.class);
                startActivity(createPlayDateIntent);
                return true;
            case R.id.action_playdates:
                Intent playDateDiscoveryIntent = new Intent(PointOfInterestDetailsActivity.this, PlayDateDiscoveryActivity.class);
                startActivity(playDateDiscoveryIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setYelpInfo(YelpPlaceInfo yelpInfo) {
        yelpRating.setText(yelpInfo.getRating().toString());

        if (yelpInfo.isClosed()) {
            isOpen.setText("Currently Closed");
            isOpen.setTextColor(Color.RED);
        } else {
            isOpen.setText("Currently Open");
            isOpen.setTextColor(Color.GREEN);
        }

        // download the thumbnail! ( if required )
        if (this.thumbnailBitmap == null) {
            new ImageDownloaderTask(this).execute(yelpInfo.getImageUrl());
        }
    }

    public void setThumbnailBitmap(Bitmap result) {
        this.thumbnailBitmap = Bitmap.createScaledBitmap(result, 350, 350, false);
        tryRenderThumbnail();
    }

    private void tryRenderThumbnail() {
        if (this.thumbnailBitmap != null) {
            this.thumbnail.setImageBitmap(thumbnailBitmap);
        }
    }

}
