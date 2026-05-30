package com.game.tournament;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;

/**
 * Manages the tournament: players, rounds, and leaderboard logic.
 */
public class Tournament {

    private String name;
    private List<Player> players;
    private List<String> matchLog;
    private int currentRound;

    public Tournament(String name) {
        this.name     = name;
        this.players  = new ArrayList<>();
        this.matchLog = new ArrayList<>();
        this.currentRound = 1;
    }

    // --- Player management ---

    public void addPlayer(String playerName) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(playerName)) return; // no duplicates
        }
        players.add(new Player(playerName));
    }

    public void removePlayer(String playerName) {
        players.removeIf(p -> p.getName().equalsIgnoreCase(playerName));
    }

    /** Returns the player with the given name, or null if not found. */
    public Player getPlayer(String playerName) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(playerName)) return p;
        }
        return null;
    }

    // --- Stat recording ---

    /**
     * Record a kill event: increments kills for the killer and deaths for the victim.
     * Optionally credits an assisting player.
     */
    public void recordKill(String killerName, String victimName, String assistName) {
        Player killer = getPlayer(killerName);
        Player victim = getPlayer(victimName);

        if (killer != null) killer.addKill();
        if (victim != null) victim.addDeath();

        if (assistName != null && !assistName.isEmpty()) {
            Player assist = getPlayer(assistName);
            if (assist != null) assist.addAssist();
        }

        String log = String.format("Round %d | Kill: %s → %s%s",
                currentRound, killerName, victimName,
                (assistName != null && !assistName.isEmpty()) ? " (assist: " + assistName + ")" : "");
        matchLog.add(log);
    }

    /** Convenience overload with no assist. */
    public void recordKill(String killerName, String victimName) {
        recordKill(killerName, victimName, null);
    }

    /**
     * Bulk-update a player's stats directly (useful for importing match results).
     */
    public void updateStats(String playerName, int kills, int deaths, int assists) {
        Player p = getPlayer(playerName);
        if (p != null) {
            p.addKills(kills);
            p.addDeaths(deaths);
            p.addAssists(assists);
            matchLog.add(String.format("Round %d | Stats updated for %s: +%dK +%dD +%dA",
                    currentRound, playerName, kills, deaths, assists));
        }
    }

    // --- Round management ---

    public void nextRound() {
        matchLog.add("--- Round " + currentRound + " ended ---");
        currentRound++;
        matchLog.add("--- Round " + currentRound + " started ---");
    }

    public void resetTournament() {
        for (Player p : players) p.resetStats();
        matchLog.clear();
        currentRound = 1;
        matchLog.add("Tournament reset.");
    }

    // --- Leaderboard queries ---

    /** Returns players sorted by KDA (highest first). */
    public List<Player> getLeaderboardByKDA() {
        List<Player> sorted = new ArrayList<>(players);
        sorted.sort((a, b) -> Float.compare(b.getKDA(), a.getKDA()));
        return Collections.unmodifiableList(sorted);
    }

    /** Returns players sorted by total score (highest first). */
    public List<Player> getLeaderboardByScore() {
        List<Player> sorted = new ArrayList<>(players);
        sorted.sort((a, b) -> Float.compare(b.getScore(), a.getScore()));
        return Collections.unmodifiableList(sorted);
    }

    /** Returns players sorted by raw kill count (highest first). */
    public List<Player> getLeaderboardByKills() {
        List<Player> sorted = new ArrayList<>(players);
        sorted.sort((a, b) -> Integer.compare(b.getKills(), a.getKills()));
        return Collections.unmodifiableList(sorted);
    }

    // --- Getters ---

    public String       getName()        { return name; }
    public int          getCurrentRound(){ return currentRound; }
    public List<Player> getPlayers()     { return Collections.unmodifiableList(players); }
    public List<String> getMatchLog()    { return Collections.unmodifiableList(matchLog); }
    public int          getPlayerCount() { return players.size(); }

    /** Returns the top-ranked player by score, or null if no players exist. */
    public Player getTopPlayer() {
        return players.isEmpty() ? null : getLeaderboardByScore().get(0);
    }
}
