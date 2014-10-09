package com.hackathon.playground.app.provider;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Author: Dave
 */
public class PlaygroundSchema {
    /**
     * Project Related Constants
     */
    public static final String ORGANIZATIONAL_NAME = "com.hackathon";
    public static final String PROJECT_NAME = "playground";

    /**
     * ContentProvider Related Constants
     */
    public static final String AUTHORITY = ORGANIZATIONAL_NAME + "."
            + PROJECT_NAME + ".playgroundprovider";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PointOfInterest.PATH, PointOfInterest.PATH_URI_CODE);
        uriMatcher.addURI(AUTHORITY, PointOfInterest.PATH_FOR_ID, PointOfInterest.PATH_FOR_ID_URI_CODE);
        uriMatcher.addURI(AUTHORITY, Amenity.PATH, Amenity.PATH_URI_CODE);
        uriMatcher.addURI(AUTHORITY, Amenity.PATH_FOR_ID, Amenity.PATH_FOR_ID_URI_CODE);
        uriMatcher.addURI(AUTHORITY, PlayDate.PATH, PlayDate.PATH_URI_CODE);
        uriMatcher.addURI(AUTHORITY, PlayDate.PATH_FOR_ID, PlayDate.PATH_FOR_ID_URI_CODE);
    }

    // Representation of the PointOfInterest object
    public static class PointOfInterest {
        // define a URI paths to access entity
        // BASE_URI/pointOfInterest - for list of pointOfInterest(s)
        // BASE_URI/pointOfInterest/* - retrieve specific pointOfInterest by id
        public static final String PATH = "pointOfInterest";
        public static final int PATH_URI_CODE = 1;

        public static final String PATH_FOR_ID = "pointOfInterest/*";
        public static final int PATH_FOR_ID_URI_CODE = 2;

        // URI for all content stored as a pointOfInterest entity
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH).build();

        // the names and order of ALL columns, including internal use ones
        public static final String[] ALL_COLUMN_NAMES = { Cols.ID,
                Cols.NAME, Cols.LOCATION, Cols.AMENITY_GROUP, Cols.LATITUDE, Cols.LONGITUDE };

        public static ContentValues initializeWithDefault(final ContentValues assignedValues) {

            final ContentValues setValues = (assignedValues == null) ? new ContentValues()
                    : assignedValues;
            if (!setValues.containsKey(Cols.NAME)) {
                setValues.put(Cols.NAME, "");
            }
            if (!setValues.containsKey(Cols.LOCATION)) {
                setValues.put(Cols.LOCATION, "");
            }
            if (!setValues.containsKey(Cols.AMENITY_GROUP)) {
                setValues.put(Cols.AMENITY_GROUP, "");
            }
            if (!setValues.containsKey(Cols.LATITUDE)) {
                setValues.put(Cols.LATITUDE, 0);
            }
            if (!setValues.containsKey(Cols.LONGITUDE)) {
                setValues.put(Cols.LONGITUDE, 0);
            }
            return setValues;
        }

        // a static class to store columns in entity
        public static class Cols {
            public static final String ID = BaseColumns._ID; // convention
            // The name and column index of each column in your database
            public static final String NAME = "NAME";
            public static final String LOCATION = "LOCATION";
            public static final String AMENITY_GROUP = "AMENITY_GROUP";
            public static final String LATITUDE = "LATITUDE";
            public static final String LONGITUDE = "LONGITUDE";
        }
    }

    // Representation of the Amenity object
    public static class Amenity {
        // define a URI paths to access entity
        // BASE_URI/amenity - for list of amenity(s)
        // BASE_URI/amenity/* - retrieve specific amenity by id
        public static final String PATH = "amenity";
        public static final int PATH_URI_CODE = 3;

        public static final String PATH_FOR_ID = "amenity/*";
        public static final int PATH_FOR_ID_URI_CODE = 4;

        // URI for all content stored as a pointOfInterest entity
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH).build();

        // the names and order of ALL columns, including internal use ones
        public static final String[] ALL_COLUMN_NAMES = { Cols.IDX,
                Cols.AMENITY_GROUP, Cols.AMENITY };

        public static ContentValues initializeWithDefault(final ContentValues assignedValues) {

            final ContentValues setValues = (assignedValues == null) ? new ContentValues()
                    : assignedValues;
            if (!setValues.containsKey(Cols.IDX)) {
                setValues.put(Cols.IDX, 0);
            }
            if (!setValues.containsKey(Cols.AMENITY_GROUP)) {
                setValues.put(Cols.AMENITY_GROUP, "");
            }
            if (!setValues.containsKey(Cols.AMENITY)) {
                setValues.put(Cols.AMENITY, "");
            }
            return setValues;
        }

        // a static class to store columns in entity
        public static class Cols {
            public static final String ID = BaseColumns._ID; // convention
            // The name and column index of each column in your database
            public static final String IDX = "IDX";
            public static final String AMENITY_GROUP = "AMENITY_GROUP";
            public static final String AMENITY = "AMENITY";
        }
    }

    public static class PlayDate {
        // define a URI paths to access entity
        // BASE_URI/PlayDate - for list of PlayDate(s)
        // BASE_URI/PlayDate/* - retrieve specific PlayDate by id
        public static final String PATH = "playDate";
        public static final int PATH_URI_CODE = 5;

        public static final String PATH_FOR_ID = "playDate/*";
        public static final int PATH_FOR_ID_URI_CODE = 6;

        // URI for all content stored as a pointOfInterest entity
        public static final Uri CONTENT_URI = BASE_URI.buildUpon()
                .appendPath(PATH).build();

        // the names and order of ALL columns, including internal use ones
        public static final String[] ALL_COLUMN_NAMES = { Cols.ID,
                Cols.LOCATION, Cols.DESCRIPTION, Cols.ORGANISER, Cols.START_TIME, Cols.END_TIME,
                Cols.LATITUDE, Cols.LONGITUDE};

        public static ContentValues initializeWithDefault(final ContentValues assignedValues) {

            final ContentValues setValues = (assignedValues == null) ? new ContentValues()
                    : assignedValues;
            if (!setValues.containsKey(Cols.ID)) {
                setValues.put(Cols.ID, "");
            }
            if (!setValues.containsKey(Cols.LOCATION)) {
                setValues.put(Cols.LOCATION, "");
            }
            if (!setValues.containsKey(Cols.DESCRIPTION)) {
                setValues.put(Cols.DESCRIPTION, "");
            }
            if (!setValues.containsKey(Cols.ORGANISER)) {
                setValues.put(Cols.ORGANISER, "");
            }
            if (!setValues.containsKey(Cols.START_TIME)) {
                setValues.put(Cols.START_TIME, 0);
            }
            if (!setValues.containsKey(Cols.END_TIME)) {
                setValues.put(Cols.END_TIME, 0);
            }
            if (!setValues.containsKey(Cols.LATITUDE)) {
                setValues.put(Cols.LATITUDE, 0);
            }
            if (!setValues.containsKey(Cols.LONGITUDE)) {
                setValues.put(Cols.LONGITUDE, 0);
            }
            return setValues;
        }

        // a static class to store columns in entity
        public static class Cols {
            //public static final String ID = BaseColumns._ID; // convention
            // The name and column index of each column in your database
            public static final String ID = "NAME";
            public static final String LOCATION = "LOCATION";
            public static final String DESCRIPTION = "DESCRIPTION";
            public static final String ORGANISER = "ORGANISER";
            public static final String START_TIME = "START_TIME";
            public static final String END_TIME = "END_TIME";
            public static final String LATITUDE = "LATITUDE";
            public static final String LONGITUDE = "LONGITUDE";
        }
    }
}

