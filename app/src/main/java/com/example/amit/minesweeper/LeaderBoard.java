package com.example.amit.minesweeper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LeaderBoard {
    private static final LeaderBoard ourInstance = new LeaderBoard();


    public static final int NUM_OF_LEADERS = 5;
    public static final int NUM_OF_DIFFICULTIES = 3;
    private PlayerScore[][] playerScores; //first array is the difficulty
    // sorted ascending by the scores
    private int[] currentNumOfLeaders;


    public static LeaderBoard getInstance() {
        return ourInstance;
    }

    private LeaderBoard() {
        playerScores = new PlayerScore[NUM_OF_DIFFICULTIES][NUM_OF_LEADERS];
        currentNumOfLeaders = new int[NUM_OF_DIFFICULTIES];
    }


    public boolean isLeader(MainActivity.eDifficulty difficulty, int playerTime) {
        int difficultyIndex = difficulty.ordinal();
        if (currentNumOfLeaders[difficultyIndex] > 0)
            return playerTime <
                    playerScores[difficultyIndex][currentNumOfLeaders[difficultyIndex] - 1].getTimeInSecond();
        else
            return true;
    }

    public boolean addPlayer(MainActivity.eDifficulty difficulty, PlayerScore playerScore, boolean addToDataBase) {
        int difficultyIndex = difficulty.ordinal();
        int i = 0;
        while (i < currentNumOfLeaders[difficultyIndex] && playerScores[difficultyIndex][i].getTimeInSecond() <= playerScore.getTimeInSecond()) {
            i++;
        }
        if (i <= currentNumOfLeaders[difficultyIndex]) {
            if (i == playerScores[difficultyIndex].length - 1)
                playerScores[difficultyIndex][i] = playerScore;
            else {
                playerScores[difficultyIndex][i + 1] = playerScores[difficultyIndex][i];
                playerScores[difficultyIndex][i] = playerScore;
            }
            currentNumOfLeaders[difficultyIndex]++;
        } else
            return false;

        if (addToDataBase) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //get difficulty ref
            DatabaseReference myRef = database.getReference(difficulty.name().toLowerCase());
            if (myRef.child("" + (i + 1)) == null) { // entry  doesn't exist in firebase
                myRef.setValue("" + (i + 1));
            }

            DatabaseReference refChild = myRef.child("" + (i + 1));


            Map<String, String> leaderDetails = new HashMap<>();
            leaderDetails.put("name", playerScore.getName());
            leaderDetails.put("time", "" + playerScore.getTimeInSecond());
            leaderDetails.put("lat", "" + playerScore.getLocation().getLatitude());
            leaderDetails.put("lng", "" + playerScore.getLocation().getLongitude());

            refChild.setValue(leaderDetails);
        }
        return true;
    }

}
