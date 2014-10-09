package com.hackathon.playground.app.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hackathon.playground.app.orm.PointOfInterestCreator;

/**
 * Representation of a point of interest to be added to the map
 */
public class PointOfInterest implements Parcelable {

    private long KEY_ID;

    @SerializedName("name")
    private String name;
    @SerializedName("location")
    private String location;
    @SerializedName("amenity_group")
    private String amenityGroup;

    private Double latitude;
    private Double longitude;

    public PointOfInterest() {
    }

    public PointOfInterest(long rowID, String name, String location, String amenityGroup, Double latitude, Double longitude) {
        this.KEY_ID = rowID;
        this.name = name;
        this.location = location;
        this.amenityGroup = amenityGroup;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PointOfInterest(Parcel source) {
        this.KEY_ID = source.readLong();
        this.name = source.readString();
        this.location = source.readString();
        this.amenityGroup = source.readString();
        this.latitude = source.readDouble();
        this.longitude = source.readDouble();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAmenityGroup() {
        return amenityGroup;
    }

    public void setAmenityGroup(String amenityGroup) {
        this.amenityGroup = amenityGroup;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public ContentValues getCV() {
        return PointOfInterestCreator.getCVfromPointOfInterest(this);
    }

    public Long getId() {
        return KEY_ID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.KEY_ID);
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeString(this.amenityGroup);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    public static final Parcelable.Creator<PointOfInterest> CREATOR = new Parcelable.Creator<PointOfInterest>() {

        @Override
        public PointOfInterest createFromParcel(Parcel source) {
            return new PointOfInterest(source);
        }

        @Override
        public PointOfInterest[] newArray(int size) {
            return new PointOfInterest[size];
        }
    };
}
