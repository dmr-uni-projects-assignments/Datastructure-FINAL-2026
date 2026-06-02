package com.jjs.game.screens.tournament;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.jjs.game.Main;
import com.jjs.game.screens.title.TitleScreen;
import com.jjs.game.utils.Constants;
import com.jjs.game.screens.tournament.tabs.*;

public class TournamentScreen implements Screen {

    private Main game;

    private Stage stage;
    private Skin skin;

    private TournamentManager manager;

    private Table rootTable;
    private Table tabContent;

    public TournamentScreen(Main game,
            TournamentManager manager) {

        this.game = game;
        this.manager = manager;
    }

    @Override
    public void show() {

        stage = new Stage(
                new StretchViewport(
                        Constants.WIDTH,
                        Constants.HEIGHT));

        Gdx.input.setInputProcessor(stage);

        skin = new Skin(
                Gdx.files.internal(
                        "ui/uiskin.json"));

        rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        // top bar
        Table topBar = new Table();

        TextButton recordsButton =
                new TextButton("Records", skin);

        TextButton addGameButton =
                new TextButton("Add Game", skin);

        TextButton predictorButton =
                new TextButton("Predictor", skin);

        TextButton backButton =
                new TextButton("Back", skin);

        topBar.add(recordsButton).pad(5);
        topBar.add(addGameButton).pad(5);
        topBar.add(predictorButton).pad(5);
        topBar.add().expandX();
        topBar.add(backButton).pad(5);

        tabContent = new Table();

        rootTable.top();

        rootTable.add(topBar)
                .expandX()
                .fillX()
                .row();

        rootTable.add(tabContent)
                .expand()
                .fill();

        // default
        showRecordsTab();

        // listeners

        recordsButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(
                            InputEvent event,
                            float x,
                            float y) {

                        showRecordsTab();
                    }
                });

        addGameButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(
                            InputEvent event,
                            float x,
                            float y) {

                        showAddGameTab();
                    }
                });

        predictorButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(
                            InputEvent event,
                            float x,
                            float y) {

                        showPredictorTab();
                    }
                });

        backButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(
                            InputEvent event,
                            float x,
                            float y) {

                        StorageManager.save(manager);

                        game.setScreen(
                                new TitleScreen(game));
                    }
                });
    }

    private void showRecordsTab() {

        tabContent.clear();

        RecordsTab tab =
                new RecordsTab(
                        manager,
                        skin);

        tabContent.add(tab.build())
                .expand()
                .fill();
    }

    private void showAddGameTab() {

        tabContent.clear();

        AddGameTab tab =
                new AddGameTab(
                        manager,
                        skin);

        tabContent.add(tab.build())
                .expand()
                .fill();
    }

    private void showPredictorTab() {

        tabContent.clear();

        PredictorTab tab =
                new PredictorTab(
                        manager,
                        skin);

        tabContent.add(tab.build())
                .expand()
                .fill();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(
            int width,
            int height) {

        stage.getViewport()
                .update(
                        width,
                        height,
                        true);
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
        if (manager != null) {
            StorageManager.save(manager);
            manager.getPlayers().clear();
            manager.clear();
            manager = null;
        }

        if (stage != null) {
            stage.dispose();
        }

        if (skin != null) {
            skin.dispose();
        }
    }
}