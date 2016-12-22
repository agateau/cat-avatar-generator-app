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
