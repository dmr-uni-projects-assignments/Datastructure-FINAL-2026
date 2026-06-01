package com.jjs.game.screens.tournament.tabs;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;

import com.jjs.game.screens.tournament.GameResult;
import com.jjs.game.screens.tournament.PlayerRecord;
import com.jjs.game.screens.tournament.StorageManager;
import com.jjs.game.screens.tournament.TournamentManager;

public class AddGameTab {

    private TournamentManager manager;
    private Skin skin;

    public AddGameTab(TournamentManager manager, Skin skin) {
        this.manager = manager;
        this.skin = skin;
    }

    // TournamentScreen expects build()
    public Table build() {

        Table root = new Table();
        root.pad(20);
        root.top().left();

        Label title = new Label("Add Game Result", skin);

        // searchable player picker
        Label playerLabel = new Label("Player:", skin);

        TextField searchField = new TextField("", skin);
        searchField.setMessageText("Search or type player...");

        SelectBox<String> playerBox = new SelectBox<>(skin);
        refreshPlayerBox(playerBox, "");

        TextButton newPlayerButton = new TextButton("+ New Player", skin);

        // stat fields
        TextField killsField = new TextField("", skin);
        killsField.setMessageText("Kills");

        TextField placementField = new TextField("", skin);
        placementField.setMessageText("Placement");

        TextField totalPlayersField = new TextField("", skin);
        totalPlayersField.setMessageText("Total Players");

        TextButton submitButton = new TextButton("Submit Result", skin);

        Label statusLabel = new Label("", skin);

        // live search
        searchField.setTextFieldListener((textField, c) -> {
            refreshPlayerBox(playerBox, searchField.getText());
        });

        // add new player
        newPlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                String name = searchField.getText().trim();

                if (name.isEmpty()) {
                    statusLabel.setText("Enter player name.");
                    return;
                }

                if (manager.findPlayer(name) != null) {
                    statusLabel.setText("Player already exists.");
                    return;
                }

                if (manager.addPlayer(name)) {

                    StorageManager.save(manager);

                    refreshPlayerBox(playerBox, "");
                    playerBox.setSelected(name);

                    statusLabel.setText("Player added.");
                }
            }
        });

        // submit result
        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                try {

                    String playerName = playerBox.getSelected();

                    if (playerName == null || playerName.isEmpty()) {
                        statusLabel.setText("Select player.");
                        return;
                    }

                    int kills =
                            Integer.parseInt(
                                    killsField.getText());

                    int placement =
                            Integer.parseInt(
                                    placementField.getText());

                    int totalPlayers =
                            Integer.parseInt(
                                    totalPlayersField.getText());

                    if (placement <= 0
                            || totalPlayers <= 0
                            || placement > totalPlayers) {

                        statusLabel.setText(
                                "Invalid placement.");
                        return;
                    }

                    GameResult result =
                            new GameResult(
                                    playerName,
                                    kills,
                                    placement,
                                    totalPlayers);

                    if (manager.recordGame(result)) {

                        StorageManager.save(manager);

                        killsField.setText("");
                        placementField.setText("");
                        totalPlayersField.setText("");

                        statusLabel.setText(
                                "Result recorded.");
                    } else {

                        statusLabel.setText(
                                "Failed to record.");
                    }

                } catch (NumberFormatException e) {

                    statusLabel.setText(
                            "Enter valid numbers.");
                }
            }
        });

        // layout
        root.add(title)
                .colspan(3)
                .padBottom(20)
                .left()
                .row();

        root.add(playerLabel).left().padRight(10);
        root.add(searchField).width(220);
        root.add(newPlayerButton).padLeft(10).row();

        root.add(new Label("Select:", skin)).left();
        root.add(playerBox)
                .width(220)
                .colspan(2)
                .left()
                .row();

        root.add(new Label("Kills:", skin)).left();
        root.add(killsField)
                .width(220)
                .colspan(2)
                .left()
                .row();

        root.add(new Label("Placement:", skin)).left();
        root.add(placementField)
                .width(220)
                .colspan(2)
                .left()
                .row();

        root.add(new Label("Total Players:", skin)).left();
        root.add(totalPlayersField)
                .width(220)
                .colspan(2)
                .left()
                .row();

        root.add(submitButton)
                .colspan(3)
                .padTop(20)
                .width(220)
                .left()
                .row();

        root.add(statusLabel)
                .colspan(3)
                .padTop(10)
                .left();

        return root;
    }

    private void refreshPlayerBox(
            SelectBox<String> playerBox,
            String filter) {

        Array<String> names = new Array<>();

        ArrayList<PlayerRecord> players =
                manager.getPlayers();

        for (PlayerRecord p : players) {

            String name = p.getName();

            if (filter == null
                    || filter.isBlank()
                    || name.toLowerCase()
                            .contains(
                                    filter.toLowerCase())) {

                names.add(name);
            }
        }

        playerBox.setItems(names);
    }
}