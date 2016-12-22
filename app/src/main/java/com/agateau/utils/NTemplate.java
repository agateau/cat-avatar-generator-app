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
