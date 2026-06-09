package com.jjs.game.screens.gameplay.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jjs.game.utils.*;

public class WorldRenderer {
    private ShapeRenderer shapeRenderer;
    private static final float LINE_THICKNESS = 2f;

    public WorldRenderer() {
        shapeRenderer = new ShapeRenderer();
    }

    public void render(TileMap map, OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // iterate through every tile for rendering
        for (int x = 0; x < Constants.MAP_SIZE; x++) {
            for (int y = 0; y < Constants.MAP_SIZE; y++) {

                Tile tile = map.getTile(x, y);

                if (tile == null) {
                    continue;
                }

                // bounds for rendering line
                float px = x * 64;
                float py = y * 64;

                // render red line if wall exists, otherwise gray grid
                // north
                if (tile.hasWall(Constants.Direction.NORTH)) {
                    shapeRenderer.setColor(Color.RED);
                } else {
                    shapeRenderer.setColor(Color.GRAY);
                }
                shapeRenderer.rect(px, py + 64 - LINE_THICKNESS / 2f, 64, LINE_THICKNESS);

                // south
                if (tile.hasWall(Constants.Direction.SOUTH)) {
                    shapeRenderer.setColor(Color.RED);
                } else {
                    shapeRenderer.setColor(Color.GRAY);
                }
                shapeRenderer.rect(px, py - LINE_THICKNESS / 2f, 64, LINE_THICKNESS);

                // east
                if (tile.hasWall(Constants.Direction.EAST)) {
                    shapeRenderer.setColor(Color.RED);
                } else {
                    shapeRenderer.setColor(Color.GRAY);
                }
                shapeRenderer.rect(px + 64 - LINE_THICKNESS / 2f, py, LINE_THICKNESS, 64);

                // west
                if (tile.hasWall(Constants.Direction.WEST)) {
                    shapeRenderer.setColor(Color.RED);
                } else {
                    shapeRenderer.setColor(Color.GRAY);
                }
                shapeRenderer.rect(px - LINE_THICKNESS / 2f, py, LINE_THICKNESS, 64);
            }
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}