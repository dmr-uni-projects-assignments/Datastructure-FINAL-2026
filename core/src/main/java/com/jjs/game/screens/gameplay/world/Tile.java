package com.jjs.game.screens.gameplay.world;

import com.jjs.game.utils.*;

public class Tile {

    private boolean northWall;
    private boolean southWall;
    private boolean eastWall;
    private boolean westWall;

    public Tile() {
        northWall = false;
        southWall = false;
        eastWall = false;
        westWall = false;
    }

    public boolean hasWall(Constants.Direction dir) {
        switch (dir) {
            case NORTH:
                return northWall;
            case SOUTH:
                return southWall;
            case EAST:
                return eastWall;
            case WEST:
                return westWall;
        }

        return false;
    }

    public void setWall(Constants.Direction dir, boolean value) {

        switch (dir) {
            case NORTH:
                northWall = value;
                break;
            case SOUTH:
                southWall = value;
                break;
            case EAST:
                eastWall = value;
                break;
            case WEST:
                westWall = value;
                break;
        }
    }
}