package com.hackathon.playground.app.loader;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.hackathon.playground.app.activity.MapsActivity;
import com.hackathon.playground.app.loader.connector.GeolocationConnector;
import com.hackathon.playground.app.loader.connector.ScraperWikiConnector;
import com.hackathon.playground.app.model.Amenity;
import com.hackathon.playground.app.model.PointOfInterest;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Dave
 */
public class ScraperWikiLoaderTask extends AsyncTask<Void, Void, Pair<List<PointOfInterest>, List<Amenity>>> {

    public final static String LOG_TAG = ScraperWikiLoaderTask.class.getCanonicalName();

    private static final String POINT_OF_INTEREST_QUERY =
            "/081zyeo/j5fx7plmqkdvieo/sql/?q=select%20name%2Clocation%2Camenity_group%0Afrom%20parks";

    private static final String AMENITY_QUERY =
            "/081zyeo/j5fx7plmqkdvieo/sql/?q=select%20id%2Camenity_group%2Camenity%0Afrom%20amenities";

    private final WeakReference<MapsActivity> mParent;

    public ScraperWikiLoaderTask(MapsActivity parent) {
        super();
        mParent = new WeakReference<>(parent);
    }

    @Override
    protected Pair<List<PointOfInterest>, List<Amenity>> doInBackground(Void... params) {

        // retrieve the PointsOfInterest
        ScraperWikiConnector<PointOfInterest> pointOfInterestConnector = new ScraperWikiConnector<>(new TypeToken<ArrayList<PointOfInterest>>(){}.getType());
        List<PointOfInterest> pointsOfInterest = pointOfInterestConnector.getJsonResultList(POINT_OF_INTEREST_QUERY);

        // retrieve the Amenities
        ScraperWikiConnector<Amenity> amenityConnector = new ScraperWikiConnector<>(new TypeToken<ArrayList<Amenity>>(){}.getType());
        List<Amenity> amenities = amenityConnector.getJsonResultList(AMENITY_QUERY);

        List<PointOfInterest> validPointsOfInterest = new ArrayList<>();
        GeolocationConnector geolocationConnector = new GeolocationConnector();

        // retrieve the geo locations for the points of interest
        for(PointOfInterest pointOfInterest : pointsOfInterest) {

            LatLng latLng = geolocationConnector.getResult(pointOfInterest);

            SystemClock.sleep(1000);

            // only save the points of interest that have a valid LatLng
            if (latLng != null) {
                pointOfInterest.setLatitude(latLng.latitude);
                pointOfInterest.setLongitude(latLng.longitude);
                validPointsOfInterest.add(pointOfInterest);
            }
            break;
        }

        return new Pair<>(validPointsOfInterest, amenities);
    }

    @Override
    protected void onPostExecute(Pair<List<PointOfInterest>, List<Amenity>> result) {

        if (null != mParent.get()) {

            if (result != null) {
                mParent.get().storeScrapedData(result);
            }
            mParent.get().loadMapMarkers();
        }
    }
}
