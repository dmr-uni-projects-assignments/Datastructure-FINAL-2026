package com.jjs.game.screens.gameplay.world;

import java.util.ArrayList;
import com.jjs.game.utils.*;

public class TileMapArrayList {
    private ArrayList<ArrayList<Tile>> tiles;
    private static final int MAP_SIZE = Constants.MAP_SIZE;

    public TileMapArrayList() {
        tiles = new ArrayList<>(MAP_SIZE);

        // populate world
        for (int x = 0; x < MAP_SIZE; x++) {

            ArrayList<Tile> column = new ArrayList<>(MAP_SIZE);

            for (int y = 0; y < MAP_SIZE; y++) {
                column.add(new Tile());
            }

            tiles.add(column);
        }

        // fill ALL walls for benchmark consistency
        for (int x = 0; x < MAP_SIZE; x++) {
            for (int y = 0; y < MAP_SIZE; y++) {

                setWall(x, y,
                        Constants.Direction.NORTH,
                        true);

                setWall(x, y,
                        Constants.Direction.SOUTH,
                        true);

                setWall(x, y,
                        Constants.Direction.EAST,
                        true);

                setWall(x, y,
                        Constants.Direction.WEST,
                        true);
            }
        }
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= MAP_SIZE || y >= MAP_SIZE) {
            return null;
        }

        return tiles.get(x).get(y);
    }

    public void setWall(int x, int y, Constants.Direction dir, boolean value) {
        Tile tile = getTile(x, y);
        if (tile == null) {
            return;
        }

        tile.setWall(dir, value);

        switch (dir) {
            case NORTH:
                Tile north = getTile(x, y + 1);
                if (north != null)
                    north.setWall(Constants.Direction.SOUTH, value);
                break;

            case SOUTH:
                Tile south = getTile(x, y - 1);
                if (south != null)
                    south.setWall(Constants.Direction.NORTH, value);
                break;

            case EAST:
                Tile east = getTile(x + 1, y);
                if (east != null)
                    east.setWall(Constants.Direction.WEST, value);
                break;

            case WEST:
                Tile west = getTile(x - 1, y);
                if (west != null)
                    west.setWall(Constants.Direction.EAST, value);
                break;
        }
    }
}