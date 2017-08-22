package com.example.amit.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.view.ViewGroup;
import android.widget.ImageView;


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
        //setBackgroundColor(Color.LTGRAY);
        setBackgroundResource(R.drawable.ic_frame);

        setPadding(20,20,20,20);
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

    public void setNumOfMinesAround(int numOfMinesAround) {
        this.numOfMinesAround = numOfMinesAround;
    }

    public boolean hasMine() {
        return hasMine;
    }

    /** Returns true if the player has clicked a mine, false otherwise */
    public boolean press() {
        if(!isFlagged) {
            if (!isPressed) {
                isPressed = true;
                if (hasMine) {
                    setBackgroundColor(Color.RED);
                    return true;
                } else {
                    setBackgroundResource(R.drawable.ic_frame_pressed);
                    setText(String.valueOf(numOfMinesAround));
                    return false;
                }
            }
        }
        return false;
    }

    public void markFlag() {
        if (!isPressed) {
            if (!isFlagged) {
                setBackgroundResource(R.drawable.ic_flag);
                isFlagged = true;
                setText("F");
            } else {
                setBackgroundResource(0);
                setBackgroundResource(R.drawable.ic_frame);

                isFlagged = false;
            }
        }
    }

    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }
}
