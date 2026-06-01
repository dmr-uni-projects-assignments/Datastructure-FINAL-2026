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
                TournamentScreen.TournamentData tournament = new TournamentScreen.TournamentData("Main Tournament");
                TournamentScreen.PlayerData player1 = new TournamentScreen.PlayerData("Player 1");
                TournamentScreen.PlayerData player2 = new TournamentScreen.PlayerData("Player 2");
                TournamentScreen.PlayerData player3 = new TournamentScreen.PlayerData("Player 3");
                TournamentScreen.PlayerData player4 = new TournamentScreen.PlayerData("Player 4");
                
                player1.kills = 25;
                player1.deaths = 10;
                player1.assists = 15;
                
                player2.kills = 18;
                player2.deaths = 12;
                player2.assists = 20;
                
                player3.kills = 12;
                player3.deaths = 15;
                player3.assists = 8;
                
                player4.kills = 8;
                player4.deaths = 20;
                player4.assists = 5;
                
                tournament.addPlayer(player1);
                tournament.addPlayer(player2);
                tournament.addPlayer(player3);
                tournament.addPlayer(player4);
                
                game.setScreen(new TournamentScreen(game, tournament));
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
    }
}