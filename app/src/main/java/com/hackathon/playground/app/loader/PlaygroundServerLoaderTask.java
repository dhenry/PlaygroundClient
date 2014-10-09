package com.hackathon.playground.app.loader;

import android.os.AsyncTask;

import com.hackathon.playground.app.activity.PlayDatesListFragment;
import com.hackathon.playground.app.loader.connector.PlaygroundServerConnector;
import com.hackathon.playground.app.model.PlayDate;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Author: Dave
 */
public class PlaygroundServerLoaderTask extends AsyncTask<Void, Void, List<PlayDate>> {

    private final WeakReference<PlayDatesListFragment> mParent;

    public PlaygroundServerLoaderTask(PlayDatesListFragment fragment) {
        mParent = new WeakReference<>(fragment);
    }

    protected List<PlayDate> doInBackground(Void... args) {
        // retrieve the Amenities
        PlaygroundServerConnector<PlayDate> playgroundServerConnector = new PlaygroundServerConnector<>();
        List<PlayDate> playDates = playgroundServerConnector.getJsonResultList("playdates");
        return playDates;
    }

    protected void onPostExecute(List<PlayDate> result) {
        if (mParent.get() != null) {
            mParent.get().setPlayDates(result);
        }
    }
}
