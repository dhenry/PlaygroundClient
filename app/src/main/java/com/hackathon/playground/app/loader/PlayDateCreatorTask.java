package com.hackathon.playground.app.loader;

import android.os.AsyncTask;

import com.hackathon.playground.app.activity.PlayDateCreateActivity;
import com.hackathon.playground.app.loader.connector.Connector;
import com.hackathon.playground.app.loader.connector.PlaygroundServerConnector;
import com.hackathon.playground.app.model.PlayDate;

import java.lang.ref.WeakReference;

/**
 * Author: Dave
 */
public class PlayDateCreatorTask extends AsyncTask<PlayDate, Void, PlayDate> {

    private final WeakReference<PlayDateCreateActivity> mParent;

    public PlayDateCreatorTask(PlayDateCreateActivity fragment) {
        mParent = new WeakReference<>(fragment);
    }

    protected PlayDate doInBackground(PlayDate... args) {
        // retrieve the Amenities
        PlaygroundServerConnector<PlayDate> playgroundServerConnector = new PlaygroundServerConnector<>();
        playgroundServerConnector.setType(PlayDate.class);
        playgroundServerConnector.setPostData(args[0]);
        PlayDate newPlayDate = playgroundServerConnector.makeRequest("playdates", Connector.REQUEST_TYPE.POST);

        return newPlayDate;
    }

    protected void onPostExecute(PlayDate result) {
        if (mParent.get() != null) {
            if (result != null) {
                mParent.get().onPlayDateCreated(result);
            } else {
                mParent.get().onPlayDateCreationFailed();
            }
        }
    }
}

