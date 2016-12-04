package com.agateau.catgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.agateau.utils.log.NLog;

import java.util.Random;

/**
 * Generate a bitmap based on avatar parts
 */
public class AvatarGenerator {
    public static Bitmap generate(Context context, PartDb partDb, long seed, int size) {
        Random random = new Random(seed);

        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawColor(Color.WHITE);
        for (String partName : PartDb.PART_NAMES) {
            int idx = random.nextInt(partDb.getPartCount(partName));
            NLog.d("partName=%s idx=%d", partName, idx);
            int id = partDb.getPart(partName, idx);
            Drawable drawable = context.getResources().getDrawable(id);
            drawable.setBounds(0, 0, size, size);
            drawable.draw(canvas);
        }
        return bitmap;
    }
}
