package com.hackathon.playground.app.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.hackathon.playground.app.model.Amenity;
import com.hackathon.playground.app.provider.PlaygroundSchema;

import java.util.ArrayList;

/**
 * Author: Dave
 */
public class AmenityCreator {

    /**
     * Create a ContentValues from a provided Amenity.
     *
     * @param data Amenity to be converted.
     * @return ContentValues that is created from the Amenity object
     */
    public static ContentValues getCVfromAmenity(final Amenity data) {
        ContentValues rValue = new ContentValues();
        rValue.put(PlaygroundSchema.Amenity.Cols.IDX, data.getIndex());
        rValue.put(PlaygroundSchema.Amenity.Cols.AMENITY_GROUP, data.getAmenityGroup());
        rValue.put(PlaygroundSchema.Amenity.Cols.AMENITY, data.getAmenity());
        return rValue;
    }

    /**
     * Get all of the Amenitys from the passed in cursor.
     *
     * @param cursor
     *            passed in cursor to get Amenity(s) of.
     * @return ArrayList<Amenity> The list of Amenitys
     */
    public static ArrayList<Amenity> getAmenityArrayListFromCursor(
            Cursor cursor) {
        ArrayList<Amenity> rValue = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    rValue.add(getAmenityFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        }
        return rValue;
    }

    /**
     * Get the first Amenity from the passed in cursor.
     *
     * @param cursor
     *            passed in cursor
     * @return Amenity object
     */
    public static Amenity getAmenityFromCursor(Cursor cursor) {

        long rowID = cursor.getLong(cursor
                .getColumnIndex(PlaygroundSchema.Amenity.Cols.ID));
        String index = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.Amenity.Cols.IDX));
        String amenityGroup = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.Amenity.Cols.AMENITY_GROUP));
        String amenity = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.Amenity.Cols.AMENITY));

        // construct the returned object
        return new Amenity(rowID, index, amenityGroup, amenity);
    }
}
