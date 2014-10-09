package com.hackathon.playground.app.orm;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;

import com.hackathon.playground.app.model.Amenity;
import com.hackathon.playground.app.model.PointOfInterest;
import com.hackathon.playground.app.provider.PlaygroundSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Dave
 */
public class PlaygroundResolver {
    private ContentResolver cr;

    private Uri pointOfInterestURI = PlaygroundSchema.PointOfInterest.CONTENT_URI;
    private Uri amenityURI = PlaygroundSchema.Amenity.CONTENT_URI;

    public PlaygroundResolver(Activity activity) {
        cr = activity.getContentResolver();
    }

    public Uri insert(final PointOfInterest pointOfInterest) {
        ContentValues tempCV = pointOfInterest.getCV();
        return cr.insert(pointOfInterestURI, tempCV);
    }

    public int insertPointsOfInterest(final List<PointOfInterest> pointsOfInterest) {
        ContentValues[] cvArray = new ContentValues[pointsOfInterest.size()];

        for(int i =0; i < pointsOfInterest.size(); i++) {
            cvArray[i] = pointsOfInterest.get(i).getCV();
        }
        return cr.bulkInsert(pointOfInterestURI, cvArray);
    }

    public Uri insert(final Amenity amenity) {
        ContentValues tempCV = amenity.getCV();
        return cr.insert(amenityURI, tempCV);
    }

    public int insertAmenities(final List<Amenity> amenities) {
        ContentValues[] cvArray = new ContentValues[amenities.size()];

        for(int i =0; i < amenities.size(); i++) {
            cvArray[i] = amenities.get(i).getCV();
        }
        return cr.bulkInsert(amenityURI, cvArray);
    }

    /**
     * Get all the PointOfInterest objects current stored in the Content Provider
     *
     * @return an ArrayList containing all the PointOfInterest objects
     * @throws RemoteException
     */
    public ArrayList<PointOfInterest> getAllPointsOfInterest() throws RemoteException {
        return queryPointsOfInterest(null, null, null, null);
    }

    /**
     * Get all the Amenity objects current stored in the Content Provider
     *
     * @return an ArrayList containing all the Amenity objects
     * @throws RemoteException
     */
    public ArrayList<Amenity> getAllAmenities() throws RemoteException {
        return queryAmenities(null, null, null, null);
    }


    /**
     * Get all the Amenity objects current stored in the Content Provider
     *
     * @return an ArrayList containing all the Amenity objects
     * @throws RemoteException
     */
    public ArrayList<Amenity> getAmenitiesForGroup(String amenityGroup) throws RemoteException {
        String[] args = { amenityGroup };
        return queryAmenities(null, PlaygroundSchema.Amenity.Cols.AMENITY_GROUP + " = ? ", args, null);
    }


    /**
     * Query the PointOfInterest table
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return an ArrayList of PointOfInterest objects
     * @throws RemoteException
     */
    public ArrayList<PointOfInterest> queryPointsOfInterest(final String[] projection,
                                                            final String selection, final String[] selectionArgs,
                                                            final String sortOrder) throws RemoteException {
        // query the C.R.
        Cursor result = cr.query(pointOfInterestURI, projection, selection,
                selectionArgs, sortOrder);
        // make return object
        ArrayList<PointOfInterest> rValue = new ArrayList<>();
        // convert cursor to return object
        rValue.addAll(PointOfInterestCreator.getPointOfInterestArrayListFromCursor(result));
        result.close();
        // return 'return object'
        return rValue;
    }

    /**
     * Query the Amenity table
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return an ArrayList of PointOfInterest objects
     * @throws RemoteException
     */
    public ArrayList<Amenity> queryAmenities(final String[] projection,
                                                            final String selection, final String[] selectionArgs,
                                                            final String sortOrder) throws RemoteException {
        // query the C.R.
        Cursor result = cr.query(amenityURI, projection, selection,
                selectionArgs, sortOrder);
        // make return object
        ArrayList<Amenity> rValue = new ArrayList<>();
        // convert cursor to return object
        rValue.addAll(AmenityCreator.getAmenityArrayListFromCursor(result));
        result.close();
        // return 'return object'
        return rValue;
    }

    public PointOfInterest getPointOfInterest(Long pointOfInterestId) throws RemoteException {
        String[] args = { pointOfInterestId.toString() };
        List<PointOfInterest> results = queryPointsOfInterest(null, PlaygroundSchema.PointOfInterest.Cols.ID + " = ? ", args, null);

        if (results.size() > 1) {
            throw new RuntimeException("getPointOfInterest() returned non-unique result for " + pointOfInterestId);
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            return null;
        }
    }

    public PointOfInterest getPointOfInterestByName(String name) throws RemoteException {
        String[] args = { name };
        List<PointOfInterest> results = queryPointsOfInterest(null,
                PlaygroundSchema.PointOfInterest.Cols.NAME + " = ?", args, null);

        if (results.size() > 1) {
            throw new RuntimeException("getPointOfInterestByName() returned non-unique result for "
                                        + name );
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            return null;
        }
    }
}
