package com.hackathon.playground.app.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hackathon.playground.app.MapWrapperLayout;
import com.hackathon.playground.app.OnInfoWindowElemTouchListener;
import com.hackathon.playground.app.R;
import com.hackathon.playground.app.loader.ScraperWikiLoaderTask;
import com.hackathon.playground.app.model.Amenity;
import com.hackathon.playground.app.model.PointOfInterest;
import com.hackathon.playground.app.orm.PlaygroundResolver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity {

    public final static String LOG_TAG = MapsActivity.class.getCanonicalName();
    private GoogleMap map;

    private PlaygroundResolver resolver;

    private ViewGroup infoWindow;
    private TextView infoTitle;
    private ImageButton infoButton;
    private ImageButton playDateButton;
    private OnInfoWindowElemTouchListener infoButtonListener;
    private OnInfoWindowElemTouchListener playDateButtonListener;
    private Map<String, PointOfInterest> markersForPointsOfInterest = new HashMap<>();

    private int requestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        requestCode = getIntent().getIntExtra(PlayDateCreateActivity.EMBEDDED_REQUEST_CODE, 0);

        if (requestCode == PlayDateCreateActivity.PICK_LOCATION_REQUEST) {
            Toast.makeText(this, "Tap a map marker to select a playdate location!", Toast.LENGTH_LONG).show();
        }

        final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        map = mapFragment.getMap();

        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(map, getPixelsFromDp(this, 39 + 20));

        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.playDateButton = (ImageButton)infoWindow.findViewById(R.id.play_date_btn);
        this.infoButton = (ImageButton)infoWindow.findViewById(R.id.details_button);

        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        this.infoButtonListener = new OnInfoWindowElemTouchListener(infoButton)
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Go to the details activity
                Intent pointOfInterestIntent = new Intent(MapsActivity.this, PointOfInterestDetailsActivity.class);
                pointOfInterestIntent.putExtra(PointOfInterestDetailsActivity.POINT_OF_INTEREST,
                        markersForPointsOfInterest.get(marker.getId()));
                startActivity(pointOfInterestIntent);
            }
        };
        this.infoButton.setOnTouchListener(infoButtonListener);

        this.playDateButtonListener = new OnInfoWindowElemTouchListener(playDateButton)
        {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Go to the play date discovery activity
                Intent playDatesIntent = new Intent(MapsActivity.this, PlayDateDiscoveryActivity.class);
                playDatesIntent.putExtra(PlayDateDiscoveryActivity.SEARCH_QUERY,
                        markersForPointsOfInterest.get(marker.getId()).getName());
                startActivity(playDatesIntent);
            }
        };

        this.playDateButton.setOnTouchListener(playDateButtonListener);


        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                if (requestCode == PlayDateCreateActivity.PICK_LOCATION_REQUEST) {
                    // return to the create play date activity
                    Intent intent = new Intent();
                    intent.putExtra(PointOfInterestDetailsActivity.POINT_OF_INTEREST, markersForPointsOfInterest.get(marker.getId()));
                    setResult(RESULT_OK, intent);
                    finish();
                    return null;
                }


                // Setting up the infoWindow with current's marker info
                infoTitle.setText(marker.getTitle());
                infoButtonListener.setMarker(marker);
                playDateButtonListener.setMarker(marker);

                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                return infoWindow;
            }
        });

        setUpMap();

        resolver = new PlaygroundResolver(this);

        populateMapData();
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
                return true;
            case R.id.action_playdate:
                Intent createPlayDateIntent = new Intent(MapsActivity.this, PlayDateCreateActivity.class);
                startActivity(createPlayDateIntent);
                return true;
            case R.id.action_playdates:
                Intent playDateDiscoveryIntent = new Intent(MapsActivity.this, PlayDateDiscoveryActivity.class);
                startActivity(playDateDiscoveryIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * If the resolve doesn't have the data we crave then use the WikiScraperLoader to retrieve it.
     * If the data exists, load the map markers
     */
    private void populateMapData() {
        boolean yoDataIsStale = true;
        try {
            if (resolver.getAllPointsOfInterest().size() > 0) {
                yoDataIsStale = false;
                loadMapMarkers();
            }
        } catch(RemoteException e) {
            yoDataIsStale = true;
            Log.e(LOG_TAG, e.getMessage());
        }

        if (yoDataIsStale) {
            ScraperWikiLoaderTask scraperWikiLoaderTask = new ScraperWikiLoaderTask(MapsActivity.this);
            scraperWikiLoaderTask.execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap() {
        map.setMyLocationEnabled(true);

        // center the location of the map over the devices location
        String locationProvider = LocationManager.NETWORK_PROVIDER;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), 12));
    }

    private void addMarker(PointOfInterest pointOfInterest) {

        Marker mapMarker = map.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.house_flag))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(pointOfInterest.getLatitude(), pointOfInterest.getLongitude()))
                .title(pointOfInterest.getName()));

        markersForPointsOfInterest.put(mapMarker.getId(), pointOfInterest);
    }

    public void loadMapMarkers() {
        try {
            // add map markers for each point of interest
            for(PointOfInterest pointOfInterest : resolver.getAllPointsOfInterest()) {

                addMarker(pointOfInterest);
            }

            Toast.makeText(this,"Loaded map markers!", Toast.LENGTH_LONG).show();
        } catch (RemoteException e) {
            Log.e(LOG_TAG, e.getMessage());
            Toast.makeText(this,"Failed to load map markers", Toast.LENGTH_LONG).show();
        }
    }

    public void storeScrapedData(Pair<List<PointOfInterest>, List<Amenity>> scrapedData) {
        resolver.insertPointsOfInterest(scrapedData.first);
        resolver.insertAmenities(scrapedData.second);
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

}
