package com.example.amit.minesweeper;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.GridLayout;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Bundle bundle = getIntent().getExtras();
        MainActivity.eDifficulty difficulty = (MainActivity.eDifficulty) bundle.getSerializable(Keys.DIFFICULTY);
        int numOfMines = bundle.getInt(Keys.NUM_OF_MINES);
        int screenSize = bundle.getInt(Keys.SCREEN_SIZE);

        GridLayout grid = (GridLayout) findViewById(R.id.grid);
        grid.setRowCount(screenSize);
        grid.setColumnCount(screenSize);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        for (int i = 0; i < screenSize; i++) {
            for (int j = 0; j < screenSize; j++) {
                TextView text = new TextView(this);
                text.setText("HI");
                grid.addView(text);
            }
        }
        grid.invalidate();
    }
}
