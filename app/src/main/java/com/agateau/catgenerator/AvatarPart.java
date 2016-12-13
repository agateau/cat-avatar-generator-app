package com.agateau.catgenerator;

/**
 * POD class describing an avatar part
 */
public class AvatarPart {
    public final String filename;
    public final int x;
    public final int y;

    public AvatarPart(String filename, int x, int y) {
        this.filename = filename;
        this.x = x;
        this.y = y;
    }
}
