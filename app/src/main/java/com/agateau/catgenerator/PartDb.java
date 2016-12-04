package com.agateau.catgenerator;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Holds all avatar parts
 */
public class PartDb {
    private Map<String, Vector<Integer>> mParts = new HashMap<>();

    public static final String[] PART_NAMES = {"body", "fur", "eyes", "mouth", "accessorie"};

    public int getPartCount(String partName) {
        return mParts.get(partName).size();
    }

    public int getPart(String partName, int idx) {
        return mParts.get(partName).get(idx);
    }

    public void init(Context context) {
        for (String partName: PART_NAMES) {
            listParts(context, partName);
        }
    }

    private void listParts(Context context, String partName) {
        Vector<Integer> ids = new Vector<>();
        for (int idx = 1;; ++idx) {
            String name = String.format("%s_%d", partName, idx);
            int id = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            if (id == 0) {
                break;
            }
            ids.add(id);
        }
        mParts.put(partName, ids);
    }
}
