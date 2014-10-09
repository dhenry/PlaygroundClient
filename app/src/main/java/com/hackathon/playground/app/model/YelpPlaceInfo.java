package com.hackathon.playground.app.model;

import java.math.BigDecimal;

/**
 * Author: Dave
 */
public class YelpPlaceInfo {
    private final BigDecimal rating;
    private final boolean isClosed;
    private final String imageUrl;

    public YelpPlaceInfo(BigDecimal rating, String imageUrl, boolean isClosed) {
        this.rating = rating;
        this.isClosed = isClosed;
        this.imageUrl = imageUrl;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
