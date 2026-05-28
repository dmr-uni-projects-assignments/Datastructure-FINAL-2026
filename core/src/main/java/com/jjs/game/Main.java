package com.jjs.game;

import com.badlogic.gdx.Game;
import com.jjs.game.screens.title.TitleScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {

    @Override
    public void create() {
        setScreen(new TitleScreen(this));
    }
}
