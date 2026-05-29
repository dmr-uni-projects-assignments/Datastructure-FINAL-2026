package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.jjs.game.Main;
import com.jjs.game.utils.*;

public class GameplayScreen implements Screen {
    private final Main game;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;

    // used for aiming later on
    private Vector3 mousePos = new Vector3();

    private Player player;
    private Character[] entities;

    public GameplayScreen(Main game) {
        this.game = game;
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

        viewport = new StretchViewport(
                Constants.WIDTH,
                Constants.HEIGHT,
                camera);

        viewport.apply();
        camera.position.set(
                Constants.WIDTH / 2f,
                Constants.HEIGHT / 2f,
                0);
        camera.update();

        batch = new SpriteBatch();

        // spawn all
        player = new Player(0, 0);
        entities = new Character[] {
                player,
                new Enemy(600, 300),
                new Enemy(900, 500),
                new Enemy(400, 600)
        };
    }

    @Override
    public void render(float delta) {

        update(delta);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

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
    }

    @Override
    public void dispose() {
        batch.dispose();
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