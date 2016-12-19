package com.agateau.utils;

import android.text.Html;
import android.text.Spanned;
import android.text.SpannedString;

/**
 * Simple text template system. Replaces {keyword} with values.
 * Works with Spanned strings.
 */
public class NTemplate {
    private String mHtml;

    public NTemplate(CharSequence text) {
        mHtml = Html.toHtml(new SpannedString(text));
    }

    public NTemplate put(String key, String value) {
        mHtml = mHtml.replace("{" + key + "}", value);
        return this;
    }

    public Spanned toSpanned() {
        return Html.fromHtml(mHtml);
    }
}
