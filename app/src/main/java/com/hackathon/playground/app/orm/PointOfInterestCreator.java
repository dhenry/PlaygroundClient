package com.hackathon.playground.app.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.hackathon.playground.app.model.PointOfInterest;
import com.hackathon.playground.app.provider.PlaygroundSchema;

import java.util.ArrayList;

/**
 * Author: Dave
 */
public class PointOfInterestCreator {

    /**
     * Create a ContentValues from a provided PointOfInterest.
     *
     * @param data PointOfInterest to be converted.
     * @return ContentValues that is created from the PointOfInterest object
     */
    public static ContentValues getCVfromPointOfInterest(final PointOfInterest data) {
        ContentValues rValue = new ContentValues();
        rValue.put(PlaygroundSchema.PointOfInterest.Cols.NAME, data.getName());
        rValue.put(PlaygroundSchema.PointOfInterest.Cols.AMENITY_GROUP, data.getAmenityGroup());
        rValue.put(PlaygroundSchema.PointOfInterest.Cols.LOCATION, data.getLocation());
        rValue.put(PlaygroundSchema.PointOfInterest.Cols.LATITUDE, data.getLatitude());
        rValue.put(PlaygroundSchema.PointOfInterest.Cols.LONGITUDE, data.getLongitude());
        return rValue;
    }

    /**
     * Get all of the PointOfInterests from the passed in cursor.
     *
     * @param cursor
     *            passed in cursor to get PointOfInterest(s) of.
     * @return ArrayList<PointOfInterest> The list of PointOfInterests
     */
    public static ArrayList<PointOfInterest> getPointOfInterestArrayListFromCursor(
            Cursor cursor) {
        ArrayList<PointOfInterest> rValue = new ArrayList<PointOfInterest>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    rValue.add(getPointOfInterestFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        }
        return rValue;
    }

    /**
     * Get the first PointOfInterest from the passed in cursor.
     *
     * @param cursor
     *            passed in cursor
     * @return PointOfInterest object
     */
    public static PointOfInterest getPointOfInterestFromCursor(Cursor cursor) {

        long rowID = cursor.getLong(cursor
                .getColumnIndex(PlaygroundSchema.PointOfInterest.Cols.ID));
        String name = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.PointOfInterest.Cols.NAME));
        String location = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.PointOfInterest.Cols.LOCATION));
        String amenityGroup = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.PointOfInterest.Cols.AMENITY_GROUP));
        Double latitude = cursor.getDouble(cursor
                .getColumnIndex(PlaygroundSchema.PointOfInterest.Cols.LATITUDE));
        Double longitude = cursor.getDouble(cursor
                .getColumnIndex(PlaygroundSchema.PointOfInterest.Cols.LONGITUDE));

        // construct the returned object
        return new PointOfInterest(rowID, name, location, amenityGroup, latitude, longitude);
    }
}
