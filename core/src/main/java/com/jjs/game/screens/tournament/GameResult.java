package com.jjs.game.screens.tournament;

public class GameResult {

    public String playerName;
    public int kills;
    public int placement;
    public int totalPlayers;

    public GameResult() {
        // needed for JSON loading
    }

    public GameResult(String playerName, int kills, int placement, int totalPlayers) {
        this.playerName = playerName;
        this.kills = kills;
        this.placement = placement;
        this.totalPlayers = totalPlayers;
    }

    // winner = first place
    public boolean isWin() {
        return placement == 1;
    }

    // convert placement into %
    // 1st place = 100%
    // last place = 0%
    public float getPlacementPercent() {

        // safety
        if (totalPlayers <= 1) {
            return 100f;
        }

        // clamp bad data
        int p = Math.max(1, Math.min(placement, totalPlayers));

        return 100f * (totalPlayers - p) / (totalPlayers - 1);
    }
}