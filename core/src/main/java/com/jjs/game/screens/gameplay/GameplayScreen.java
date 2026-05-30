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
        world.setWall(5, 5, Constants.Direction.NORTH, true);
        world.setWall(5, 5, Constants.Direction.EAST, true);
        world.setWall(10, 10, Constants.Direction.SOUTH, true);

        // populate entity list
        entities = new ArrayList<>();
        player = new Player(0, 0, world, entities);
        entities.add(player);
        for (int i = 0; i < Constants.ENEMY_COUNT; i++) {

            float x = (float) (Math.random() * Constants.WIDTH);
            float y = (float) (Math.random() * Constants.HEIGHT);

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
        for (Character c : entities) {
            c.render(batch);
        }

        batch.end();
    }

    private void update(float delta) {
        updateMouse();
        for (Character c : entities) {
            c.update(delta);
        }

        camera.position.set(player.getCoords()[0], player.getCoords()[1], 0);
        camera.update();

        // TODO: remove debug
        // float[] temp = Functions.pixelToTile(player.getCoords()[0], player.getCoords()[1]);
        // System.out.println(String.format("%f, %f", temp[0], temp[1]));
    }

    @Override
    public void dispose() {
        batch.dispose();
        worldRenderer.dispose();
        for (Character c : entities) {
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