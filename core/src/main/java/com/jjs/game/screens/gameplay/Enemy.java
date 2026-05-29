package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.jjs.game.screens.gameplay.world.TileMap;

public class Enemy extends Character {
    public Enemy(float x, float y, TileMap world) {
        super(new Texture("enemy.png"), x, y , world);
    }

    @Override
    public void update(float dt) {
        // TODO: more logic
    }
}
