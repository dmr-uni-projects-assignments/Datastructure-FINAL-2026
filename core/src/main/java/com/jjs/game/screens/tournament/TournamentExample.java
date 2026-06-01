package com.jjs.game.screens.tournament;

/**
 * TournamentExample — shows how to use Tournament + TournamentScreen
 * in your main game class or wherever you set up a match.
 *
 * This is NOT a runnable class by itself — it's a reference snippet.
 * Copy the relevant parts into your actual game code.
 */
public class TournamentExample {

    public static void main(String[] args) {

        // ── 1. Create a tournament ────────────────────────────────────────
        Tournament t = new Tournament("Spring Showdown");

        // ── 2. Register players ───────────────────────────────────────────
        t.addPlayer("Alice");
        t.addPlayer("Bob");
        t.addPlayer("Charlie");
        t.addPlayer("Dana");

        // ── 3. Record events during a match ──────────────────────────────
        t.recordKill("Alice",   "Bob");               // Alice kills Bob
        t.recordKill("Alice",   "Charlie", "Dana");   // Alice kills Charlie, Dana assisted
        t.recordKill("Bob",     "Alice");              // Bob gets revenge
        t.recordKill("Charlie", "Dana");
        t.recordKill("Alice",   "Bob",     "Dana");

        // ── 4. Advance to the next round ──────────────────────────────────
        t.nextRound();

        t.recordKill("Bob",     "Charlie", "Alice");
        t.recordKill("Dana",    "Bob");
        t.recordKill("Alice",   "Dana");

        // ── 5. Query the leaderboard ──────────────────────────────────────
        System.out.println("=== Leaderboard by Score ===");
        for (Player p : t.getLeaderboardByScore()) {
            System.out.printf("  %-10s  K:%-3d  D:%-3d  A:%-3d  KDA:%.2f  Score:%.1f%n",
                    p.getName(), p.getKills(), p.getDeaths(),
                    p.getAssists(), p.getKDA(), p.getScore());
        }

        System.out.println("\n=== Leaderboard by KDA ===");
        for (Player p : t.getLeaderboardByKDA()) {
            System.out.printf("  %-10s  KDA:%.2f%n", p.getName(), p.getKDA());
        }

        System.out.println("\n=== Match Log ===");
        for (String entry : t.getMatchLog()) {
            System.out.println("  " + entry);
        }

        System.out.println("\nTop Player: " + t.getTopPlayer());

        /*
         * ── 6. Open TournamentScreen in libGDX ───────────────────────────
         *
         * Inside your Game subclass (e.g. Main.java or GameMain.java):
         *
         *   Tournament tournament = new Tournament("Spring Showdown");
         *   // ... add players ...
         *   setScreen(new TournamentScreen(this, tournament));
         *
         * TournamentScreen key bindings:
         *   [1]   Sort leaderboard by Score
         *   [2]   Sort leaderboard by Kills
         *   [3]   Sort leaderboard by KDA
         *   [ESC] Close tournament screen
         */
    }
}
