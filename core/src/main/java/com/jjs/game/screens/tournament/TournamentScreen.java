package com.game.tournament;

import java.util.List;

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

/**
 * TournamentScreen — libGDX Screen that renders the tournament tracker.
 *
 * Displays:
 *  - Header bar with tournament name and round
 *  - Leaderboard table (Rank | Name | Kills | Deaths | Assists | KDA)
 *  - Match log (last 8 entries)
 *  - Summary stats (top player, total kills)
 *
 * Drop this into your core module under:
 *   core/src/main/java/com/game/tournament/TournamentScreen.java
 *
 * To open this screen from your main game class:
 *   game.setScreen(new TournamentScreen(game, myTournament));
 */
public class TournamentScreen implements Screen {

    // ── Layout constants ──────────────────────────────────────────────────
    private static final int PAD          = 20;
    private static final int ROW_H        = 32;
    private static final int HEADER_H     = 60;
    private static final int FONT_SMALL   = 14; // approximate; BitmapFont doesn't scale like this out of the box
    private static final Color COL_BG     = new Color(0.08f, 0.09f, 0.12f, 1f);
    private static final Color COL_HEADER = new Color(0.13f, 0.15f, 0.20f, 1f);
    private static final Color COL_ROW1   = new Color(0.11f, 0.13f, 0.17f, 1f);
    private static final Color COL_ROW2   = new Color(0.09f, 0.10f, 0.14f, 1f);
    private static final Color COL_ACCENT = new Color(0.20f, 0.70f, 0.45f, 1f); // green accent
    private static final Color COL_KILL   = new Color(0.90f, 0.35f, 0.35f, 1f); // red for kills
    private static final Color COL_DEATH  = new Color(0.55f, 0.55f, 0.65f, 1f); // muted for deaths
    private static final Color COL_ASSIST = new Color(0.35f, 0.65f, 0.90f, 1f); // blue for assists
    private static final Color COL_GOLD   = new Color(1.00f, 0.82f, 0.20f, 1f); // #1 rank highlight

    // ── libGDX objects ────────────────────────────────────────────────────
    private final com.badlogic.gdx.Game gdxGame;
    private final Tournament tournament;
    private SpriteBatch   batch;
    private ShapeRenderer shapes;
    private BitmapFont    font;
    private BitmapFont    fontSmall;

    // ── Sorting state ─────────────────────────────────────────────────────
    private enum SortMode { SCORE, KILLS, KDA }
    private SortMode sortMode = SortMode.SCORE;

    public TournamentScreen(com.badlogic.gdx.Game gdxGame, Tournament tournament) {
        this.gdxGame    = gdxGame;
        this.tournament = tournament;
    }

    @Override
    public void show() {
        batch     = new SpriteBatch();
        shapes    = new ShapeRenderer();
        font      = new BitmapFont();          // default libGDX font
        fontSmall = new BitmapFont();
        fontSmall.getData().setScale(0.85f);

        font.setColor(Color.WHITE);
        fontSmall.setColor(Color.WHITE);

        // ── Key bindings ──────────────────────────────────────────────────
        // Press 1/2/3 to change sort mode; ESC to go back
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int key) {
                if (key == Input.Keys.NUM_1) sortMode = SortMode.SCORE;
                if (key == Input.Keys.NUM_2) sortMode = SortMode.KILLS;
                if (key == Input.Keys.NUM_3) sortMode = SortMode.KDA;
                if (key == Input.Keys.ESCAPE) gdxGame.setScreen(null); // or your main menu screen
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        int W = Gdx.graphics.getWidth();
        int H = Gdx.graphics.getHeight();

        // ── Clear ─────────────────────────────────────────────────────────
        Gdx.gl.glClearColor(COL_BG.r, COL_BG.g, COL_BG.b, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapes.begin(ShapeType.Filled);

        // ── Header bar ────────────────────────────────────────────────────
        shapes.setColor(COL_HEADER);
        shapes.rect(0, H - HEADER_H, W, HEADER_H);

        // ── Accent line under header ──────────────────────────────────────
        shapes.setColor(COL_ACCENT);
        shapes.rect(0, H - HEADER_H - 3, W, 3);

        // ── Leaderboard rows ──────────────────────────────────────────────
        List<Player> ranked = getSortedPlayers();
        int tableTop = H - HEADER_H - 16 - ROW_H; // start below header

        // Column header row
        shapes.setColor(COL_HEADER);
        shapes.rect(PAD, tableTop, W - PAD * 2, ROW_H);

        // Player rows
        for (int i = 0; i < ranked.size(); i++) {
            float y = tableTop - (i + 1) * ROW_H;
            shapes.setColor(i % 2 == 0 ? COL_ROW1 : COL_ROW2);
            shapes.rect(PAD, y, W - PAD * 2, ROW_H);
        }

        // ── Log panel ─────────────────────────────────────────────────────
        int logTop = tableTop - (ranked.size() + 2) * ROW_H;
        shapes.setColor(COL_HEADER);
        shapes.rect(PAD, logTop - ROW_H * 8, W - PAD * 2, ROW_H * 9);

        shapes.end();

        // ── Text pass ─────────────────────────────────────────────────────
        batch.begin();

        // Header text
        font.setColor(Color.WHITE);
        font.draw(batch, "TOURNAMENT: " + tournament.getName().toUpperCase(),
                PAD + 8, H - 16);
        fontSmall.setColor(COL_ACCENT);
        fontSmall.draw(batch, "Round " + tournament.getCurrentRound()
                + "   |   Players: " + tournament.getPlayerCount()
                + "   |   [1] Score  [2] Kills  [3] KDA  (sort)",
                PAD + 8, H - 38);

        // Column headers
        float colY = tableTop + ROW_H - 8;
        font.setColor(COL_ACCENT);
        drawColumns(W, colY, null, true);

        // Player rows
        for (int i = 0; i < ranked.size(); i++) {
            Player p   = ranked.get(i);
            float  rowY = tableTop - i * ROW_H - 10;

            if (i == 0) font.setColor(COL_GOLD);
            else        font.setColor(Color.WHITE);

            drawColumns(W, rowY, p, false);
        }

        // Sort mode indicator
        fontSmall.setColor(COL_ACCENT);
        fontSmall.draw(batch, "Sorted by: " + sortMode.name(),
                PAD + 8, tableTop - ranked.size() * ROW_H - 8);

        // Log header
        font.setColor(COL_ACCENT);
        font.draw(batch, "Match Log", PAD + 8, logTop + 4);

        // Log entries (last 8)
        List<String> log = tournament.getMatchLog();
        int start = Math.max(0, log.size() - 8);
        fontSmall.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 8 && (start + i) < log.size(); i++) {
            fontSmall.draw(batch, log.get(start + i),
                    PAD + 10, logTop - 4 - i * (ROW_H - 8));
        }

        // Top player callout
        Player top = tournament.getTopPlayer();
        if (top != null) {
            font.setColor(COL_GOLD);
            font.draw(batch, "Top Player: " + top.getName()
                    + "  (" + top.getKills() + "K / "
                    + top.getDeaths() + "D / "
                    + top.getAssists() + "A)",
                    PAD + 8, logTop - ROW_H * 8 - 8);
        }

        batch.end();
    }

    /**
     * Draws the leaderboard columns.
     * If {@code header} is true, draws column labels; otherwise draws player data.
     * Column layout (approximate %): Rank 5%, Name 30%, Kills 13%, Deaths 13%, Assists 13%, KDA 13%, Score 13%
     */
    private void drawColumns(int W, float y, Player p, boolean header) {
        int usable = W - PAD * 2;
        float x0 = PAD + 8;

        float[] xPos = {
            x0,
            x0 + usable * 0.06f,
            x0 + usable * 0.36f,
            x0 + usable * 0.51f,
            x0 + usable * 0.64f,
            x0 + usable * 0.77f,
            x0 + usable * 0.89f
        };

        if (header) {
            String[] labels = {"#", "Name", "Kills", "Deaths", "Assists", "KDA", "Score"};
            for (int i = 0; i < labels.length; i++) {
                font.draw(batch, labels[i], xPos[i], y);
            }
        } else if (p != null) {
            int rank = getSortedPlayers().indexOf(p) + 1;

            // Rank
            font.draw(batch, String.valueOf(rank), xPos[0], y);
            // Name
            font.draw(batch, p.getName(), xPos[1], y);
            // Kills
            font.setColor(COL_KILL);
            font.draw(batch, String.valueOf(p.getKills()), xPos[2], y);
            // Deaths
            font.setColor(COL_DEATH);
            font.draw(batch, String.valueOf(p.getDeaths()), xPos[3], y);
            // Assists
            font.setColor(COL_ASSIST);
            font.draw(batch, String.valueOf(p.getAssists()), xPos[4], y);
            // KDA
            font.setColor(Color.WHITE);
            font.draw(batch, String.format("%.2f", p.getKDA()), xPos[5], y);
            // Score
            font.draw(batch, String.format("%.1f", p.getScore()), xPos[6], y);

            // Reset color for next row
            font.setColor(Color.WHITE);
        }
    }

    private List<Player> getSortedPlayers() {
        return switch (sortMode) {
            case KILLS -> tournament.getLeaderboardByKills();
            case KDA   -> tournament.getLeaderboardByKDA();
            default    -> tournament.getLeaderboardByScore();
        };
    }

    @Override public void resize(int width, int height) { batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height); }
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    @Override
    public void dispose() {
        batch.dispose();
        shapes.dispose();
        font.dispose();
        fontSmall.dispose();
    }
}
