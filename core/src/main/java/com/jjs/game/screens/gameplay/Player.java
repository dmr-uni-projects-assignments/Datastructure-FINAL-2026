package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

import com.jjs.game.screens.gameplay.world.*;

public class Player extends Character {
    public Player(float x, float y, TileMap world) {
        super(new Texture("player.png"), x, y, world);
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