package com.hackathon.playground.app.loader.connector;

import com.google.gson.reflect.TypeToken;
import com.hackathon.playground.app.model.PlayDate;

import java.util.ArrayList;
import java.lang.reflect.Type;

/**
 * Author: Dave
 */
public class PlaygroundServerConnector<T> extends Connector<T> {

    public PlaygroundServerConnector() {
        this.baseURL = "http://playground-mtgox.rhcloud.com/rest/playground/";
        this.type = new TypeToken<ArrayList<PlayDate>>(){}.getType();
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setPostData(PlayDate playDate) {
        this.postData = playDate;
    }
}
