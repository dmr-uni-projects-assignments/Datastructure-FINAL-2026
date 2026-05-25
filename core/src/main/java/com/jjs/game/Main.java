package com.jjs.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture playerTexture;

    // player attributes
    private float playerX;
    private float playerY;
    private float speed = 250f;

    @Override
    public void create() {
        batch = new SpriteBatch();

        playerTexture = new Texture("libgdx.png"); // TODO: change texture

        playerX = 200;
        playerY = 200;
    }

    @Override
    public void render() {
        // TODO: separate this into funcs
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            playerY += speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            playerY -= speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            playerX -= speed * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            playerX += speed * delta;
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        batch.begin();
        batch.draw(playerTexture, playerX, playerY);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
    }
}
