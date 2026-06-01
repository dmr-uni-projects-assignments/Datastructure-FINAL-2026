package com.jjs.game.screens.gameover;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.jjs.game.Main;
import com.jjs.game.screens.gameplay.GameplayScreen;
import com.jjs.game.screens.title.TitleScreen;
import com.jjs.game.utils.Constants;

public class GameoverScreen implements Screen {

    private final Main game;

    private Stage stage;
    private Skin skin;
    private String label;

    public GameoverScreen(Main game, String label) {
        this.game = game;
        this.label = label;
    }

    @Override
    public void show() {

        stage = new Stage(new StretchViewport(Constants.WIDTH, Constants.HEIGHT));

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // ui elements
        Label title = new Label(label, skin);
        TextButton restartButton = new TextButton("Restart", skin);
        TextButton titleButton = new TextButton("Back To Title", skin);

        // restart gameplay
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameplayScreen(game));
            }
        });

        // return to title
        titleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });

        // layout
        table.add(title).padBottom(50).row();
        table.add(restartButton).width(220).height(50).padBottom(20).row();
        table.add(titleButton).width(220).height(50);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 1f);
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
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }

        if (skin != null) {
            skin.dispose();
        }
    }
}