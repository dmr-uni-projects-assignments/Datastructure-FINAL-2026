package com.jjs.game.screens.gameplay;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jjs.game.Main;
import com.jjs.game.screens.gameover.GameoverScreen;
import com.jjs.game.screens.gameplay.world.TileMap;
import com.jjs.game.screens.gameplay.world.WorldRenderer;
import com.jjs.game.utils.Constants;
import com.jjs.game.utils.Functions;

public class GameplayScreen implements Screen {
    private Main game;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Viewport viewport;
    private TileMap world;
    private WorldRenderer worldRenderer;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private OrthographicCamera uiCam;

    // used for aiming later on
    private Vector3 mousePos = new Vector3();

    private Player player;
    private ArrayList<Character> entities;
    private ArrayList<ShotTrail> trails;

    public GameplayScreen(Main game) {
        this.game = game;
    }

    private void updateMouse() {
        mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePos);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        // separate cam for ui elements so it doesnt follow the world/game cam
        uiCam = new OrthographicCamera(Constants.WIDTH, Constants.HEIGHT);
        uiCam.position.set(0, 0, 0);

        viewport = new StretchViewport(Constants.WIDTH, Constants.HEIGHT, camera);
        font = new BitmapFont();

        viewport.apply();
        batch = new SpriteBatch();

        world = new TileMap();
        worldRenderer = new WorldRenderer();
        shapeRenderer = new ShapeRenderer();
        trails = new ArrayList<>();

        // populate entity list
        entities = new ArrayList<>();
        float x = (float) (Math.random() * Constants.MAP_SIZE * 64);
        float y = (float) (Math.random() * Constants.MAP_SIZE * 64);
        player = new Player(x, y, world, entities, trails);
        entities.add(player);
        for (int i = 0; i < Constants.ENEMY_COUNT; i++) {
            x = (float) (Math.random() * Constants.MAP_SIZE * 64);
            y = (float) (Math.random() * Constants.MAP_SIZE * 64);

            entities.add(new Enemy(x, y, world, entities, trails));
        }
    }

    @Override
    public void render(float delta) {
        update(delta);

        ScreenUtils.clear(0f, 0f, 0f, 1f);

        // render all
        worldRenderer.render(world, camera);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < trails.size(); i++) {
            ShotTrail t = trails.get(i);
            shapeRenderer.setColor(1f, 1f, 0f, t.life / 0.15f);
            shapeRenderer.rectLine(t.x1, t.y1, t.x2, t.y2, 3f);
        }
        shapeRenderer.end();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // render players
        for (int i = 0; i < entities.size(); i++) {
            Character c = entities.get(i);
            c.render(batch);
        }

        batch.end();

        // healthbar
        uiCam.update();

        shapeRenderer.setProjectionMatrix(uiCam.combined);

        float barWidth = 300f;
        float barHeight = 25f;
        float offset = 10f;
        float barX = -Constants.WIDTH / 2f + offset;
        float barY = Constants.HEIGHT / 2f - offset - barHeight;

        // hp percent
        float hpPercent = player.getHp() / 100f;

        // background + fill
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, barWidth * hpPercent, barHeight);
        shapeRenderer.end();

        // outline
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.end();

        // player count text
        batch.setProjectionMatrix(uiCam.combined);
        batch.begin();

        int remaining = entities.size();

        font.draw(batch, "Players Remaining: " + remaining, barX, barY - 10f);

        batch.end();
    }

    private void update(float delta) {
        updateMouse();

        // left mouse click shooting
        if (Gdx.input.isButtonJustPressed(0)) {
            float[] p = player.getCoords();

            // mouse - player center
            float dx = mousePos.x - (p[0] + 24);
            float dy = mousePos.y - (p[1] + 24);

            float angle = (float) Math.atan2(dy, dx);

            player.shoot(angle, 10);
        }

        // right click wall building
        if (Gdx.input.isButtonJustPressed(1)) {
            float[] p = player.getCoords();

            // mouse - player center
            float dx = mousePos.x - (p[0] + 24);
            float dy = mousePos.y - (p[1] + 24);

            // player tile
            int[] tile = Functions.pixelToTile(p[0], p[1], true);
            Constants.Direction dir;

            // choose dominant axis
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    dir = Constants.Direction.EAST;
                } else {
                    dir = Constants.Direction.WEST;
                }
            } else {
                if (dy > 0) {
                    dir = Constants.Direction.NORTH;
                } else {
                    dir = Constants.Direction.SOUTH;
                }
            }

            // place wall
            world.setWall(tile[0], tile[1], dir, true);
        }

        // handle trails
        for (int i = trails.size() - 1; i >= 0; i--) {
            // when trail life is over
            if (trails.get(i).update(delta)) {
                trails.remove(i);
            }
        }

        // handle characters
        for (int i = 0; i < entities.size(); i++) {
            Character c = entities.get(i);
            c.update(delta);
        }

        camera.position.set(player.getCoords()[0], player.getCoords()[1], 0);
        camera.update();

        // player death handling
        if (player.getHp() <= 0) {
            game.setScreen(new GameoverScreen(game, "Game Over! You placed #" + (entities.size() + 1)));
            return;
        }
        // win condition
        else if (entities.size() == 1 && entities.get(0) == player) {
            game.setScreen(new GameoverScreen(game, "You Win!"));
            return;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        worldRenderer.dispose();
        shapeRenderer.dispose();
        for (int i = 0; i < entities.size(); i++) {
            Character c = entities.get(i);
            c.dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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