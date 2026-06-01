package com.jjs.game.screens.tournament.tabs;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jjs.game.screens.tournament.PlayerRecord;
import com.jjs.game.screens.tournament.TournamentManager;

public class PredictorTab {

    private TournamentManager manager;
    private Skin skin;

    public PredictorTab(
            TournamentManager manager,
            Skin skin) {

        this.manager = manager;
        this.skin = skin;
    }

    public Table build() {

        Table root = new Table();
        root.pad(20);

        Label title =
                new Label(
                        "Match Predictor",
                        skin);

        SelectBox<String> player1Box =
                new SelectBox<>(skin);

        SelectBox<String> player2Box =
                new SelectBox<>(skin);

        Label resultLabel =
                new Label(
                        "Select players",
                        skin);

        // populate dropdowns
        ArrayList<PlayerRecord> players =
                manager.getPlayers();

        String[] names =
                new String[players.size()];

        for (int i = 0; i < players.size(); i++) {
            names[i] =
                    players.get(i).name;
        }

        player1Box.setItems(names);
        player2Box.setItems(names);

        // auto-update prediction
        Runnable updatePrediction = () -> {

            String p1 =
                    player1Box.getSelected();

            String p2 =
                    player2Box.getSelected();

            if (p1 == null ||
                    p2 == null) {
                return;
            }

            PlayerRecord player1 =
                    manager.findPlayer(p1);

            PlayerRecord player2 =
                    manager.findPlayer(p2);

            if (player1 == null ||
                    player2 == null) {
                return;
            }

            int elo1 = player1.getElo();
            int elo2 = player2.getElo();

            int totalElo = elo1 + elo2;

            if (totalElo <= 0) {
                resultLabel.setText(
                        "Invalid ELO values.");
                return;
            }

            float p1Chance =
                    (float) elo1 / totalElo * 100f;

            float p2Chance =
                    (float) elo2 / totalElo * 100f;

            resultLabel.setText(
                    player1.name
                            + ": "
                            + String.format("%.1f", p1Chance)
                            + "%\n"
                            + player2.name
                            + ": "
                            + String.format("%.1f", p2Chance)
                            + "%");
        };

        player1Box.addListener(e -> {
            updatePrediction.run();
            return false;
        });

        player2Box.addListener(e -> {
            updatePrediction.run();
            return false;
        });

        // initial prediction
        updatePrediction.run();

        root.add(title)
                .colspan(2)
                .padBottom(20)
                .row();

        root.add(
                new Label(
                        "Player 1",
                        skin))
                .pad(10);

        root.add(player1Box)
                .width(250)
                .row();

        root.add(
                new Label(
                        "Player 2",
                        skin))
                .pad(10);

        root.add(player2Box)
                .width(250)
                .row();

        root.add(resultLabel)
                .colspan(2)
                .padTop(30);

        return root;
    }
}