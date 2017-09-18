package com.example.amit.minesweeper;


import android.location.Location;

public class PlayerScore {

    private String name;
    private int score;
    private int timeInSecond;



    private Location location;

    public PlayerScore(String name, int score, Location location, int timeInSecond) {
        this.name = name;
        this.score = score;
        this.location = location;
        this.timeInSecond = timeInSecond;
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

    public int getTimeInSecond() {
        return timeInSecond;
    }
}
