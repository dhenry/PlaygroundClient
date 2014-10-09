package com.hackathon.playground.app.loader.connector;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hackathon.playground.app.model.PointOfInterest;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Author: Dave
 */
public class GeolocationConnector extends Connector {

    public final static String LOG_TAG = GeolocationConnector.class.getCanonicalName();

    public GeolocationConnector() {
        this.baseURL = "http://maps.googleapis.com/maps/api/geocode/json?address=";
    }

    public LatLng getResult(PointOfInterest pointOfInterest) {

        String urlSuffix = pointOfInterest.getLocation();
        if (urlSuffix == null) {
            urlSuffix = pointOfInterest.getName();
        }
        InputStream source = retrieveStream(baseURL + urlSuffix.trim().replaceAll("\\s+", "+").replaceAll("&amp", ""));
        Reader reader = new InputStreamReader(source);

        JsonParser jsonParser = new JsonParser();
        try {
            JsonElement json = jsonParser.parse(reader);

            JsonObject resultGlob = json.getAsJsonObject()
                    .get("results").getAsJsonArray().get(0).getAsJsonObject()
                    .get("geometry").getAsJsonObject()
                    .get("location").getAsJsonObject();

            return new LatLng(resultGlob.get("lat").getAsDouble(), resultGlob.get("lng").getAsDouble());
        } catch (IndexOutOfBoundsException e) {
            Log.e(LOG_TAG, "Couldn't get location information for: " + urlSuffix + "\n" +
                    "Caught IndexOutOfBoundsException => " + e.getMessage());
            return null;
        }
    }
}


