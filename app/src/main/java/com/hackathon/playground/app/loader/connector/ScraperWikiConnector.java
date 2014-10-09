package com.hackathon.playground.app.loader.connector;

import java.lang.reflect.Type;

/**
 * Author: Dave
 */
public class ScraperWikiConnector<T> extends Connector<T> {

    public ScraperWikiConnector(Type type) {
        this.type = type;
        this.baseURL = "https://free-ec2.scraperwiki.com";
    }
}

