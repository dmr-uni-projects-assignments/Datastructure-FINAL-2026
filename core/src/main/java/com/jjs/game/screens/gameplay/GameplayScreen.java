package com.jjs.game.screens.gameplay;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.jjs.game.utils.*;
import com.jjs.game.screens.gameplay.world.*;

public class GameplayScreen implements Screen {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private TileMap world;
    private WorldRenderer worldRenderer;

    // used for aiming later on
    private Vector3 mousePos = new Vector3();

    private Player player;
    private ArrayList<Character> entities;

    public GameplayScreen() {
    }

    private void updateMouse() {
        mousePos.set(
                Gdx.input.getX(),
                Gdx.input.getY(),
                0);

        viewport.unproject(mousePos);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();

        viewport = new StretchViewport(Constants.WIDTH, Constants.HEIGHT, camera);

        viewport.apply();
        batch = new SpriteBatch();

        world = new TileMap();
        worldRenderer = new WorldRenderer();
        // TODO: remove debug
        world.setWall(5, 5, Constants.Direction.NORTH, true);
        world.setWall(5, 5, Constants.Direction.EAST, true);
        world.setWall(10, 10, Constants.Direction.SOUTH, true);

        // populate entity list
        entities = new ArrayList<>();
        float x = (float) (Math.random() * Constants.MAP_SIZE * 64);
        float y = (float) (Math.random() * Constants.MAP_SIZE * 64);
        player = new Player(x, y, world, entities);
        entities.add(player);
        for (int i = 0; i < Constants.ENEMY_COUNT; i++) {

            x = (float) (Math.random() * Constants.MAP_SIZE * 64);
            y = (float) (Math.random() * Constants.MAP_SIZE * 64);

            entities.add(new Enemy(x, y, world, entities));
        }
    }

    @Override
    public void render(float delta) {

        update(delta);

        ScreenUtils.clear(0f, 0f, 0f, 1f);

        worldRenderer.render(world, camera);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // render players
        for (int i = 0; i < entities.size(); i++) {
            Character c = entities.get(i);
            c.render(batch);
        }

        batch.end();
    }

    private void update(float delta) {
        updateMouse();

        // left mouse click shooting
        if (Gdx.input.isButtonJustPressed(0)) {
            float[] p = player.getCoords();

            // mouse - player center
            float dx = mousePos.x - (p[0] + 24);
            float dy = mousePos.y - (p[1] + 24);

            float angle = (float) Math.atan2(dy, dx);

            player.shoot(angle, 10);
        }

        // right click wall building
        if (Gdx.input.isButtonJustPressed(1)) {
            float[] p = player.getCoords();

            // mouse - player center
            float dx = mousePos.x - (p[0] + 24);
            float dy = mousePos.y - (p[1] + 24);

            // player tile
            int[] tile = Functions.pixelToTile(p[0], p[1], true);
            Constants.Direction dir;

            // choose dominant axis
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    dir = Constants.Direction.EAST;
                } else {
                    dir = Constants.Direction.WEST;
                }

            } else {
                if (dy > 0) {
                    dir = Constants.Direction.NORTH;
                } else {
                    dir = Constants.Direction.SOUTH;
                }
            }

            // place wall
            world.setWall(tile[0], tile[1], dir, true);
        }

        for (int i = 0; i < entities.size(); i++) {
            Character c = entities.get(i);
            c.update(delta);
        }

        camera.position.set(player.getCoords()[0], player.getCoords()[1], 0);
        camera.update();

        // TODO: remove debug
        // float[] temp = Functions.pixelToTile(player.getCoords()[0],
        // player.getCoords()[1]);
        // System.out.println(String.format("%f, %f", temp[0], temp[1]));
    }

    @Override
    public void dispose() {
        batch.dispose();
        worldRenderer.dispose();
        for (int i = 0; i < entities.size(); i++) {
            Character c = entities.get(i);
            c.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}