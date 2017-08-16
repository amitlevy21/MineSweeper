package com.example.amit.minesweeper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;



public class Board  {

    public static final int MAX_NUM_OF_MINES_AROUND_BLOCK = 4;

    private Block[] blocks;
    private int totalNumOfBlocks;
    private int currentNumOfBlocks;
    private GridLayout gridLayout;
    private int numOfFlagsAllowed;


    public Board(Context context, GridLayout gridLayout, int boardSize, int buttonWidth) {
        this.totalNumOfBlocks = boardSize*boardSize;
        blocks = new Block[boardSize*boardSize];
        this.gridLayout = gridLayout;
        this.gridLayout.setRowCount(boardSize);
        this.gridLayout.setColumnCount(boardSize);

        for (int i = 0; i < totalNumOfBlocks; i++) {
            blocks[i] = new Block(context, buttonWidth);
            blocks[i].setLayoutParams(new ViewGroup.LayoutParams(buttonWidth, buttonWidth));
            addBlock(blocks[i]);
            blocks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }

    public boolean addBlock(Block block) {
        if(currentNumOfBlocks >= totalNumOfBlocks)
            return false;
        blocks[currentNumOfBlocks] = block;
        currentNumOfBlocks++;
        gridLayout.addView(block);
        return true;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public int getTotalNumOfBlocks() {
        return totalNumOfBlocks;
    }
    

}
