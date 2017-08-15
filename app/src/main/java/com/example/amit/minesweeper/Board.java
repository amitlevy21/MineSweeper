package com.example.amit.minesweeper;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.GridLayout;


public class Board extends GridLayout {

    private static Board instance = null;
    private Block[] blocks;
    private int numOfBlocks;

    public static Board getInstance(Context context, int numOfBlocks) {
        if(instance == null) {
            instance = new Board(context, numOfBlocks);
        }
        return instance;
    }

    private Board(Context context, int numOfBlocks) {
        super(context);
        this.numOfBlocks = numOfBlocks;
        blocks = new Block[numOfBlocks];
        Display display = PlayActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

    }

    public boolean addBlock() {

        return true;
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
