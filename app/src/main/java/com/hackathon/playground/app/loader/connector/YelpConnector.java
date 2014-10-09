package com.hackathon.playground.app.loader.connector;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hackathon.playground.app.loader.YelpApi2;
import com.hackathon.playground.app.model.PointOfInterest;
import com.hackathon.playground.app.model.YelpPlaceInfo;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.math.BigDecimal;

/**
 * Author: Dave
 */
public class YelpConnector extends Connector {

    public final static String LOG_TAG = YelpConnector.class.getCanonicalName();

    private static final String consumerKey = "f2Y2TXh_59hvjRvEkYfYiQ";
    private static final String consumerSecret = "j3VgWqZGYjRHsz50AC-VpXBLx6I";
    private static final String token = "vKotLKN3FMZCf7kS-NlX-TF3z-HqApHt";
    private static final String tokenSecret = "v4scTMKewqeQ48yWEzSnP7GD4lQ";

    private OAuthService service;
    private Token accessToken;

    public YelpConnector() {

        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }

    /**
    * Search with term and location.
    *
    * @param term Search term
    * @param latitude Latitude
    * @param longitude Longitude
    * @return JSON string response
    */
    public String search(String term, double latitude, double longitude) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    public YelpPlaceInfo getYelpInfo(PointOfInterest pointOfInterest) {

        String response = search(pointOfInterest.getName(), pointOfInterest.getLatitude(), pointOfInterest.getLongitude());

        JsonParser jsonParser = new JsonParser();
        try {
            JsonElement json = jsonParser.parse(response);

            JsonObject businessGlob = json.getAsJsonObject()
                                    .getAsJsonArray("businesses")
                                    .get(0)
                                    .getAsJsonObject();

            // try to get the 'business' we're after
            if (businessGlob.get("distance").getAsBigDecimal().compareTo(BigDecimal.ZERO) == 0) {


                return new YelpPlaceInfo(businessGlob.get("rating").getAsBigDecimal(),
                                         businessGlob.get("image_url").getAsString(),
                                         businessGlob.get("is_closed").getAsBoolean());
            }
            //return new LatLng(resultGlob.get("lat").getAsDouble(), resultGlob.get("lng").getAsDouble());
            return null;
        } catch (IndexOutOfBoundsException e) {
            Log.e(LOG_TAG, "Couldn't get yelp information for: " + pointOfInterest.getName() + "\n" +
                    "Caught IndexOutOfBoundsException => " + e.getMessage());
            return null;
        }
    }
}
