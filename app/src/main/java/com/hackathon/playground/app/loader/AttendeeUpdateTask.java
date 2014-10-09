package com.hackathon.playground.app.loader;

import android.os.AsyncTask;

import com.hackathon.playground.app.activity.AttendeesFragment;
import com.hackathon.playground.app.loader.connector.Connector;
import com.hackathon.playground.app.loader.connector.PlaygroundServerConnector;
import com.hackathon.playground.app.model.PlayDate;

import java.lang.ref.WeakReference;

/**
 * Author: Dave
 */
public class AttendeeUpdateTask extends AsyncTask<String, Void, PlayDate> {

    private final WeakReference<AttendeesFragment> mParent;

    public AttendeeUpdateTask(AttendeesFragment fragment) {
        mParent = new WeakReference<>(fragment);
    }

    protected PlayDate doInBackground(String... args) {
        // retrieve the Amenities
        PlaygroundServerConnector<PlayDate> playgroundServerConnector = new PlaygroundServerConnector<>();
        playgroundServerConnector.setType(PlayDate.class);
        String urlSuffix = "playdates/" + args[0] + "/" + args[1] + "?attendee=" + args[2];
        PlayDate updatedPlayDate = playgroundServerConnector.makeRequest(urlSuffix, Connector.REQUEST_TYPE.PUT);

        return updatedPlayDate;
    }

    protected void onPostExecute(PlayDate result) {
        if (mParent.get() != null) {
            mParent.get().refreshView(result);
        }
    }
}
