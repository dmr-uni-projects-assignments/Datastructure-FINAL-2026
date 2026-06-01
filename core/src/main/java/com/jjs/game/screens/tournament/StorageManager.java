package com.jjs.game.screens.tournament;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

public class StorageManager {

    private static final String SAVE_FILE = "tournament_players.json";

    // save manager data
    public static void save(TournamentManager manager) {

        Json json = new Json();

        ArrayList<PlayerRecord> players = manager.getPlayers();

        String data = json.prettyPrint(players);

        FileHandle file = Gdx.files.local(SAVE_FILE);
        file.writeString(data, false);
    }

    // load manager data
    @SuppressWarnings("unchecked")
    public static TournamentManager load() {

        TournamentManager manager = new TournamentManager();
        FileHandle file = Gdx.files.local(SAVE_FILE);

        // first launch / no save
        if (!file.exists()) {
            return manager;
        }

        try {

            Json json = new Json();

            ArrayList<PlayerRecord> players =
                    json.fromJson(ArrayList.class,
                            PlayerRecord.class,
                            file.readString());

            if (players != null) {
                manager.setPlayers(players);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return manager;
    }
}