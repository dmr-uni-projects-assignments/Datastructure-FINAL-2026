package com.jjs.game.screens.title;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.jjs.game.Main;
import com.jjs.game.screens.gameplay.world.*;
import com.jjs.game.screens.tournament.*;
import com.jjs.game.utils.Constants;

public class DataStructureBenchmarkScreen implements Screen {

    private Main game;
    private Stage stage;
    private Skin skin;
    private Random random = new Random();

    private static final int RUNS = 20;

    public DataStructureBenchmarkScreen(Main game) {
        this.game = game;
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

        Table root = new Table();
        root.setFillParent(true);
        root.pad(20);
        stage.addActor(root);

        Label title = new Label(
                "Data Structure Benchmark",
                skin);

        TextButton tournamentButton = new TextButton(
                "Run Tournament Test",
                skin);

        TextButton tilemapButton = new TextButton(
                "Run TileMap Test",
                skin);

        TextButton backButton = new TextButton(
                "Back",
                skin);

        Label resultsLabel = new Label(
                "Press a benchmark button.",
                skin);

        resultsLabel.setWrap(true);

        ScrollPane scroll = new ScrollPane(
                resultsLabel,
                skin);

        tournamentButton.addListener(
                new ClickListener() {

                    @Override
                    public void clicked(
                            InputEvent event,
                            float x,
                            float y) {

                        resultsLabel.setText(
                                runTournamentBenchmark());
                    }
                });

        tilemapButton.addListener(
                new ClickListener() {

                    @Override
                    public void clicked(
                            InputEvent event,
                            float x,
                            float y) {

                        resultsLabel.setText(
                                runTilemapBenchmark());
                    }
                });

        backButton.addListener(
                new ClickListener() {

                    @Override
                    public void clicked(
                            InputEvent event,
                            float x,
                            float y) {

                        game.setScreen(
                                new TitleScreen(game));
                    }
                });

        root.add(title)
                .colspan(3)
                .padBottom(20)
                .row();

        root.add(tournamentButton)
                .width(220)
                .pad(5);

        root.add(tilemapButton)
                .width(220)
                .pad(5);

        root.add(backButton)
                .width(150)
                .pad(5)
                .row();

        root.add(scroll)
                .colspan(3)
                .expand()
                .fill()
                .padTop(20);
    }

    private String runTournamentBenchmark() {

        StringBuilder sb = new StringBuilder();

        int[] sizes = {
                100,
                1000,
                10000
        };

        Runtime runtime = Runtime.getRuntime();

        sb.append("=== Tournament Benchmark ===\n");
        sb.append("Average of ")
                .append(RUNS)
                .append(" runs\n\n");

        for (int size : sizes) {

            sb.append("Players: ")
                    .append(size)
                    .append("\n\n");

            double hashInsert = 0;
            double hashLookup = 0;
            double hashSort = 0;
            double hashMemory = 0;

            double treeInsert = 0;
            double treeLookup = 0;
            double treeSort = 0;
            double treeMemory = 0;

            double listInsert = 0;
            double listLookup = 0;
            double listSort = 0;
            double listMemory = 0;

            for (int run = 0; run < RUNS; run++) {

                long start;
                long before;
                long after;

                // HASHMAP

                System.gc();

                before = runtime.totalMemory()
                        - runtime.freeMemory();

                TournamentManager hash = new TournamentManager();

                start = System.nanoTime();

                for (int i = 0; i < size; i++) {
                    hash.addPlayer(
                            "Player" + i);
                }

                hashInsert += (System.nanoTime()
                        - start)
                        / 1_000_000.0;

                after = runtime.totalMemory()
                        - runtime.freeMemory();

                hashMemory += (after - before)
                        / 1024.0
                        / 1024.0;

                start = System.nanoTime();

                hash.findPlayer(
                        "Player"
                                + (size / 2));

                hashLookup += (System.nanoTime()
                        - start)
                        / 1_000_000.0;

                start = System.nanoTime();

                hash.sortByKills();

                hashSort += (System.nanoTime()
                        - start)
                        / 1_000_000.0;

                // TREEMAP

                System.gc();

                before = runtime.totalMemory()
                        - runtime.freeMemory();

                TournamentManagerTreeMap tree = new TournamentManagerTreeMap();

                start = System.nanoTime();

                for (int i = 0; i < size; i++) {
                    tree.addPlayer(
                            "Player" + i);
                }

                treeInsert += (System.nanoTime()
                        - start)
                        / 1_000_000.0;

                after = runtime.totalMemory()
                        - runtime.freeMemory();

                treeMemory += (after - before)
                        / 1024.0
                        / 1024.0;

                start = System.nanoTime();

                tree.findPlayer(
                        "Player"
                                + (size / 2));

                treeLookup += (System.nanoTime()
                        - start)
                        / 1_000_000.0;

                start = System.nanoTime();

                tree.sortByKills();

                treeSort += (System.nanoTime()
                        - start)
                        / 1_000_000.0;
                // ARRAYLIST

                System.gc();

                before = runtime.totalMemory()
                        - runtime.freeMemory();

                TournamentManagerArrayList list = new TournamentManagerArrayList();

                start = System.nanoTime();

                for (int i = 0; i < size; i++) {
                    list.addPlayer(
                            "Player" + i);
                }

                listInsert += (System.nanoTime()
                        - start)
                        / 1_000_000.0;

                after = runtime.totalMemory()
                        - runtime.freeMemory();

                listMemory += (after - before)
                        / 1024.0
                        / 1024.0;

                start = System.nanoTime();

                list.findPlayer(
                        "Player"
                                + (size / 2));

                listLookup += (System.nanoTime()
                        - start)
                        / 1_000_000.0;

                start = System.nanoTime();

                list.sortByKills();

                listSort += (System.nanoTime()
                        - start)
                        / 1_000_000.0;
            }

            sb.append("HashMap\n");
            sb.append("Insert: ")
                    .append(hashInsert / RUNS)
                    .append(" ms\n");
            sb.append("Lookup: ")
                    .append(hashLookup / RUNS)
                    .append(" ms\n");
            sb.append("Sort: ")
                    .append(hashSort / RUNS)
                    .append(" ms\n");
            sb.append("Memory: ")
                    .append(hashMemory / RUNS)
                    .append(" MB\n\n");

            sb.append("TreeMap\n");
            sb.append("Insert: ")
                    .append(treeInsert / RUNS)
                    .append(" ms\n");
            sb.append("Lookup: ")
                    .append(treeLookup / RUNS)
                    .append(" ms\n");
            sb.append("Sort: ")
                    .append(treeSort / RUNS)
                    .append(" ms\n");
            sb.append("Memory: ")
                    .append(treeMemory / RUNS)
                    .append(" MB\n\n");

            sb.append("ArrayList\n");
            sb.append("Insert: ")
                    .append(listInsert / RUNS)
                    .append(" ms\n");
            sb.append("Lookup: ")
                    .append(listLookup / RUNS)
                    .append(" ms\n");
            sb.append("Sort: ")
                    .append(listSort / RUNS)
                    .append(" ms\n");
            sb.append("Memory: ")
                    .append(listMemory / RUNS)
                    .append(" MB\n\n");
        }

        String retVal = sb.toString();
        System.out.println(retVal);
        return retVal;
    }

    private String runTilemapBenchmark() {

        StringBuilder sb = new StringBuilder();

        Runtime runtime = Runtime.getRuntime();

        sb.append(
                "=== TileMap Benchmark ===\n");

        sb.append(
                "Average of ")
                .append(RUNS)
                .append(" runs\n\n");

        double c1 = 0, l1 = 0, u1 = 0, b1 = 0, m1 = 0;
        double c2 = 0, l2 = 0, u2 = 0, b2 = 0, m2 = 0;
        double c3 = 0, l3 = 0, u3 = 0, b3 = 0, m3 = 0;

        for (int run = 0; run < RUNS; run++) {

            long start;
            long before;
            long after;

            // 2D ARRAY

            System.gc();

            before = runtime.totalMemory()
                    - runtime.freeMemory();

            start = System.nanoTime();

            TileMap map1 = new TileMap();

            c1 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            after = runtime.totalMemory()
                    - runtime.freeMemory();

            m1 += (after - before)
                    / 1024.0
                    / 1024.0;

            start = System.nanoTime();

            for (int i = 0; i < 100000; i++) {
                map1.getTile(
                        random.nextInt(
                                Constants.MAP_SIZE),
                        random.nextInt(
                                Constants.MAP_SIZE));
            }

            l1 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            start = System.nanoTime();

            for (int i = 0; i < 100000; i++) {
                map1.setWall(
                        random.nextInt(
                                Constants.MAP_SIZE),
                        random.nextInt(
                                Constants.MAP_SIZE),
                        Constants.Direction.NORTH,
                        random.nextBoolean());
            }

            u1 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            start = System.nanoTime();

            for (int i = 0; i < 100; i++) {
                bfsTileMap(
                        map1);
            }

            b1 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            // HASHMAP

            System.gc();

            before = runtime.totalMemory()
                    - runtime.freeMemory();

            start = System.nanoTime();

            TileMapHashMap map2 = new TileMapHashMap();

            c2 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            after = runtime.totalMemory()
                    - runtime.freeMemory();

            m2 += (after - before)
                    / 1024.0
                    / 1024.0;
            start = System.nanoTime();

            for (int i = 0; i < 100000; i++) {
                map2.getTile(
                        random.nextInt(
                                Constants.MAP_SIZE),
                        random.nextInt(
                                Constants.MAP_SIZE));
            }

            l2 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            start = System.nanoTime();

            for (int i = 0; i < 100000; i++) {
                map2.setWall(
                        random.nextInt(
                                Constants.MAP_SIZE),
                        random.nextInt(
                                Constants.MAP_SIZE),
                        Constants.Direction.NORTH,
                        random.nextBoolean());
            }

            u2 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            start = System.nanoTime();

            for (int i = 0; i < 100; i++) {
                bfsTileMap(
                        map2);
            }

            b2 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            // ARRAYLIST

            System.gc();

            before = runtime.totalMemory()
                    - runtime.freeMemory();

            start = System.nanoTime();

            TileMapArrayList map3 = new TileMapArrayList();

            c3 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            after = runtime.totalMemory()
                    - runtime.freeMemory();

            m3 += (after - before)
                    / 1024.0
                    / 1024.0;

            start = System.nanoTime();

            for (int i = 0; i < 100000; i++) {
                map3.getTile(
                        random.nextInt(
                                Constants.MAP_SIZE),
                        random.nextInt(
                                Constants.MAP_SIZE));
            }

            l3 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            start = System.nanoTime();

            for (int i = 0; i < 100000; i++) {
                map3.setWall(
                        random.nextInt(
                                Constants.MAP_SIZE),
                        random.nextInt(
                                Constants.MAP_SIZE),
                        Constants.Direction.NORTH,
                        random.nextBoolean());
            }

            u3 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;

            start = System.nanoTime();

            for (int i = 0; i < 100; i++) {
                bfsTileMap(
                        map3);
            }

            b3 += (System.nanoTime()
                    - start)
                    / 1_000_000.0;
        }

        sb.append("2D Array\n");
        sb.append("Construct: ")
                .append(c1 / RUNS)
                .append(" ms\n");
        sb.append("Lookup: ")
                .append(l1 / RUNS)
                .append(" ms\n");
        sb.append("Update: ")
                .append(u1 / RUNS)
                .append(" ms\n");
        sb.append("BFS: ")
                .append(b1 / RUNS)
                .append(" ms\n");
        sb.append("Memory: ")
                .append(m1 / RUNS)
                .append(" MB\n\n");

        sb.append("HashMap\n");
        sb.append("Construct: ")
                .append(c2 / RUNS)
                .append(" ms\n");
        sb.append("Lookup: ")
                .append(l2 / RUNS)
                .append(" ms\n");
        sb.append("Update: ")
                .append(u2 / RUNS)
                .append(" ms\n");
        sb.append("BFS: ")
                .append(b2 / RUNS)
                .append(" ms\n");
        sb.append("Memory: ")
                .append(m2 / RUNS)
                .append(" MB\n\n");

        sb.append("ArrayList\n");
        sb.append("Construct: ")
                .append(c3 / RUNS)
                .append(" ms\n");
        sb.append("Lookup: ")
                .append(l3 / RUNS)
                .append(" ms\n");
        sb.append("Update: ")
                .append(u3 / RUNS)
                .append(" ms\n");
        sb.append("BFS: ")
                .append(b3 / RUNS)
                .append(" ms\n");
        sb.append("Memory: ")
                .append(m3 / RUNS)
                .append(" MB\n\n");

        String retVal = sb.toString();
        System.out.println(retVal);
        return retVal;
    }

    private void bfsTileMap(
            TileMap map) {

        bfsInternal(
                (x, y) -> map.getTile(
                        x,
                        y));
    }

    private void bfsTileMap(
            TileMapHashMap map) {

        bfsInternal(
                (x, y) -> map.getTile(
                        x,
                        y));
    }

    private void bfsTileMap(
            TileMapArrayList map) {

        bfsInternal(
                (x, y) -> map.getTile(
                        x,
                        y));
    }

    private void bfsInternal(
            TileGetter getter) {

        int startX = random.nextInt(
                Constants.MAP_SIZE);

        int startY = random.nextInt(
                Constants.MAP_SIZE);

        int goalX = random.nextInt(
                Constants.MAP_SIZE);

        int goalY = random.nextInt(
                Constants.MAP_SIZE);

        boolean[][] visited = new boolean[Constants.MAP_SIZE][Constants.MAP_SIZE];

        Queue<int[]> queue = new LinkedList<>();

        queue.add(
                new int[] {
                        startX,
                        startY
                });

        visited[startX][startY] = true;

        int[][] dirs = {
                { 0, 1 },
                { 0, -1 },
                { 1, 0 },
                { -1, 0 }
        };

        while (!queue.isEmpty()) {

            int[] cur = queue.poll();

            int cx = cur[0];

            int cy = cur[1];

            if (cx == goalX
                    && cy == goalY) {
                return;
            }

            Tile tile = getter.getTile(
                    cx,
                    cy);

            for (int i = 0; i < 4; i++) {

                int nx = cx + dirs[i][0];

                int ny = cy + dirs[i][1];

                if (nx < 0
                        || ny < 0
                        || nx >= Constants.MAP_SIZE
                        || ny >= Constants.MAP_SIZE) {
                    continue;
                }

                if (visited[nx][ny]) {
                    continue;
                }

                boolean blocked = false;

                switch (i) {
                    case 0:
                        blocked = tile.hasWall(
                                Constants.Direction.NORTH);
                        break;
                    case 1:
                        blocked = tile.hasWall(
                                Constants.Direction.SOUTH);
                        break;
                    case 2:
                        blocked = tile.hasWall(
                                Constants.Direction.EAST);
                        break;
                    case 3:
                        blocked = tile.hasWall(
                                Constants.Direction.WEST);
                        break;
                }

                if (blocked) {
                    continue;
                }

                visited[nx][ny] = true;

                queue.add(
                        new int[] {
                                nx,
                                ny
                        });
            }
        }
    }

    private interface TileGetter {
        Tile getTile(
                int x,
                int y);
    }

    @Override
    public void render(
            float delta) {

        ScreenUtils.clear(
                0,
                0,
                0,
                1);

        stage.act(
                delta);

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

        if (stage != null)
            stage.dispose();

        if (skin != null)
            skin.dispose();
    }
}