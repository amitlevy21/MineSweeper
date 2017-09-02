package com.example.amit.minesweeper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("WeakerAccess") //Android studio keeps saying we should make this class and all its methods in private..
public class Board {

    interface BoardListener {

        void onUpdate(int numOfPressedBlocks, int numOfFlags, eState state);
    }

    public void setBoardListener(BoardListener boardListener) {
        this.boardListener = boardListener;
    }

    public enum eState {
        IN_PROGRESS, WIN, LOSE
    }

    private BoardListener boardListener;
    private Block[][] blocks;
    private int totalNumOfBlocks;
    private GridLayout gridLayout;
    private int numOfFlags;
    private int numOfMines;
    private int numOfPressedBlocks;
    private int numOfGoodFlags;
    private eState state = eState.IN_PROGRESS;


    public Board(Context context, GridLayout gridLayout, int boardSize, int buttonWidth, int numOfMines,
                 MainActivity.eDifficulty eDifficulty) {

        this.totalNumOfBlocks = boardSize * boardSize;
        blocks = new Block[boardSize][boardSize];
        this.gridLayout = gridLayout;
        this.gridLayout.setRowCount(boardSize);
        this.gridLayout.setColumnCount(boardSize);
        this.numOfMines = numOfMines;

        createBlocks(context, boardSize, buttonWidth);
        setMines();
    }

    private void createBlocks(Context context, int boardSize, int buttonSize) {

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                blocks[i][j] = new Block(context, i, j, buttonSize);
                blocks[i][j].setLayoutParams(new ViewGroup.LayoutParams(buttonSize, buttonSize));
                blocks[i][j].setLongClickable(true);
                addBlock(blocks[i][j], i, j);
                blocks[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Block block = (Block) view;
                        if (block.hasMine()) {
                            state = eState.LOSE;
                        }
                        else if(numOfPressedBlocks == totalNumOfBlocks - numOfMines) {
                            state = eState.WIN;
                        }

                        boardListener.onUpdate(numOfPressedBlocks, numOfFlags, state);
                        pressNeighbours(block.getRow(), block.getCol());

                    }
                });

                blocks[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Block block = (Block) view;
                        if(block.hasMine())
                            numOfGoodFlags++;
                        if(block.isFlagged())
                            numOfFlags++;
                        else
                            numOfFlags--;
                        block.markFlag();
                        boardListener.onUpdate(numOfPressedBlocks, numOfFlags, state);
                        return true;
                    }
                });
            }
        }
    }


    private boolean addBlock(Block block, int row, int col) {
        if(row * gridLayout.getColumnCount() + col >= totalNumOfBlocks)
            return false;
        blocks[row][col] = block;
        gridLayout.addView(block);
        return true;
    }

    /** Randomly spread mines */
    private void setMines() {
        final Random rnd = new Random();
        final List<Integer> randomNum = new ArrayList<>(blocks.length * blocks[0].length);

        for (int i = 0; i < blocks.length * blocks[0].length; i++) {
            randomNum.add(i);
        }

        Collections.shuffle(randomNum, rnd);
        List<Integer> rows = new ArrayList<>(randomNum);
        Collections.shuffle(randomNum,rnd);
        List<Integer> cols = new ArrayList<>(randomNum);

        for (int i = 0; i < numOfMines; i++) {
            blocks[rows.get(i) % (blocks.length - 1)][cols.get(i) % (blocks[0].length - 1)].setHasMine(true);
        }

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                int mines = numOfMinesAround(i, j);
                blocks[i][j].setNumOfMinesAround(mines);
            }
        }

    }

    private int numOfMinesAround(int row, int col){
        int mines = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                if (row + i < 0 || row + i >= blocks.length) continue;
                if (col + j < 0 || col + j >= blocks[0].length) continue;
                if (blocks[row+i][col+j].hasMine())
                    mines++;
            }
        }
        return mines;
    }


    private void pressNeighbours(int row, int col) {

        if( row >= 0 && col >= 0 && row < blocks.length && col < blocks[0].length && !blocks[row][col].getIsPressed()){
            blocks[row][col].press();
            numOfPressedBlocks++;

            if( blocks[row][col].getNumOfMinesAround() == 0 ){
                for( int xt = -1 ; xt <= 1 ; xt++ ){
                    for( int yt = -1 ; yt <= 1 ; yt++){
                        if( xt != yt ){
                            pressNeighbours(row + xt , col + yt);
                        }
                    }
                }
            }
        }
    }

    public int getNumOfFlags() {
        return numOfFlags;
    }

    public int getNumOfPressedBlocks() {
        return numOfPressedBlocks;
    }

    public int getNumOfGoodFlags() {
        return numOfGoodFlags;
    }

}
