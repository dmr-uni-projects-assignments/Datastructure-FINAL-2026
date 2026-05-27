package com.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    private Texture texture;

    private float x;
    private float y;

    private final float speed = 250f;

    public Player() {
        texture = new Texture("libgdx.png"); // TODO: replace texture

        x = 200;
        y = 200;
    }

    public void update(float dt) {

        if (Gdx.input.isKeyPressed(Input.Keys.W))
            y += speed * dt;

        if (Gdx.input.isKeyPressed(Input.Keys.S))
            y -= speed * dt;

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            x -= speed * dt;

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            x += speed * dt;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void dispose() {
        texture.dispose();
    }
}