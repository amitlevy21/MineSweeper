package com.example.amit.minesweeper;

import android.widget.GridLayout;



public class Board  {

    public static final int MAX_NUM_OF_MINES_AROUND_BLOCK = 4;

    private static Board instance = null;
    private Block[] blocks;
    private int totalNumOfBlocks;
    private int currentNumOfBlocks;
    private GridLayout gridLayout;

    public static Board getInstance( GridLayout gridLayout, int totalNumOfBlocks) {
        if(instance == null) {
            instance = new Board(gridLayout, totalNumOfBlocks);
        }
        return instance;
    }

    private Board(GridLayout gridLayout, int numOfBlocks) {
        this.totalNumOfBlocks = numOfBlocks;
        blocks = new Block[numOfBlocks];
        this.gridLayout = gridLayout;

    }

    public boolean addBlock(Block block) {
        if(currentNumOfBlocks >= gridLayout.getRowCount() * gridLayout.getColumnCount())
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
    
    public void rebuild(int numOfBlocks) {
        this.totalNumOfBlocks = numOfBlocks;
        blocks = new Block[numOfBlocks];
    }
}
