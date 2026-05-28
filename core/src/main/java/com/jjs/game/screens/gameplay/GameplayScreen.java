package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.jjs.game.*;

public class GameplayScreen implements Screen {
    private Main game;

    private SpriteBatch batch;
    private Player player;
    
    
    public GameplayScreen(Main game) {
        this.game = game;
    }

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

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}