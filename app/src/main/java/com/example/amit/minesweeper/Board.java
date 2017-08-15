package com.example.amit.minesweeper;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;


public class Board extends GridLayout {

    private static Board instance = null;
    private Block[] blocks;
    private int numOfBlocks;

    public static Board getInstance(View view, int numOfBlocks) {
        if(instance == null) {
            instance = new Board(view.getContext(), numOfBlocks);
        }
        return instance;
    }

    private Board(Context context, int numOfBlocks) {
        super(context);
        this.numOfBlocks = numOfBlocks;
        blocks = new Block[numOfBlocks];

        for (int i = 0; i < numOfBlocks; i++) {
            
        }
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public int getNumOfBlocks() {
        return numOfBlocks;
    }
    
    public void rebuild(int numOfBlocks) {
        this.numOfBlocks = numOfBlocks;
        blocks = new Block[numOfBlocks];
    }
}
