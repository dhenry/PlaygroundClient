package com.hackathon.playground.app.loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.hackathon.playground.app.activity.PointOfInterestDetailsActivity;

import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Author: Dave
 */
public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

    private final WeakReference<PointOfInterestDetailsActivity> mParent;

    public ImageDownloaderTask(PointOfInterestDetailsActivity activity) {
        mParent = new WeakReference<>(activity);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (mParent.get() != null) {
            mParent.get().setThumbnailBitmap(result);
        }
    }
}
