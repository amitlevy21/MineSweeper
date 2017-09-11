package com.example.amit.minesweeper;

public class LeaderBoard {
    private static final LeaderBoard ourInstance = new LeaderBoard();

    public static final int NUM_OF_LEADERS = 5;
    public static final int NUM_OF_DIFFICULTIES = 3;
    private PlayerScore[][] playerScores; //first array is the difficulty
                                            // sorted ascending by the scores


    public static LeaderBoard getInstance() {
        return ourInstance;
    }

    private LeaderBoard() {
        playerScores = new PlayerScore[NUM_OF_DIFFICULTIES][NUM_OF_LEADERS];

    }

    /** checks if player is a rightful leader, adds him to the leader board if so. */
    public boolean isLeader(MainActivity.eDifficulty difficulty,PlayerScore playerScore) {
        int difficultyIndex = difficulty.ordinal();
        if( playerScore.getScore() >
                playerScores[difficultyIndex][playerScores.length - 1].getScore()) {
            addPlayer(difficulty, playerScore);
            return true;
        }
        return false;
    }

    private boolean addPlayer(MainActivity.eDifficulty difficulty, PlayerScore playerScore) {
        int difficultyIndex = difficulty.ordinal();
        int i = 0;
        while(playerScores[difficultyIndex][i].getScore() >= playerScore.getScore()) {
            i++;
        }
        if(i == playerScores.length - 1) {
            playerScores[difficultyIndex][i] = playerScore;
        }
        else {
            playerScores[difficultyIndex][i + 1] = playerScores[difficultyIndex][i];
            playerScores[difficultyIndex][i] = playerScore;
        }
        return true;
    }
}
