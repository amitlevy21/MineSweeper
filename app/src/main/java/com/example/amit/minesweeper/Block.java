package com.example.amit.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.view.ViewGroup;


public class Block extends AppCompatButton{


    private boolean isPressed = false;
    private boolean isFlagged = false;
    private boolean hasMine = false;
    private int numOfMinesAround;
    private int row;
    private int col;

    public Block(Context context, int row, int col, int size) {

        super(context);
        setLayoutParams(new ViewGroup.LayoutParams(size,size));
        setBackgroundColor(Color.LTGRAY);
        this.row = row;
        this.col = col;


    }

    public int getRow() { return row; }

    public int getCol() { return col; }

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

    public boolean press() {
        if(!isPressed) {
            isPressed = true;
            if(hasMine) {
                setBackgroundColor(Color.RED);
                return false;
            }
            else {
                setBackgroundColor(Color.BLUE);
                return true;
            }
        }
        return true;
    }

    public void markFlag() {
        if(!isFlagged) {
            setText("F");
            isFlagged = true;
        }
        else {
            setText("");
            isFlagged = false;
        }
    }
}
