package com.jjs.game.utils;

public class Functions {
    public static float[] tileToPixel(float x, float y) {
        return new float[] {x * 64f, y * 64f};
    }

    public static float[] pixelToTile(float x, float y) {
        return new float[] {x / 64f, y / 64f};
    }
}
