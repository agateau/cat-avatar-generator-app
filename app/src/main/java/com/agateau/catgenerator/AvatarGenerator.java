/*
Copyright 2016 Aurélien Gâteau

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.agateau.catgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

import com.agateau.utils.log.NLog;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * Generate a bitmap based on avatar parts
 */
public class AvatarGenerator extends AsyncTask<Long, Void, Bitmap> {
    private static final int AVATAR_FULL_SIZE = 1024;
    private static final String AVATAR_PARTS_DIR = "parts";

    private final Context mContext;
    private final AvatarPartDb mAvatarPartDb;
    private final int mSize;
    private final WeakReference<ImageView> mImageViewReference;
    private final Random mRandom = new Random();

    public AvatarGenerator(Context context, AvatarPartDb avatarPartDb, ImageView imageView, int size) {
        mContext = context;
        mAvatarPartDb = avatarPartDb;
        mSize = size;
        mImageViewReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(Long... params) {
        long seed = params[0];
        mRandom.setSeed(seed);
        NLog.i("Starting seed=%d", seed);

        Bitmap bitmap = Bitmap.createBitmap(mSize, mSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Matrix matrix = new Matrix();
        float ratio = mSize / (float)AVATAR_FULL_SIZE;
        matrix.setScale(ratio, ratio);
        canvas.setMatrix(matrix);

        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);

        canvas.drawColor(Color.TRANSPARENT);
        for (String partName : AvatarPartDb.PART_NAMES) {
            if (isCancelled()) {
                NLog.i("Cancelled seed=%d", seed);
                return null;
            }
            int idx = mRandom.nextInt(mAvatarPartDb.getPartCount(partName));
            AvatarPart part = mAvatarPartDb.getPart(partName, idx);
            if (part == null) {
                continue;
            }

            String filePath = AVATAR_PARTS_DIR + "/" + part.filename;
            if (TextUtils.isEmpty(filePath)) {
                continue;
            }
            Bitmap partBitmap;
            try {
                InputStream stream;
                stream = mContext.getAssets().open(filePath);
                partBitmap = BitmapFactory.decodeStream(stream);
                stream.close();
            } catch (IOException e) {
                NLog.e("Failed to read part %s: %s", filePath, e);
                continue;
            }
            canvas.drawBitmap(partBitmap, part.x, part.y, paint);
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
