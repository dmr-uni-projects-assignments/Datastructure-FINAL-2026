package com.jjs.game.screens.tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TournamentManagerArrayList {

    // backend datastore
    private ArrayList<PlayerRecord> players;

    public TournamentManagerArrayList() {
        players = new ArrayList<>();
    }

    // UI snapshot
    public ArrayList<PlayerRecord> getPlayers() {
        return new ArrayList<>(players);
    }

    public void setPlayers(ArrayList<PlayerRecord> playerList) {
        players.clear();
        players.addAll(playerList);
    }

    // add player by name
    public boolean addPlayer(String name) {

        if (findPlayer(name) != null) {
            return false;
        }

        players.add(new PlayerRecord(name));
        return true;
    }

    // overload for AddGameTab
    public void addPlayer(PlayerRecord player) {

        if (findPlayer(player.getName()) == null) {
            players.add(player);
        }
    }

    public boolean removePlayer(String name) {

        PlayerRecord player = findPlayer(name);

        if (player == null) {
            return false;
        }

        players.remove(player);
        return true;
    }

    // O(n) lookup
    public PlayerRecord findPlayer(String name) {

        String key = name.toLowerCase();

        for (PlayerRecord p : players) {
            if (p.name.toLowerCase().equals(key)) {
                return p;
            }
        }

        return null;
    }

    // alias for AddGameTab
    public PlayerRecord getPlayer(String name) {
        return findPlayer(name);
    }

    // search/filter for RecordsTab
    public ArrayList<PlayerRecord> searchPlayers(String query) {

        ArrayList<PlayerRecord> results = new ArrayList<>();

        if (query == null || query.isBlank()) {
            results.addAll(players);
            return results;
        }

        String q = query.toLowerCase();

        for (PlayerRecord p : players) {
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

        if (player == null) {
            player = new PlayerRecord(result.playerName);
            addPlayer(player);
        }

        player.recordGame(
                result.kills,
                result.isWin(),
                result.getPlacementPercent());
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