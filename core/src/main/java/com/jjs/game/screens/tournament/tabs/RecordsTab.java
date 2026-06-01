package com.jjs.game.screens.tournament.tabs;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.jjs.game.screens.tournament.PlayerRecord;
import com.jjs.game.screens.tournament.TournamentManager;

public class RecordsTab {

    private static final int ROWS_PER_PAGE = 10;

    private TournamentManager manager;
    private Skin skin;

    private int currentPage = 0;

    private TextField searchField;
    private SelectBox<String> sortBox;

    private Table root;
    private Table dataTable;

    public RecordsTab(TournamentManager manager, Skin skin) {
        this.manager = manager;
        this.skin = skin;
    }

    public Table build() {

        root = new Table();
        root.pad(10);

        // controls
        Table controls = new Table();

        searchField = new TextField("", skin);
        searchField.setMessageText("search player...");

        sortBox = new SelectBox<>(skin);
        sortBox.setItems(
                "Name",
                "Kills",
                "Wins",
                "Games Played",
                "Placement",
                "ELO");

        TextButton prevButton =
                new TextButton("< Prev", skin);

        TextButton nextButton =
                new TextButton("Next >", skin);

        controls.add(new Label("Search:", skin)).padRight(5);
        controls.add(searchField)
                .width(250)
                .padRight(20);

        controls.add(new Label("Sort:", skin))
                .padRight(5);

        controls.add(sortBox)
                .width(180);

        controls.add().expandX();

        controls.add(prevButton).padRight(10);
        controls.add(nextButton);

        root.add(controls)
                .expandX()
                .fillX()
                .row();

        // scrollable table
        dataTable = new Table();
        ScrollPane scrollPane =
                new ScrollPane(dataTable, skin);

        scrollPane.setFadeScrollBars(false);

        root.add(scrollPane)
                .expand()
                .fill()
                .padTop(10);

        // listeners
        searchField.setTextFieldListener((field, c) -> refresh());

        sortBox.addListener(event -> {
            refresh();
            return false;
        });

        prevButton.addListener(event -> {
            if (currentPage > 0) {
                currentPage--;
                refresh();
            }
            return false;
        });

        nextButton.addListener(event -> {
            currentPage++;
            refresh();
            return false;
        });

        refresh();

        return root;
    }

    private void refresh() {

        dataTable.clear();

        ArrayList<PlayerRecord> players =
                manager.searchPlayers(
                        searchField.getText());

        // sorting
        String sort = sortBox.getSelected();

        switch (sort) {

            case "Kills":
                players.sort((a, b) ->
                        Integer.compare(b.kills, a.kills));
                break;

            case "Wins":
                players.sort((a, b) ->
                        Integer.compare(b.wins, a.wins));
                break;

            case "Games Played":
                players.sort((a, b) ->
                        Integer.compare(
                                b.gamesPlayed,
                                a.gamesPlayed));
                break;

            case "Placement":
                players.sort((a, b) ->
                        Float.compare(
                                b.averagePlacement,
                                a.averagePlacement));
                break;

            case "ELO":
                players.sort((a, b) ->
                        Integer.compare(
                                b.getElo(),
                                a.getElo()));
                break;

            default:
                players.sort((a, b) ->
                        a.name.compareToIgnoreCase(b.name));
                break;
        }

        // headers
        addHeader("Player");
        addHeader("Kills");
        addHeader("Games");
        addHeader("Wins");
        addHeader("Win %");
        addHeader("Avg Place");
        addHeader("ELO");

        dataTable.row();

        int start = currentPage * ROWS_PER_PAGE;
        int end =
                Math.min(
                        start + ROWS_PER_PAGE,
                        players.size());

        // prevent empty overflow pages
        if (start >= players.size()) {
            currentPage = 0;
            start = 0;
            end =
                    Math.min(
                            ROWS_PER_PAGE,
                            players.size());
        }

        for (int i = start; i < end; i++) {

            PlayerRecord p = players.get(i);

            addCell(p.name);
            addCell("" + p.kills);
            addCell("" + p.gamesPlayed);
            addCell("" + p.wins);
            addCell(
                    String.format(
                            "%.1f%%",
                            p.getWinRate()));
            addCell(
                    String.format(
                            "%.1f%%",
                            p.getAveragePlacement()));
            addCell("" + p.getElo());

            dataTable.row();
        }
    }

    private void addHeader(String text) {

        dataTable.add(
                new Label(text, skin))
                .pad(8)
                .minWidth(120);
    }

    private void addCell(String text) {

        dataTable.add(
                new Label(text, skin))
                .pad(6)
                .minWidth(120);
    }
}