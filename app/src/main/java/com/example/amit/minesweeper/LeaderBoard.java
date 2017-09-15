package com.example.amit.minesweeper;

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


    public boolean isLeader(MainActivity.eDifficulty difficulty,int playerScore) {
        int difficultyIndex = difficulty.ordinal();
        if(currentNumOfLeaders[difficultyIndex] > 0)
        return playerScore >
                playerScores[difficultyIndex][currentNumOfLeaders[difficultyIndex] - 1].getScore();
        else
            return true;
    }

    public boolean addPlayer(MainActivity.eDifficulty difficulty, PlayerScore playerScore) {
        int difficultyIndex = difficulty.ordinal();
        int i = 0;
        while(i < currentNumOfLeaders[difficultyIndex] && playerScores[difficultyIndex][i].getScore() >= playerScore.getScore()) {
            i++;
        }
        if(i < currentNumOfLeaders[difficultyIndex]) {
            if(i == playerScores[difficultyIndex].length - 1)
                playerScores[difficultyIndex][i] = playerScore;
            else{
                playerScores[difficultyIndex][i + 1] = playerScores[difficultyIndex][i];
                playerScores[difficultyIndex][i] = playerScore;
            }
        }
        else
            return false;
        return true;
    }
}
