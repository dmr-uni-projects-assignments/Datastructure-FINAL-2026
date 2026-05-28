package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Character {
    protected Texture texture;

    protected float x;
    protected float y;
    protected final float speed = 250f;

    protected int hp;

    public Character(Texture texture, float x, float y) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.hp = 100;
    }

    public abstract void update(float dt);

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public void dispose() {
        texture.dispose();
    }
}
