package com.hackathon.playground.app.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.hackathon.playground.app.model.PlayDate;
import com.hackathon.playground.app.provider.PlaygroundSchema;

import java.util.ArrayList;

public class PlayDateCreator {

    public static ContentValues getCVfromPlayDate(final PlayDate data) {
        ContentValues rValue = new ContentValues();
        rValue.put(PlaygroundSchema.PlayDate.Cols.ID, data.getId());
        rValue.put(PlaygroundSchema.PlayDate.Cols.LOCATION, data.getLocation());
        rValue.put(PlaygroundSchema.PlayDate.Cols.DESCRIPTION, data.getDescription());
        rValue.put(PlaygroundSchema.PlayDate.Cols.ORGANISER, data.getOrganiser());
        rValue.put(PlaygroundSchema.PlayDate.Cols.START_TIME, data.getStartTime());
        rValue.put(PlaygroundSchema.PlayDate.Cols.END_TIME, data.getEndTime());
        rValue.put(PlaygroundSchema.PlayDate.Cols.LATITUDE, data.getLatitude());
        rValue.put(PlaygroundSchema.PlayDate.Cols.LONGITUDE, data.getLongitude());

        return rValue;
    }

    public static ArrayList<PlayDate> getPlayDateArrayListFromCursor(Cursor cursor) {
        ArrayList<PlayDate> rValue = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    rValue.add(getPlayDateFromCursor(cursor));
                } while (cursor.moveToNext());
            }
        }
        return rValue;
    }

    public static PlayDate getPlayDateFromCursor(Cursor cursor) {

        String id = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.PlayDate.Cols.ID));
        String location = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.PlayDate.Cols.LOCATION));
        String description = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.PlayDate.Cols.DESCRIPTION));
        String organiser = cursor.getString(cursor
                .getColumnIndex(PlaygroundSchema.PlayDate.Cols.ORGANISER));
        Long startTime = cursor.getLong(cursor
                .getColumnIndex(PlaygroundSchema.PlayDate.Cols.START_TIME));
        Long endTime = cursor.getLong(cursor
                .getColumnIndex(PlaygroundSchema.PlayDate.Cols.END_TIME));
        Double latitude = cursor.getDouble(cursor
                .getColumnIndex(PlaygroundSchema.PlayDate.Cols.LATITUDE));
        Double longitude = cursor.getDouble(cursor
                .getColumnIndex(PlaygroundSchema.PlayDate.Cols.LONGITUDE));

        // construct the returned object
        return new PlayDate(id, location, description, organiser, startTime, endTime, latitude, longitude);
    }
}
