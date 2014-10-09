package com.hackathon.playground.app.loader.connector;

import android.util.JsonWriter;
import android.util.Log;

import com.google.gson.Gson;
import com.hackathon.playground.app.model.PlayDate;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Author: Dave
 */
public abstract class Connector<T> {

    public static enum REQUEST_TYPE {
        GET,
        POST,
        PUT,
        DELETE
    }

    protected Type type;
    protected String baseURL;
    protected PlayDate postData;

    protected InputStream retrieveStream(String url) {
        return retrieveStream(url, REQUEST_TYPE.GET);
    }

    protected InputStream retrieveStream(String url, REQUEST_TYPE requestType) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpRequestBase requestBase = getRequestBase(requestType, url);
        Header header = new BasicHeader("Content-Type", "application/json");
        requestBase.addHeader(header);

        if (requestType == REQUEST_TYPE.POST) {
            Gson gson = new Gson();
            String json = gson.toJson(postData);
            try {
                ((HttpPost)requestBase).setEntity(new StringEntity(json));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                // TODO
            }
        }

        try {
            HttpResponse getResponse = client.execute(requestBase);
            final int statusCode = getResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(),
                        "Error " + statusCode + " for URL " + url);
                return null;
            }
            HttpEntity getResponseEntity = getResponse.getEntity();
            return getResponseEntity.getContent();
        }
        catch (IOException e) {
            requestBase.abort();
            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        return null;
    }

    private HttpRequestBase getRequestBase(REQUEST_TYPE requestType, String url) {
        switch(requestType) {
            case POST:
                return new HttpPost(url);
            case PUT:
                return new HttpPut(url);
            case DELETE:
                return new HttpDelete(url);
            default:
                return new HttpGet(url);
        }
    }


    public List<T> getJsonResultList(String urlSuffix) {

        InputStream source = retrieveStream(baseURL + urlSuffix);
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(source);

        return gson.fromJson(reader, type);
    }

    public T makeRequest(String urlSuffix, REQUEST_TYPE requestType) {

        InputStream source = retrieveStream(baseURL + urlSuffix, requestType);
        Gson gson = new Gson();
        Reader reader = new InputStreamReader(source);

        return gson.fromJson(reader, type);
    }
}
