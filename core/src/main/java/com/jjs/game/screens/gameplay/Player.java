package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Player extends Character {
    public Player(float x, float y) {
        super(new Texture("player.png"), x, y);
    }

    @Override
    public void update(float dt) {

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            move(true, (speed * dt));
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            move(true, -(speed * dt));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            move(false, -(speed * dt));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            move(false, (speed * dt));
        }
    }
}