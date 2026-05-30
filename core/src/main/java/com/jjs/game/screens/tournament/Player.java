package com.game.tournament;

/**
 * Represents a player in the tournament with kill/death/assist tracking.
 */
public class Player {

    private String name;
    private int kills;
    private int deaths;
    private int assists;

    public Player(String name) {
        this.name = name;
        this.kills = 0;
        this.deaths = 0;
        this.assists = 0;
    }

    // --- Mutators ---

    public void addKill()   { kills++; }
    public void addDeath()  { deaths++; }
    public void addAssist() { assists++; }

    public void addKills(int k)   { kills   = Math.max(0, kills   + k); }
    public void addDeaths(int d)  { deaths  = Math.max(0, deaths  + d); }
    public void addAssists(int a) { assists = Math.max(0, assists + a); }

    public void resetStats() {
        kills = 0;
        deaths = 0;
        assists = 0;
    }

    // --- Computed stats ---

    /**
     * KDA ratio: (Kills + Assists) / max(Deaths, 1)
     * Deaths floored at 1 to avoid division by zero.
     */
    public float getKDA() {
        return (kills + assists) / (float) Math.max(deaths, 1);
    }

    /**
     * Total score using a common weighted formula: K*1 + A*0.5 - D*0.75
     */
    public float getScore() {
        return kills * 1.0f + assists * 0.5f - deaths * 0.75f;
    }

    // --- Getters ---

    public String getName()   { return name; }
    public int    getKills()  { return kills; }
    public int    getDeaths() { return deaths; }
    public int    getAssists(){ return assists; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return String.format("Player{name='%s', K=%d, D=%d, A=%d, KDA=%.2f}",
                name, kills, deaths, assists, getKDA());
    }
}
