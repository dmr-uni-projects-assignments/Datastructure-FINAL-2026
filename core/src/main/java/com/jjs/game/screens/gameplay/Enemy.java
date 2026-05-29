package com.jjs.game.screens.gameplay;

import java.util.*;
import com.badlogic.gdx.graphics.Texture;
import com.jjs.game.screens.gameplay.world.Tile;
import com.jjs.game.screens.gameplay.world.TileMap;
import com.jjs.game.utils.Constants;
import com.jjs.game.utils.Functions;

public class Enemy extends Character {
    private ArrayList<Character> entities;

    private float wanderTimer = 0f;
    private int wanderDir = -1;
    private float wanderChangeTimer = 0f;
    private Random random = new Random();

    public Enemy(float x, float y, TileMap world, ArrayList<Character> entities) {
        super(new Texture("enemy.png"), x, y, world);
        this.entities = entities;
    }

    // behold, pathfinding :tired:
    @Override
    public void update(float dt) {
        // wander mode handler
        if (wanderTimer > 0f) {
            // decrement timers
            wanderTimer -= dt;
            wanderChangeTimer -= dt;

            // pick direction once every ~0.2s OR if blocked
            if (wanderChangeTimer <= 0f) {
                wanderDir = random.nextInt(4);
                wanderChangeTimer = 0.2f;
            }

            boolean moved = false;

            // move in a semi-random direction
            switch (wanderDir) {
                case 0:
                    moved = move(true, speed * dt);
                    break;
                case 1:
                    moved = move(true, -speed * dt);
                    break;
                case 2:
                    moved = move(false, speed * dt);
                    break;
                case 3:
                    moved = move(false, -speed * dt);
                    break;
            }

            // if blocked, force immediate reroll next frame
            if (!moved) {
                wanderChangeTimer = 0f;
            }

            return;
        }

        // find next target to pathfind to
        Character target = findClosestTarget();
        if (target == null) {
            return;
        }

        // find next tile to move to
        int[] nextTile = getNextTileToward(target);
        if (nextTile == null) {
            return;
        }

        // someone already heading/standing there?
        if (tileOccupied(nextTile[0], nextTile[1])) {
            startWander();
            return;
        }

        // next pixel target to move toward [x,y]
        float[] nextPixel = Functions.tileToPixel(nextTile[0], nextTile[1]);
        float targetX = nextPixel[0] + 8f;
        float targetY = nextPixel[1] + 8f;

        // distances
        float dx = targetX - x;
        float dy = targetY - y;

        // do the mvoement
        if (Math.abs(dx) > 2f) {
            move(false, Math.signum(dx) * speed * dt);
        }
        if (Math.abs(dy) > 2f) {
            move(true, Math.signum(dy) * speed * dt);
        }
    }

    private boolean tileOccupied(int tx, int ty) {
        for (Character c : entities) {
            // ignore self and main player
            if (c == this) {
                continue;
            }
            if (!(c instanceof Enemy)) {
                continue;
            }

            // check if character is in the tile
            int[] tile = Functions.pixelToTile(c.x, c.y, true);
            if (tile[0] == tx && tile[1] == ty) {
                return true;
            }
        }

        return false;
    }

    private void startWander() {
        wanderTimer = 0.5f + random.nextFloat();
        wanderDir = random.nextInt(4);
    }

    private Character findClosestTarget() {
        Character closest = null;
        float bestDist = Float.MAX_VALUE;

        for (Character c : entities) {
            // integrity check
            if (c == this) {
                continue;
            }

            // basic math formula but skip the sqrt
            float dx = c.x - x;
            float dy = c.y - y;
            float dist = dx * dx + dy * dy;

            if (dist < bestDist) {
                bestDist = dist;
                closest = c;
            }
        }

        return closest;
    }

    // bfs search for next tile
    private int[] getNextTileToward(Character target) {
        int[] start = Functions.pixelToTile(x, y, true);
        int[] goal = Functions.pixelToTile(target.x, target.y, true);

        // aliases
        int startX = start[0];
        int startY = start[1];
        int goalX = goal[0];
        int goalY = goal[1];

        // already there
        if (startX == goalX && startY == goalY) {
            return null;
        }

        boolean[][] visited = new boolean[Constants.MAP_SIZE][Constants.MAP_SIZE];
        int[][] parentX = new int[Constants.MAP_SIZE][Constants.MAP_SIZE];
        int[][] parentY = new int[Constants.MAP_SIZE][Constants.MAP_SIZE];

        Queue<int[]> queue = new LinkedList<>();

        // start BFS here
        queue.add(new int[] { startX, startY });
        visited[startX][startY] = true;

        // aliases for cardinal directions
        int[][] dirs = {
                { 0, 1 },
                { 0, -1 },
                { 1, 0 },
                { -1, 0 }
        };

        // main bfs loop
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();

            int cx = cur[0];
            int cy = cur[1];

            // stop if goal reached
            if (cx == goalX && cy == goalY) {
                break;
            }

            Tile tile = world.getTile(cx, cy);

            // visit all cardinal directions
            for (int i = 0; i < 4; i++) {
                // visited tile coords
                int nx = cx + dirs[i][0];
                int ny = cy + dirs[i][1];

                // skip out of bounds and visited
                if (nx < 0 || ny < 0 || nx >= Constants.MAP_SIZE || ny >= Constants.MAP_SIZE) {
                    continue;
                }
                if (visited[nx][ny]) {
                    continue;
                }

                // check for wall
                boolean blocked = false;
                switch (i) {
                    case 0:
                        blocked = tile.hasWall(Constants.Direction.NORTH);
                        break;
                    case 1:
                        blocked = tile.hasWall(Constants.Direction.SOUTH);
                        break;
                    case 2:
                        blocked = tile.hasWall(Constants.Direction.EAST);
                        break;
                    case 3:
                        blocked = tile.hasWall(Constants.Direction.WEST);
                        break;
                }

                // cancel visit if theres a wall

                if (blocked) {
                    continue;
                }

                // mark visited and mark BFS'd path
                visited[nx][ny] = true;
                parentX[nx][ny] = cx;
                parentY[nx][ny] = cy;

                // redo BFS
                queue.add(new int[] { nx, ny });
            }
        }

        // no path found
        if (!visited[goalX][goalY]) {
            return null;
        }

        // reconstruct path
        // current tile (work backwards)
        int cx = goalX;
        int cy = goalY;

        // walk back from the previously logged parent paths
        while (!(parentX[cx][cy] == startX && parentY[cx][cy] == startY)) {

            int px = parentX[cx][cy];
            int py = parentY[cx][cy];

            // integrity check
            if (px == cx && py == cy) {
                return null;
            }

            // set for next iter
            cx = px;
            cy = py;
        }

        return new int[] { cx, cy };
    }
}