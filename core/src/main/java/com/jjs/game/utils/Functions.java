package com.jjs.game.utils;

public class Functions {
    // coordinate converters
    public static float[] tileToPixel(float x, float y) {
        return new float[] { x * 64f, y * 64f };
    }

    public static float[] pixelToTile(float x, float y) {
        return new float[] { x / 64f, y / 64f };
    }

    public static int[] pixelToTile(float x, float y, boolean useCenter) {
        // center of player (player is 48px wide)
        if (useCenter) {
            x += 48f / 2f;
            y += 48f / 2f;
        }
        return new int[] { (int) (x / 64f), (int) (y / 64f) };
    }
}
