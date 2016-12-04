package com.agateau.catgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.agateau.utils.log.NLog;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Generate a bitmap based on avatar parts
 */
public class AvatarGenerator extends AsyncTask<Long, Void, Bitmap> {
    private final Context mContext;
    private final PartDb mPartDb;
    private final int mSize;
    private final WeakReference<ImageView> mImageViewReference;
    private final Random mRandom = new Random();

    public AvatarGenerator(Context context, PartDb partDb, ImageView imageView, int size) {
        mContext = context;
        mPartDb = partDb;
        mSize = size;
        mImageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(Long... params) {
        long seed = params[0];
        mRandom.setSeed(seed);
        NLog.i("Starting seed=%d", seed);

        Bitmap bitmap = Bitmap.createBitmap(mSize, mSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(Color.TRANSPARENT);
        for (String partName : PartDb.PART_NAMES) {
            if (isCancelled()) {
                NLog.i("Cancelled seed=%d", seed);
                return null;
            }
            int idx = mRandom.nextInt(mPartDb.getPartCount(partName));
            int id = mPartDb.getPart(partName, idx);
            Drawable drawable = mContext.getResources().getDrawable(id);
            drawable.setBounds(0, 0, mSize, mSize);
            drawable.draw(canvas);
        }
        NLog.i("Done seed=%d", seed);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView view = mImageViewReference.get();
        if (view != null && bitmap != null) {
            view.setImageBitmap(bitmap);
        }
    }
}
