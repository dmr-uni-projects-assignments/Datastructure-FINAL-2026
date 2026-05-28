package com.jjs.game.screens.title;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.jjs.game.utils.Constants;

import com.jjs.game.*;
import com.jjs.game.screens.gameplay.GameplayScreen;
import com.jjs.game.screens.tournament.TournamentScreen;

public class TitleScreen implements Screen {
    private final Main game;

    private Stage stage;
    private Skin skin;

    public TitleScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {

        stage = new Stage(
                new StretchViewport(Constants.WIDTH, Constants.HEIGHT));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Untitled Building Shooter", skin);
        TextButton playButton = new TextButton("Play Game", skin);
        TextButton tournamentButton = new TextButton("Tournament Tracker", skin);

        playButton.addListener(e -> {
            if (playButton.isPressed()) {
                game.setScreen(new GameplayScreen(game));
            }
            return true;
        });
        tournamentButton.addListener(e -> {
            if (tournamentButton.isPressed()) {
                game.setScreen(new TournamentScreen(game));
            }
            return true;
        });

        table.add(title).padBottom(40);
        table.row();

        table.add(playButton)
                .width(250)
                .height(60)
                .pad(10);

        table.row();

        table.add(tournamentButton)
                .width(250)
                .height(60)
                .pad(10);
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
