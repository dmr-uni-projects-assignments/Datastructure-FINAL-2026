package com.jjs.game.screens.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.jjs.game.Main;
import com.jjs.game.screens.title.TitleScreen;
import com.jjs.game.utils.Constants;

public class PreGameplayScreen implements Screen {

    private Main game;

    private Stage stage;
    private Skin skin;

    public PreGameplayScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(Constants.WIDTH, Constants.HEIGHT));
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        table.pad(30);
        stage.addActor(table);

        // header
        Label title = new Label("Game Setup", skin);
        Label mapLabel = new Label("Map Size: " + Constants.MAP_SIZE, skin);

        // sliders
        Slider mapSlider = new Slider(8, 256, 1, false, skin);
        mapSlider.setValue(Constants.MAP_SIZE);

        int currentPlayers = Constants.ENEMY_COUNT + 1;
        Label playerLabel = new Label("Player Count: " + currentPlayers, skin);
        Slider playerSlider = new Slider(2, 100, 1, false, skin);
        playerSlider.setValue(currentPlayers);

        int currentAccuracy = (int) ((1f - Constants.MISS_CHANCE) * 100);
        Label accuracyLabel = new Label("Enemy Accuracy: " + currentAccuracy + "%", skin);
        Slider accuracySlider = new Slider(0f, 1f, 0.01f, false, skin);
        accuracySlider.setValue(1f - Constants.MISS_CHANCE);

        // buttons
        TextButton startButton = new TextButton("Start Game", skin);
        TextButton backButton = new TextButton("Back", skin);

        mapSlider.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    mapLabel.setText("Map Size: " + (int) mapSlider.getValue());
                }
            });
        playerSlider.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    playerLabel.setText("Player Count: " + (int) playerSlider.getValue());
                }
            });
        accuracySlider.addListener(
            new ChangeListener() {
                @Override
                public void changed( ChangeEvent event, Actor actor) {
                    accuracyLabel.setText("Enemy Accuracy: " + (int) (accuracySlider.getValue() * 100) + "%");
                }
            });

        startButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // set config options on start
                    Constants.MAP_SIZE = (int) mapSlider.getValue();
                    Constants.ENEMY_COUNT = (int) playerSlider.getValue() - 1;
                    Constants.MISS_CHANCE = 1f - accuracySlider.getValue();

                    game.setScreen(new GameplayScreen(game));
                }
            });
        backButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.setScreen(new TitleScreen(game));
                }
            });

        // layout
        table.add(title)
                .padBottom(40)
                .row();

        table.add(mapLabel)
                .left()
                .padBottom(5)
                .row();

        table.add(mapSlider)
                .width(350)
                .padBottom(20)
                .row();

        table.add(playerLabel)
                .left()
                .padBottom(5)
                .row();

        table.add(playerSlider)
                .width(350)
                .padBottom(20)
                .row();

        table.add(accuracyLabel)
                .left()
                .padBottom(5)
                .row();

        table.add(accuracySlider)
                .width(350)
                .padBottom(30)
                .row();

        table.add(startButton)
                .width(220)
                .height(50)
                .padBottom(15)
                .row();

        table.add(backButton)
                .width(220)
                .height(50);
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
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