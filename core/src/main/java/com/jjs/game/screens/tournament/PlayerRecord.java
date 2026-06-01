package com.jjs.game.screens.tournament;

public class PlayerRecord {

    public String name;

    public int kills;
    public int gamesPlayed;
    public int wins;

    // running average placement %
    public float averagePlacement;
    public int placementGames;

    public PlayerRecord() {
        // required for JSON loading
    }

    public PlayerRecord(String name) {
        this.name = name;
        this.averagePlacement = 0f;
        this.placementGames = 0;
    }

    // add a game result to this player
    public void recordGame(
            int kills,
            boolean won,
            float placementPercent) {

        this.kills += kills;
        this.gamesPlayed++;

        if (won) {
            this.wins++;
        }

        // running average update
        averagePlacement =
                (averagePlacement * placementGames
                        + placementPercent)
                        / (placementGames + 1);

        placementGames++;
    }

    public float getAveragePlacement() {
        return averagePlacement;
    }

    public float getWinRate() {

        if (gamesPlayed == 0) {
            return 0f;
        }

        return (wins * 100f) / gamesPlayed;
    }

    public String getName() {
        return name;
    }

    // derived ELO formula
    // replace this with whatever formula you want
    public int getElo() {

        // placeholder formula
        return 1000 + wins * 20 + kills;
    }

    @Override
    public String toString() {
        return name;
    }
}