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
        setLayoutParams(new ViewGroup.LayoutParams(size, size));
        setBackgroundResource(R.drawable.ic_frame);

        this.row = row;
        this.col = col;
    }
    public void paintGray(){
        setBackgroundResource(R.drawable.ic_frame);
    }

    public int getRow() { return row; }

    public int getCol() { return col; }

    public boolean getIsPressed() { // do not override isPressed by View class
        return isPressed;
    }

    public void setIsPressed(boolean press) { // do not override isPressed by View class
        this.isPressed = press;
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
        if (!isFlagged) {
            if (!isPressed) {
                isPressed = true;
                if (hasMine) {
                    setBackgroundColor(Color.RED);
                    return true;
                } else {
                    setBackgroundResource(R.drawable.ic_frame_pressed);
                    if (numOfMinesAround != 0) {
                        int numOfMinesAroundPicture = whichPicture(getNumOfMinesAround());
                        setBackgroundResource(numOfMinesAroundPicture);
                        return false;
                    }
                }
            }

        }return false;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public int whichPicture(int numOfMinesAround){

        switch (numOfMinesAround) {
            case 1: return R.drawable.ic_action_name1;
            case 2: return R.drawable.ic_action_name2;
            case 3: return R.drawable.ic_action_name3;
            case 4: return R.drawable.ic_action_name4;
            case 5: return R.drawable.ic_action_name5;
            case 6: return R.drawable.ic_action_name6;
            case 7: return R.drawable.ic_action_name7;
            case 8: return R.drawable.ic_action_name8;
            default: break;

        }return 0;
    }


    public void markFlag() {
        if (!isPressed) {
            if (!isFlagged) {
                setBackgroundResource(R.drawable.ic_flag);
                isFlagged = true;
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
