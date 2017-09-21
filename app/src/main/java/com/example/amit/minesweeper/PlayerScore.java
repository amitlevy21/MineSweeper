package com.example.amit.minesweeper;


import android.location.Location;

public class PlayerScore {

    private String name;
    private int timeInSecond;



    private Location location;

    public PlayerScore(String name, Location location, int timeInSecond) {
        this.name = name;

        this.location = location;
        this.timeInSecond = timeInSecond;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public int getTimeInSecond() {
        return timeInSecond;
    }
}
