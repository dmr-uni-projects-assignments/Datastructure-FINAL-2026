package com.jjs.game.utils;

public class Constants {
    public static final boolean DEBUG_MODE = true;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static final float WALL_DENSITY = 0.25f;

    // these are technically globals and not constants now but im not gonna change the code again
    public static int ENEMY_COUNT = 9;
    public static int MAP_SIZE = 32;
    public static float MISS_CHANCE = 0.4f;

    public static final float REGEN_INTERVAL = 0.2f;

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    public static final float PX_LEFTOVER = 16f;
    public static final float TL_LEFTOVER = PX_LEFTOVER / 64f;
}
