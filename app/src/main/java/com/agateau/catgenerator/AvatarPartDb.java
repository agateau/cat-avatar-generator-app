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

import android.text.TextUtils;
import android.util.JsonReader;

import com.agateau.utils.log.NLog;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Holds all avatar parts
 *
 * The json file looks like this:
 *
 * [
 *     ["$category", "$filename", $x, $y],
 *     ...
 * ]
 *
 * all files of a same category are grouped together
 *
 */
public class AvatarPartDb {
    private final Map<String, Vector<AvatarPart>> mParts = new HashMap<>();

    public static final String[] PART_NAMES = {"body", "fur", "eyes", "mouth", "accessorie"};

    public int getPartCount(String partName) {
        return mParts.get(partName).size();
    }

    public AvatarPart getPart(String partName, int idx) {
        return mParts.get(partName).get(idx);
    }

    public void init(InputStream jsonInputStream) {
        JsonReader reader;
        try {
            reader = new JsonReader(new InputStreamReader(jsonInputStream, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            NLog.e("Failed to open the json input stream: %s", e);
            throw new RuntimeException(e);
        }
        try {
            readParts(reader);
        } catch (IOException e) {
            NLog.e("Failed to read parts: %s", e);
            throw new RuntimeException(e);
        }
    }

    private void readParts(JsonReader reader) throws IOException {
        reader.beginArray();
        Vector<AvatarPart> parts = null;
        String currentCategory = "";
        while (reader.hasNext()) {
            reader.beginArray();
            String category = reader.nextString();
            if (!category.equals(currentCategory)) {
                parts = new Vector<>();
                mParts.put(category, parts);
                currentCategory = category;
            }
            AvatarPart part = null;
            String filename = reader.nextString();
            int x = reader.nextInt();
            int y = reader.nextInt();
            if (!TextUtils.isEmpty(filename)) {
                part = new AvatarPart(filename, x, y);
            }
            assert parts != null;
            parts.add(part);
            reader.endArray();
        }
        reader.endArray();
    }
}
