package com.jjs.game.utils;

public class Constants {
    public static final boolean DEBUG_MODE = true;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;

    public static final int ENEMY_COUNT = 9;
    public static final int MAP_SIZE = 32;

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    public static final float PX_LEFTOVER = 16f;
    public static final float TL_LEFTOVER = PX_LEFTOVER / 64f;
}
