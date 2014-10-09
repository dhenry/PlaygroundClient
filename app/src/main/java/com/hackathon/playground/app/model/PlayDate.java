package com.hackathon.playground.app.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.hackathon.playground.app.orm.PlayDateCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Dave
 */
public class PlayDate implements Parcelable {

    @SerializedName("id")
    private String id;
    @SerializedName("location")
    private String location;
    @SerializedName("description")
    private String description;
    @SerializedName("organiser")
    private String organiser;
    @SerializedName("startTime")
    private Long startTime;
    @SerializedName("endTime")
    private Long endTime;
    @SerializedName("latitude")
    private Double latitude;
    @SerializedName("longitude")
    private Double longitude;
    @SerializedName("attendees")
    private List<String> attendees;

    public PlayDate() {
    }

    public PlayDate(String id, String location, String description, String organiser, Long startTime,
                    Long endTime, Double latitude, Double longitude) {
        this.id = id;
        this.location = location;
        this.description = description;
        this.organiser = organiser;
        this.startTime = startTime;
        this.endTime = endTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private PlayDate(Parcel source){
        this.id = source.readString();
        this.location = source.readString();
        this.description = source.readString();
        this.organiser = source.readString();
        this.startTime = source.readLong();
        this.endTime = source.readLong();
        this.latitude = source.readDouble();
        this.longitude = source.readDouble();
        this.attendees = new ArrayList<>();
        source.readStringList(this.attendees);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganiser() {
        return organiser;
    }

    public void setOrganiser(String organiser) {
        this.organiser = organiser;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    public ContentValues getCV() {
        return PlayDateCreator.getCVfromPlayDate(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.location);
        dest.writeString(this.description);
        dest.writeString(this.organiser);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeStringList(this.getAttendees());
    }

    public static final Parcelable.Creator<PlayDate> CREATOR = new Parcelable.Creator<PlayDate>() {

        @Override
        public PlayDate createFromParcel(Parcel source) {
            return new PlayDate(source);
        }

        @Override
        public PlayDate[] newArray(int size) {
            return new PlayDate[size];
        }
    };
}



