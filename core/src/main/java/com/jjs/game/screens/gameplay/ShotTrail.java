package com.jjs.game.screens.gameplay;

public class ShotTrail {
    public float x1;
    public float y1;
    public float x2;
    public float y2;

    public float life;

    public ShotTrail(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.life = 0.15f;
    }

    public boolean update(float dt) {
        life -= dt;
        return life <= 0f;
    }
}