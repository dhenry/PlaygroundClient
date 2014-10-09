package com.hackathon.playground.app.provider;

/**
 * Author: Dave
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Author: Dave
 */
public class PlaygroundProvider extends ContentProvider {


    private SQLiteDatabase database;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "playground";


    private static final String CREATE_POINT_OF_INTEREST_TABLE = " CREATE TABLE "
            + PlaygroundSchema.PointOfInterest.PATH + " (" + PlaygroundSchema.PointOfInterest.Cols.ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PlaygroundSchema.PointOfInterest.Cols.NAME + " STRING NOT NULL, "
            + PlaygroundSchema.PointOfInterest.Cols.AMENITY_GROUP + " STRING NOT NULL, "
            + PlaygroundSchema.PointOfInterest.Cols.LATITUDE + " REAL NOT NULL, "
            + PlaygroundSchema.PointOfInterest.Cols.LONGITUDE + " REAL NOT NULL, "
            + PlaygroundSchema.PointOfInterest.Cols.LOCATION + " STRING NOT NULL);";

    private static final String CREATE_AMENITIES_TABLE = " CREATE TABLE "
            + PlaygroundSchema.Amenity.PATH + " (" + PlaygroundSchema.Amenity.Cols.ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PlaygroundSchema.Amenity.Cols.AMENITY + " STRING NOT NULL, "
            + PlaygroundSchema.Amenity.Cols.AMENITY_GROUP + " STRING NOT NULL, "
            + PlaygroundSchema.Amenity.Cols.IDX + " STRING NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_POINT_OF_INTEREST_TABLE);
            db.execSQL(CREATE_AMENITIES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + PlaygroundSchema.PointOfInterest.PATH);
            db.execSQL("DROP TABLE IF EXISTS " + PlaygroundSchema.Amenity.PATH);
            onCreate(db);
        }

        // uncomment to recreate DB on startup
        /*@Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("DROP TABLE IF EXISTS " + PlaygroundSchema.PointOfInterest.PATH);
            db.execSQL("DROP TABLE IF EXISTS " + PlaygroundSchema.Amenity.PATH);
            onCreate(db);
        }*/
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        int rowsDeleted;

        switch (PlaygroundSchema.uriMatcher.match(uri)){
            case PlaygroundSchema.PointOfInterest.PATH_URI_CODE:
            case PlaygroundSchema.PointOfInterest.PATH_FOR_ID_URI_CODE:
                rowsDeleted = database.delete(PlaygroundSchema.PointOfInterest.PATH, whereClause, whereArgs);
                break;
            case PlaygroundSchema.Amenity.PATH_URI_CODE:
            case PlaygroundSchema.Amenity.PATH_FOR_ID_URI_CODE:
                rowsDeleted = database.delete(PlaygroundSchema.Amenity.PATH, whereClause, whereArgs);
                break;
            default: throw new SQLException("Failed to delete row(s) from " + uri);
        }

        if (rowsDeleted > 0) {
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(PlaygroundSchema.PointOfInterest.CONTENT_URI, null);
            }
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // unimplemented
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID;
        Uri path;

        switch (PlaygroundSchema.uriMatcher.match(uri)){
            case PlaygroundSchema.PointOfInterest.PATH_URI_CODE:
            case PlaygroundSchema.PointOfInterest.PATH_FOR_ID_URI_CODE:
                rowID = database.insert(PlaygroundSchema.PointOfInterest.PATH, "",
                        PlaygroundSchema.PointOfInterest.initializeWithDefault(values));
                path = PlaygroundSchema.PointOfInterest.CONTENT_URI;
                break;
            case PlaygroundSchema.Amenity.PATH_URI_CODE:
            case PlaygroundSchema.Amenity.PATH_FOR_ID_URI_CODE:
                rowID = database.insert(PlaygroundSchema.Amenity.PATH, "",
                        PlaygroundSchema.Amenity.initializeWithDefault(values));
                path = PlaygroundSchema.Amenity.CONTENT_URI;
                break;
            default: throw new SQLException("Failed to insert record into " + uri);
        }

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(path, rowID);
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(_uri, null);
            }
            return _uri;
        }

        throw new SQLException("Failed to add record into" + uri);
    }

    @Override
    public boolean onCreate() {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        database = dbHelper.getWritableDatabase();
        return (database != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String tableToQuery;

        switch (PlaygroundSchema.uriMatcher.match(uri)){
            case PlaygroundSchema.PointOfInterest.PATH_URI_CODE:
            case PlaygroundSchema.PointOfInterest.PATH_FOR_ID_URI_CODE:
                tableToQuery = PlaygroundSchema.PointOfInterest.PATH;
                break;
            case PlaygroundSchema.Amenity.PATH_URI_CODE:
            case PlaygroundSchema.Amenity.PATH_FOR_ID_URI_CODE:
                tableToQuery = PlaygroundSchema.Amenity.PATH;
                break;
            default: throw new SQLException("Failed to find table to query " + uri);
        }

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(tableToQuery);

        Cursor c = qb.query(database, projection, selection, selectionArgs, null, null, sortOrder);

        if (c != null && getContext() != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String whereClause, String[] whereArgs) {

        switch (PlaygroundSchema.uriMatcher.match(uri)){
            case PlaygroundSchema.PointOfInterest.PATH_URI_CODE:
            case PlaygroundSchema.PointOfInterest.PATH_FOR_ID_URI_CODE:
                return database.update(PlaygroundSchema.PointOfInterest.PATH, contentValues, whereClause, whereArgs);
            case PlaygroundSchema.Amenity.PATH_URI_CODE:
            case PlaygroundSchema.Amenity.PATH_FOR_ID_URI_CODE:
                return database.update(PlaygroundSchema.Amenity.PATH, contentValues, whereClause, whereArgs);
            default: throw new SQLException("Failed to find table to query " + uri);
        }
    }
}

