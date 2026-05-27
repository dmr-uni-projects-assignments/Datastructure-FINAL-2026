package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Gameplay implements Screen {

    private SpriteBatch batch;
    private Player player;

    @Override
    public void show() {

        batch = new SpriteBatch();
        player = new Player();
    }

    @Override
    public void render(float delta) {

        update(delta);

        ScreenUtils.clear(0.15f,0.15f,0.2f,1f);

        batch.begin();
        player.render(batch);
        batch.end();
    }

    private void update(float dt) {
        player.update(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
    }

    // Required Screen methods
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}