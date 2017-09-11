package com.example.amit.minesweeper;


import android.location.Location;

public class PlayerScore {

    private String name;
    private int score;
    private Location location;

    public PlayerScore(String name, int score, Location location) {
        this.name = name;
        this.score = score;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public Location getLocation() {
        return location;
    }
}
