package com.jjs.game.screens.tournament;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentScreen implements Screen {

    private static final int PAD = 20;
    private static final int ROW_H = 32;
    private static final int HEADER_H = 60;
    private static final Color COL_BG = new Color(0.08f, 0.09f, 0.12f, 1f);
    private static final Color COL_HEADER = new Color(0.13f, 0.15f, 0.20f, 1f);
    private static final Color COL_ROW1 = new Color(0.11f, 0.13f, 0.17f, 1f);
    private static final Color COL_ROW2 = new Color(0.09f, 0.10f, 0.14f, 1f);
    private static final Color COL_ACCENT = new Color(0.20f, 0.70f, 0.45f, 1f);

    private final Game game;
    private final TournamentData tournament;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private BitmapFont fontSmall;
    private int scrollOffset = 0;
    private int maxScroll = 0;

    // Player class
    public static class PlayerData {
        public String name;
        public int kills;
        public int deaths;
        public int assists;
        
        public PlayerData(String name) {
            this.name = name;
            this.kills = 0;
            this.deaths = 0;
            this.assists = 0;
        }
        
        public float getKDA() {
            if (deaths == 0) return kills + assists;
            return (float)(kills + assists) / deaths;
        }
    }
    
    // Tournament class
    public static class TournamentData {
        public String name;
        public List<PlayerData> players;
        
        public TournamentData(String name) {
            this.name = name;
            this.players = new ArrayList<>();
        }
        
        public void addPlayer(PlayerData player) {
            players.add(player);
        }
        
        public PlayerData getTopPlayer() {
            if (players.isEmpty()) return null;
            PlayerData top = players.get(0);
            for (PlayerData p : players) {
                if (p.getKDA() > top.getKDA()) {
                    top = p;
                }
            }
            return top;
        }
    }

    public TournamentScreen(Game gdxGame, TournamentData tournament) {
        this.game = gdxGame;
        this.tournament = tournament;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        fontSmall = new BitmapFont();
        fontSmall.getData().setScale(0.8f);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                scrollOffset += amountY > 0 ? -ROW_H : ROW_H;
                scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset));
                return true;
            }
            
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    // Return to title screen
                    com.jjs.game.screens.title.TitleScreen titleScreen = new com.jjs.game.screens.title.TitleScreen((com.jjs.game.Main) game);
                    game.setScreen(titleScreen);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(COL_BG.r, COL_BG.g, COL_BG.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(COL_HEADER);
        shapeRenderer.rect(0, h - HEADER_H, w, HEADER_H);
        shapeRenderer.end();

        batch.begin();
        font.setColor(Color.WHITE);
        font.draw(batch, tournament.name + " - Tournament Tracker", PAD, h - HEADER_H + 40);
        fontSmall.setColor(Color.LIGHT_GRAY);
        fontSmall.draw(batch, "Press ESC to return to menu | Scroll to navigate", PAD, h - 15);
        batch.end();

        List<PlayerData> ranked = getSortedPlayers();
        maxScroll = Math.max(0, (ranked.size() * ROW_H) - (h - HEADER_H - 100));
        
        int startY = h - HEADER_H - ROW_H - 10;
        
        drawColumns(w, startY + ROW_H, null, true);
        
        for (int i = 0; i < ranked.size(); i++) {
            PlayerData p = ranked.get(i);
            float y = startY - (i * ROW_H) + scrollOffset;
            if (y > -ROW_H && y < h - HEADER_H) {
                drawColumns(w, y, p, false);
            }
        }

        PlayerData top = tournament.getTopPlayer();
        if (top != null) {
            batch.begin();
            fontSmall.setColor(COL_ACCENT);
            fontSmall.draw(batch, "Top Player: " + top.name + " (KDA: " + String.format("%.2f", top.getKDA()) + ")", PAD, 40);
            batch.end();
        }
    }

    private void drawColumns(int w, float y, PlayerData p, boolean header) {
        int col1 = PAD;
        int col2 = PAD + 80;
        int col3 = PAD + 220;
        int col4 = PAD + 340;
        int col5 = PAD + 440;
        int col6 = PAD + 540;

        if (header) {
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor(COL_HEADER);
            shapeRenderer.rect(0, y - ROW_H, w, ROW_H);
            shapeRenderer.end();
            
            batch.begin();
            font.setColor(COL_ACCENT);
            font.draw(batch, "RANK", col1, y - 10);
            font.draw(batch, "NAME", col2, y - 10);
            font.draw(batch, "KILLS", col3, y - 10);
            font.draw(batch, "DEATHS", col4, y - 10);
            font.draw(batch, "ASSISTS", col5, y - 10);
            font.draw(batch, "KDA", col6, y - 10);
            batch.end();
        } else if (p != null) {
            shapeRenderer.begin(ShapeType.Filled);
            shapeRenderer.setColor((int)(y / ROW_H) % 2 == 0 ? COL_ROW1 : COL_ROW2);
            shapeRenderer.rect(0, y - ROW_H, w, ROW_H);
            shapeRenderer.end();
            
            batch.begin();
            font.setColor(Color.WHITE);
            int rank = getSortedPlayers().indexOf(p) + 1;
            font.draw(batch, String.valueOf(rank), col1, y - 10);
            font.draw(batch, p.name, col2, y - 10);
            font.draw(batch, String.valueOf(p.kills), col3, y - 10);
            font.draw(batch, String.valueOf(p.deaths), col4, y - 10);
            font.draw(batch, String.valueOf(p.assists), col5, y - 10);
            font.setColor(COL_ACCENT);
            font.draw(batch, String.format("%.2f", p.getKDA()), col6, y - 10);
            batch.end();
        }
    }

    private List<PlayerData> getSortedPlayers() {
        List<PlayerData> sorted = new ArrayList<>(tournament.players);
        Collections.sort(sorted, (a, b) -> Float.compare(b.getKDA(), a.getKDA()));
        return sorted;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (font != null) font.dispose();
        if (fontSmall != null) fontSmall.dispose();
    }
}