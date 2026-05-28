package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Player extends Character {
    public Player() {
        super(new Texture("libgdx.png"), 0, 0);
    }

    @Override
    public void update(float dt) {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            y += speed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            y -= speed * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            x -= speed * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            x += speed * dt;
        }
    }
}