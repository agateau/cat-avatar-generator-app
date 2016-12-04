package com.agateau.catgenerator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.agateau.utils.log.NLog;

import java.util.Random;

/**
 * A view which can draw an avatar
 */
public class AvatarView extends View {
    private final PartDb mPartDb;
    private final Random mRandom = new Random();
    private long mSeed = 0;

    public AvatarView(PartDb partDb, Context context) {
        super(context);
        mPartDb = partDb;
    }

    public void setSeed(long seed) {
        mSeed = seed;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRandom.setSeed(mSeed);

        canvas.drawColor(Color.WHITE);
        int size = Math.min(getWidth(), getHeight());
        for (String partName : PartDb.PART_NAMES) {
            int idx = mRandom.nextInt(mPartDb.getPartCount(partName));
            int id = mPartDb.getPart(partName, idx);
            Drawable drawable = getResources().getDrawable(id);
            drawable.setBounds(0, 0, size, size);
            drawable.draw(canvas);
        }
    }
}
