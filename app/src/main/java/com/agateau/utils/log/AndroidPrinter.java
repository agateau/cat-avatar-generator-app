package com.agateau.utils.log;

import android.text.TextUtils;
import android.util.Log;

/**
 * A Printer which uses Android log facilities
 */
public class AndroidPrinter implements NLog.Printer {
    @Override
    public void print(int level, String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            message = "-";
        }
        if (level == NLog.DEBUG) {
            Log.d(tag, message);
        } else if (level == NLog.INFO) {
            Log.i(tag, message);
        } else {
            Log.e(tag, message);
        }
    }
}
