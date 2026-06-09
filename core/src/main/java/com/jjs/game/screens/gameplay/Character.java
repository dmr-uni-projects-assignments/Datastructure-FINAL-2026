package com.jjs.game.screens.gameplay;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.jjs.game.screens.gameplay.world.*;
import com.jjs.game.utils.*;

public abstract class Character {
    protected Texture texture;

    protected float x;
    protected float y;
    protected final float speed = 250f;
    private int hp;
    private float regenTimer = 0f;

    // need to get these from the main game obj
    protected TileMap world;
    protected ArrayList<Character> entities;
    protected ArrayList<ShotTrail> trails;

    public Character(Texture texture, float x, float y, TileMap world, ArrayList<Character> entities, ArrayList<ShotTrail> trails) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.hp = 100;
        this.world = world;
        this.entities = entities;
        this.trails = trails;
    }

    public float[] getCoords() {
        return new float[] { x, y };
    }

    protected boolean move(boolean isVertical, float amount) {
        float newX = x;
        float newY = y;

        if (isVertical) {
            newY += amount;
        } else {
            newX += amount;
        }

        // for collision checking
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

            // if tile bound crossed
            if (newTileX < currentTileX) {
                for (int ty = bottomTile; ty <= topTile; ty++) {
                    Tile tile = world.getTile(currentTileX, ty);
                    // dont move further if wall exists between boundary cross
                    if (tile != null && tile.hasWall(Constants.Direction.WEST)) {
                        return false;
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
                    if (tile != null && tile.hasWall(Constants.Direction.EAST)) {
                        return false;
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
                    if (tile != null && tile.hasWall(Constants.Direction.SOUTH)) {
                        return false;
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
                    if (tile != null && tile.hasWall(Constants.Direction.NORTH)) {
                        return false;
                    }
                }
            }
        }

        // only goes here if the movement didnt collide with wall (edge of world is
        // handled differently)
        x = newX;
        y = newY;
        x = Math.clamp(x, 0f, (Constants.MAP_SIZE - 1) * 64 + Constants.PX_LEFTOVER);
        y = Math.clamp(y, 0f, (Constants.MAP_SIZE - 1) * 64 + Constants.PX_LEFTOVER);
        return true;
    }

    // raycast algo for hitscan
    protected void shoot(float angle, int damage) {
        final float STEP = 4f; // resolution

        // ray start coords and angle mults (yay trig)
        float px = x + 24;
        float py = y + 24;
        float dx = (float) Math.cos(angle);
        float dy = (float) Math.sin(angle);

        // ray rendering
        float startX = px;
        float startY = py;

        int[] prevTile = Functions.pixelToTile(px, py, false);
        int prevTileX = prevTile[0];
        int prevTileY = prevTile[1];

        // keep casting until break
        while (true) {
            // move ray
            px += dx * STEP;
            py += dy * STEP;

            // current tile ray is in
            int[] tilecoords = Functions.pixelToTile(px, py, false);
            int tileX = tilecoords[0];
            int tileY = tilecoords[1];

            // ray oob
            if (tileX < 0 || tileY < 0 || tileX >= Constants.MAP_SIZE || tileY >= Constants.MAP_SIZE) {
                trails.add(new ShotTrail(startX, startY, px, py));
                return;
            }

            // check for walls during tile boundary crosses
            Tile tile = world.getTile(prevTileX, prevTileY);
            if (tile != null) {
                // east wall
                if (tileX > prevTileX && tile.hasWall(Constants.Direction.EAST)) {
                    world.setWall(prevTileX, prevTileY, Constants.Direction.EAST, false);
                    trails.add(new ShotTrail(startX, startY, px, py));
                    return;
                }
                // west wall
                if (tileX < prevTileX && tile.hasWall(Constants.Direction.WEST)) {
                    world.setWall(prevTileX, prevTileY, Constants.Direction.WEST, false);
                    trails.add(new ShotTrail(startX, startY, px, py));
                    return;
                }
                // north wall
                if (tileY > prevTileY && tile.hasWall(Constants.Direction.NORTH)) {
                    world.setWall(prevTileX, prevTileY, Constants.Direction.NORTH, false);
                    trails.add(new ShotTrail(startX, startY, px, py));
                    return;
                }
                // south wall
                if (tileY < prevTileY && tile.hasWall(Constants.Direction.SOUTH)) {
                    world.setWall(prevTileX, prevTileY, Constants.Direction.SOUTH, false);
                    trails.add(new ShotTrail(startX, startY, px, py));
                    return;
                }
            }

            // character hit
            for (int i = 0; i < entities.size(); i++) {
                Character c = entities.get(i);
                // ignore self
                if (c == this) {
                    continue;
                }
                // check if ray is within 48x48px hitbox
                if (px >= c.x && px <= c.x + 48 && py >= c.y && py <= c.y + 48) {
                    c.damage(damage);
                    trails.add(new ShotTrail(startX, startY, px, py));
                    return;
                }
            }

            // prepare for next iteration
            prevTileX = tileX;
            prevTileY = tileY;
        }
    }

    public abstract void update(float dt);

    public void damage(int damage) {
        this.hp -= damage;
        // remove self if dead
        if (this.hp <= 0) {
            entities.remove(this);
            dispose();
        }
    }

    public int getHp() {
        return this.hp;
    }

    public void regen(float dt) {
        // dead entities dont regen
        if (hp <= 0) {
            return;
        }

        regenTimer += dt;
        if (regenTimer >= Constants.REGEN_INTERVAL) {
            hp++;
            regenTimer = 0f;
        }
        hp = Math.clamp(hp, 0, 100);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void dispose() {
        texture.dispose();
    }
}
