package com.jjs.game.screens.gameplay.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.jjs.game.utils.Constants;

public class WorldRenderer {

    private ShapeRenderer shapeRenderer;

    public WorldRenderer() {
        shapeRenderer = new ShapeRenderer();
    }

    public void render(
            TileMap map,
            OrthographicCamera camera) {

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (int x = 0; x < Constants.MAP_SIZE; x++) {
            for (int y = 0; y < Constants.MAP_SIZE; y++) {

                Tile tile = map.getTile(x, y);

                if (tile == null)
                    continue;

                float px = x * 64;
                float py = y * 64;

                // north
                if (tile.hasWall(Constants.Direction.NORTH))
                    shapeRenderer.setColor(Color.RED);
                else
                    shapeRenderer.setColor(Color.GRAY);

                shapeRenderer.line(px, py + 64, px + 64, py + 64);

                // south
                if (tile.hasWall(Constants.Direction.SOUTH))
                    shapeRenderer.setColor(Color.RED);
                else
                    shapeRenderer.setColor(Color.GRAY);

                shapeRenderer.line(px, py, px + 64, py);

                // east
                if (tile.hasWall(Constants.Direction.EAST))
                    shapeRenderer.setColor(Color.RED);
                else
                    shapeRenderer.setColor(Color.GRAY);

                shapeRenderer.line(px + 64, py, px + 64, py + 64);

                // west
                if (tile.hasWall(Constants.Direction.WEST))
                    shapeRenderer.setColor(Color.RED);
                else
                    shapeRenderer.setColor(Color.GRAY);

                shapeRenderer.line(px, py, px, py + 64);
            }
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}