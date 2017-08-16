package com.example.amit.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.view.ViewGroup;


public class Block extends AppCompatButton{


    private boolean isPressed = false;
    private boolean isFlagged = false;
    private boolean hasMine = true;
    private int numOfMinesAround;

    public Block(Context context, int size) {

        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(size,size));
        setBackgroundColor(Color.LTGRAY);

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

    public boolean hasMine() {
        return hasMine;
    }

    public void press() {
        isPressed = true;
    }

    public void markFlag() {
        isFlagged = true;
    }
}
