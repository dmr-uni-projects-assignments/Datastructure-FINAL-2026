package com.jjs.game.screens.title;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.jjs.game.Main;
import com.jjs.game.screens.gameplay.GameplayScreen;
import com.jjs.game.screens.tournament.StorageManager;
import com.jjs.game.screens.tournament.TournamentManager;
import com.jjs.game.screens.tournament.TournamentScreen;
import com.jjs.game.utils.Constants;

public class TitleScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;

    public TitleScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.WIDTH, Constants.HEIGHT));
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label title = new Label("Untitled Building Shooter", skin);
        TextButton playButton = new TextButton("Play Game", skin);
        TextButton tournamentButton = new TextButton("Tournament Tracker", skin);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameplayScreen(game));
            }
        });

        tournamentButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                // load saved tournament data
                TournamentManager manager = StorageManager.load();

                // open tracker
                game.setScreen(new TournamentScreen(game, manager));
            }
        });
        table.add(title).padBottom(50).row();
        table.add(playButton).padBottom(20).width(200).height(50).row();
        table.add(tournamentButton).width(200).height(50);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(delta);
        stage.draw();
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
        dispose();
    }

    @Override
    public void dispose() {
        if (stage != null)
            stage.dispose();
        if (skin != null)
            skin.dispose();
    }
}