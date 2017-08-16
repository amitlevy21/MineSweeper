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
    private int numOfFlags;


    public Board(Context context, GridLayout gridLayout, int boardSize, int buttonWidth) {
        this.totalNumOfBlocks = boardSize*boardSize;
        blocks = new Block[totalNumOfBlocks];
        this.gridLayout = gridLayout;
        this.gridLayout.setRowCount(boardSize);
        this.gridLayout.setColumnCount(boardSize);

        createBlocks(context, boardSize, buttonWidth);

    }

    private void createBlocks(Context context,int boardSize, int buttonWidth) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                blocks[i * boardSize + j] = new Block(context, buttonWidth, i, j);
                blocks[i * boardSize + j].setLayoutParams(new ViewGroup.LayoutParams(buttonWidth, buttonWidth));
                blocks[i * boardSize + j].setLongClickable(true);
                addBlock(blocks[i * boardSize + j]);
                blocks[i * boardSize + j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Block block = (Block) view;
                        block.press();
                    }
                });
                blocks[i * boardSize + j].setOnLongClickListener(new View.OnLongClickListener() {
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
