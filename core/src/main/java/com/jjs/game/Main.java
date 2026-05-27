package com.jjs.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.screens.gameplay.Player;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        player.update(delta);

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        batch.begin();
        player.render(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
    }
}
