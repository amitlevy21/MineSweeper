package com.example.amit.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.view.View;


public class Block extends View{

    public enum eContains {
        EMPTY, NUMBER, MINE
    }

    private eContains content;
    private boolean isPressed = false;
    private boolean isFlagged = false;
    private int numOfMinesAround;

    public Block(Context context, int left, int top, int right, int bottom,
                 int numOfMinesAround, eContains content) {

        super(context);
        layout(left,top,right,bottom);

        setBackgroundColor(Color.LTGRAY);
        this.numOfMinesAround = numOfMinesAround;
        this.content = content;
    }

    public eContains getContent() {
        return content;
    }

    public boolean getIsPressed() { // do not override isPressed by View class
        return isPressed;
    }

    public boolean getIsFlagged() {
        return isFlagged;
    }

    public int getNumOfMinesAround() {
        return numOfMinesAround;
    }

    public void press() {
        isPressed = true;
    }

    public void markFlag() {
        isFlagged = true;
    }
}
