package com.hackathon.playground.app.model;

import android.content.ContentValues;

import com.google.gson.annotations.SerializedName;
import com.hackathon.playground.app.orm.AmenityCreator;

/**
 * Author: Dave
 */
public class Amenity {

    private long KEY_ID;

    @SerializedName("id")
    private String index;
    @SerializedName("amenity_group")
    private String amenityGroup;
    @SerializedName("amenity")
    private String amenity;

    public Amenity(long rowID, String index, String amenityGroup, String amenity) {
        this.KEY_ID = rowID;
        this.index = index;
        this.amenityGroup = amenityGroup;
        this.amenity = amenity;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getAmenityGroup() {
        return amenityGroup;
    }

    public void setAmenityGroup(String amenityGroup) {
        this.amenityGroup = amenityGroup;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public ContentValues getCV() {
        return AmenityCreator.getCVfromAmenity(this);
    }
}
