package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jjs.game.screens.gameplay.world.*;
import com.jjs.game.utils.*;

public abstract class Character {
    protected Texture texture;

    protected float x;
    protected float y;
    protected final float speed = 250f;

    protected int hp;

    protected TileMap world;

    public Character(Texture texture, float x, float y, TileMap world) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.hp = 100;
        this.world = world;
    }

    public float[] getCoords() {
        return new float[] { x, y };
    }

    protected void move(boolean isVertical, float amount) {

        float newX = x;
        float newY = y;

        if (isVertical) {
            newY += amount;
        } else {
            newX += amount;
        }

        float left = newX;
        float right = newX + 48;
        float bottom = newY;
        float top = newY + 48;

        // left
        if (!isVertical && amount < 0) {
            int currentTileX = (int) Math.floor(x / 64f);
            int newTileX = (int) Math.floor(left / 64f);

            int bottomTile = (int) Math.floor(bottom / 64f);
            int topTile = (int) Math.floor((top - 1) / 64f);

            if (newTileX < currentTileX) {
                for (int ty = bottomTile; ty <= topTile; ty++) {
                    Tile tile = world.getTile(currentTileX, ty);
                    if (tile != null &&
                            tile.hasWall(Constants.Direction.WEST)) {
                        return;
                    }
                }
            }
        }

        // right
        if (!isVertical && amount > 0) {
            int currentTileX = (int) Math.floor((x + 47) / 64f);
            int newTileX = (int) Math.floor(right / 64f);

            int bottomTile = (int) Math.floor(bottom / 64f);
            int topTile = (int) Math.floor((top - 1) / 64f);

            if (newTileX > currentTileX) {
                for (int ty = bottomTile; ty <= topTile; ty++) {
                    Tile tile = world.getTile(currentTileX, ty);
                    if (tile != null &&
                            tile.hasWall(Constants.Direction.EAST)) {
                        return;
                    }
                }
            }
        }

        // down
        if (isVertical && amount < 0) {
            int currentTileY = (int) Math.floor(y / 64f);
            int newTileY = (int) Math.floor(bottom / 64f);

            int leftTile = (int) Math.floor(left / 64f);
            int rightTile = (int) Math.floor((right - 1) / 64f);

            if (newTileY < currentTileY) {
                for (int tx = leftTile; tx <= rightTile; tx++) {
                    Tile tile = world.getTile(tx, currentTileY);
                    if (tile != null &&
                            tile.hasWall(Constants.Direction.SOUTH)) {
                        return;
                    }
                }
            }
        }

        // up
        if (isVertical && amount > 0) {
            int currentTileY = (int) Math.floor((y + 47) / 64f);
            int newTileY = (int) Math.floor(top / 64f);

            int leftTile = (int) Math.floor(left / 64f);
            int rightTile = (int) Math.floor((right - 1) / 64f);

            if (newTileY > currentTileY) {
                for (int tx = leftTile; tx <= rightTile; tx++) {
                    Tile tile = world.getTile(tx, currentTileY);
                    if (tile != null &&
                            tile.hasWall(Constants.Direction.NORTH)) {
                        return;
                    }
                }
            }
        }

        x = newX;
        y = newY;

        x = Math.clamp(x, 0f, (Constants.MAP_SIZE - 1) * 64 + Constants.PX_LEFTOVER);

        y = Math.clamp(y, 0f, (Constants.MAP_SIZE - 1) * 64 + Constants.PX_LEFTOVER);
    }

    public abstract void update(float dt);

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void dispose() {
        texture.dispose();
    }
}
