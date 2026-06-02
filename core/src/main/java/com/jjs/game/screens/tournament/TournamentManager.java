package com.jjs.game.screens.tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TournamentManager {

    // backend datastore
    private HashMap<String, PlayerRecord> players;

    public TournamentManager() {
        players = new HashMap<>();
    }

    // UI snapshot
    public ArrayList<PlayerRecord> getPlayers() {
        return new ArrayList<>(players.values());
    }

    public void setPlayers(ArrayList<PlayerRecord> playerList) {
        players.clear();

        for (int i = 0; i < playerList.size(); i++) {
            PlayerRecord p = playerList.get(i);
            players.put(p.name.toLowerCase(), p);
        }
    }

    // add player by name
    public boolean addPlayer(String name) {

        String key = name.toLowerCase();

        if (players.containsKey(key)) {
            return false;
        }

        players.put(key, new PlayerRecord(name));
        return true;
    }

    // overload for AddGameTab
    public void addPlayer(PlayerRecord player) {
        players.put(player.getName().toLowerCase(), player);
    }

    public boolean removePlayer(String name) {

        String key = name.toLowerCase();

        if (!players.containsKey(key)) {
            return false;
        }

        players.remove(key);
        return true;
    }

    // O(1) lookup
    public PlayerRecord findPlayer(String name) {
        return players.get(name.toLowerCase());
    }

    // alias for AddGameTab
    public PlayerRecord getPlayer(String name) {
        return findPlayer(name);
    }

    // search/filter for RecordsTab
    public ArrayList<PlayerRecord> searchPlayers(String query) {

        ArrayList<PlayerRecord> results = new ArrayList<>();

        if (query == null || query.isBlank()) {
            results.addAll(players.values());
            return results;
        }

        String q = query.toLowerCase();

        for (PlayerRecord p : players.values()) {
            if (p.name.toLowerCase().contains(q)) {
                results.add(p);
            }
        }

        return results;
    }

    // apply game result
    public boolean recordGame(GameResult result) {

        PlayerRecord player = findPlayer(result.playerName);

        if (player == null) {
            return false;
        }

        player.recordGame(
                result.kills,
                result.isWin(),
                result.getPlacementPercent());

        return true;
    }

    // AddGameTab-compatible wrapper
    public void addGameResult(GameResult result) {

        PlayerRecord player = findPlayer(result.playerName);

        // auto-create player if missing
        if (player == null) {
            player = new PlayerRecord(result.playerName);
            addPlayer(player);
        }

        player.recordGame(
                result.kills,
                result.isWin(),
                result.getPlacementPercent());
    }

    // save current datastore to disk
    public void save() {
        StorageManager.save(this);
    }

    public void clear() {

        if (players != null) {
            players.clear();
        }
    }

    // sorting returns UI snapshots

    public ArrayList<PlayerRecord> sortByKills() {
        ArrayList<PlayerRecord> list = getPlayers();

        Collections.sort(list,
                Comparator.comparingInt((PlayerRecord p) -> p.kills)
                        .reversed());

        return list;
    }

    public ArrayList<PlayerRecord> sortByWins() {
        ArrayList<PlayerRecord> list = getPlayers();

        Collections.sort(list,
                Comparator.comparingInt((PlayerRecord p) -> p.wins)
                        .reversed());

        return list;
    }

    public ArrayList<PlayerRecord> sortByGamesPlayed() {
        ArrayList<PlayerRecord> list = getPlayers();

        Collections.sort(list,
                Comparator.comparingInt((PlayerRecord p) -> p.gamesPlayed)
                        .reversed());

        return list;
    }

    public ArrayList<PlayerRecord> sortByPlacement() {
        ArrayList<PlayerRecord> list = getPlayers();

        Collections.sort(list,
                Comparator.comparingDouble((PlayerRecord p) -> p.averagePlacement)
                        .reversed());

        return list;
    }

    public ArrayList<PlayerRecord> sortByElo() {
        ArrayList<PlayerRecord> list = getPlayers();

        Collections.sort(list,
                Comparator.comparingInt((PlayerRecord p) -> p.getElo())
                        .reversed());

        return list;
    }
}