package com.example.amit.minesweeper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


public class Board  {

    public static final int MAX_NUM_OF_MINES_AROUND_BLOCK = 4;

    private Block[][] blocks;
    private int totalNumOfBlocks;
    private int currentNumOfBlocks;
    private GridLayout gridLayout;
    private int numOfFlags;
    private int numOfMines;
    private boolean minePressed;


    public Board(Context context, GridLayout gridLayout, int boardSize, int buttonWidth, int numOfMines) {
        this.totalNumOfBlocks = boardSize*boardSize;
        blocks = new Block[boardSize][boardSize];
        this.gridLayout = gridLayout;
        this.gridLayout.setRowCount(boardSize);
        this.gridLayout.setColumnCount(boardSize);
        this.numOfMines = numOfMines;

        createBlocks(context, boardSize, buttonWidth);
        setMines(pickRandom(numOfMines,boardSize), pickRandom(numOfMines,boardSize));
    }

    private void createBlocks(Context context,int boardSize, int buttonWidth) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                blocks[i][j] = new Block(context, buttonWidth, i, j);
                blocks[i][j].setLayoutParams(new ViewGroup.LayoutParams(buttonWidth, buttonWidth));
                blocks[i][j].setLongClickable(true);
                addBlock(blocks[i][j], i, j);
                blocks[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Block block = (Block) view;
                        if(block.press())
                            minePressed = true;
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
        currentNumOfBlocks++;
        gridLayout.addView(block);
        return true;
    }

    private void setMines(Set<Integer> rows, Set<Integer> cols) {
        Iterator<Integer> itRow = rows.iterator();
        Iterator<Integer> itCol = cols.iterator();

        while(itRow.hasNext() && itCol.hasNext()) {
            blocks[itRow.next()][itCol.next()].setHasMine(true);
        }
    }

    public Set<Integer> pickRandom(int numbers, int range) {
        final Random RANDOM = new Random();
        final Set<Integer> picked = new HashSet<>();
        while (picked.size() < numbers) {
            picked.add(RANDOM.nextInt(range + 1));
        }
        return picked;
    }

}
