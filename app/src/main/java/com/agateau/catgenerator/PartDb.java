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
public class PartDb {
    private Map<String, Vector<AvatarPart>> mParts = new HashMap<>();

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
