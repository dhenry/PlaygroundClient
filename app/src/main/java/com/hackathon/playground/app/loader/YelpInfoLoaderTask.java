package com.hackathon.playground.app.loader;

import android.os.AsyncTask;
import android.os.RemoteException;

import com.hackathon.playground.app.activity.PointOfInterestDetailsActivity;
import com.hackathon.playground.app.loader.connector.YelpConnector;
import com.hackathon.playground.app.model.PointOfInterest;
import com.hackathon.playground.app.model.YelpPlaceInfo;

import java.lang.ref.WeakReference;

/**
 * Author: Dave
 */
public class YelpInfoLoaderTask extends AsyncTask<Long, Void, YelpPlaceInfo> {

    private final WeakReference<PointOfInterestDetailsActivity> mParent;
    private final PointOfInterest pointOfInterest;

    public YelpInfoLoaderTask(PointOfInterestDetailsActivity parent, PointOfInterest pointOfInterest) {
        super();
        mParent = new WeakReference<>(parent);
        this.pointOfInterest = pointOfInterest;
    }

    @Override
    protected YelpPlaceInfo doInBackground(Long... params) {
        YelpConnector yelpConnector = new YelpConnector();
        return yelpConnector.getYelpInfo(pointOfInterest);
    }

    @Override
    protected void onPostExecute(YelpPlaceInfo yelpPlaceInfo) {
        if (mParent.get() != null && yelpPlaceInfo != null) {
            mParent.get().setYelpInfo(yelpPlaceInfo);
        }
    }
}
