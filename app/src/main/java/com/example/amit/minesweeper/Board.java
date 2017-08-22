package com.example.amit.minesweeper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;



public class Board{


    private Block[][] blocks;
    private int totalNumOfBlocks;
    private GridLayout gridLayout;
    private int numOfFlags;
    private int numOfMines;
    private boolean minePressed;
    private int numOfPressedBlocks;


    public Board(Context context, GridLayout gridLayout, int boardSize, int buttonWidth, int numOfMines) {

        this.totalNumOfBlocks = boardSize*boardSize;
        blocks = new Block[boardSize][boardSize];
        this.gridLayout = gridLayout;
        this.gridLayout.setRowCount(boardSize);
        this.gridLayout.setColumnCount(boardSize);
        this.numOfMines = numOfMines;

        createBlocks(context, boardSize, buttonWidth);
        setMines();

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks[i].length; j++) {
                int mines = numOfMinesAround(i, j);
                blocks[i][j].setNumOfMinesAround(mines);
            }
        }
    }

    private void createBlocks(Context context,int boardSize, int buttonWidth) {

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                blocks[i][j] = new Block(context, i, j, buttonWidth);
                blocks[i][j].setLayoutParams(new ViewGroup.LayoutParams(buttonWidth, buttonWidth));
                blocks[i][j].setLongClickable(true);
                addBlock(blocks[i][j], i, j);
                blocks[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Block block = (Block) view;
                        if(block.getNumOfMinesAround() == 0) {
                            pressBlockAndNeighbours(block.getRow(), block.getCol());
                        }else {
                            block.press();
                            numOfPressedBlocks++;
                        }
                        //if(numOfPressedBlocks == totalNumOfBlocks - numOfMines) // players has won

                    }
                });
                blocks[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        Block block = (Block) view;
                        block.markFlag();
                        numOfFlags++;
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

    private void pressBlockAndNeighbours(int row, int col) {
        if (col < 0 || col >= blocks[0].length || row < 0 || row >= blocks.length) return;

        if (blocks[row][col].getNumOfMinesAround() == 0 && !blocks[row][col].getIsPressed()) {
            blocks[row][col].press();
            numOfPressedBlocks++;
            pressBlockAndNeighbours(row, col + 1);
            pressBlockAndNeighbours(row, col - 1);
            pressBlockAndNeighbours(row - 1, col);
            pressBlockAndNeighbours(row + 1, col);
        }
    }


}
