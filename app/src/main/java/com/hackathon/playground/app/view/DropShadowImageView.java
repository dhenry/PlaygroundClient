package com.hackathon.playground.app.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Author: Dave
 */
public class DropShadowImageView extends ImageView {

    private Bitmap bitmap;
    private Paint mShadow;

    public DropShadowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mShadow = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // radius=10, y-offset=2, color=black
        mShadow.setShadowLayer(10.0f, 5.0f, 5.0f, 0xFF000000);

        setLayerType(LAYER_TYPE_SOFTWARE, mShadow);

        if (this.bitmap != null) {
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, mShadow);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        this.bitmap = bm;
    }
}
