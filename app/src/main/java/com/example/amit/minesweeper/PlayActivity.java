package com.example.amit.minesweeper;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;


public class PlayActivity extends AppCompatActivity {

    public static final int BIGGER_FRACTION = 6;
    public static final int SMALLER_FRACTION = 11;
    public static final int MAX_GAME_DURATION = 300000000;

    private boolean won;
    private int minutes = 0;

    TimeCounter timer = new TimeCounter(MAX_GAME_DURATION) {
        public void onTick(int second) {
            if(second%60 == 0 &&second >0)
                minutes++;
            TextView textTime = (TextView) findViewById(R.id.timer);
            if((second%60)/10 == 0)
                textTime.setText(getString(R.string.play_activity_time) + " " +minutes + ":0" + String.valueOf(second%60));
            else
                textTime.setText(getString(R.string.play_activity_time) + " " +minutes + ":" + String.valueOf(second%60));
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        timer.start();
        final long startTime = System.currentTimeMillis();

        Button quit = (Button) findViewById(R.id.button_quit);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EndGameActivity.class);

                long difference = System.currentTimeMillis() - startTime;

                won = false;
                intent.putExtra(Keys.RESULT, won);
                intent.putExtra(Keys.TIME, difference);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();

            }
        });
        buildBoard();
    }

    public void buildBoard() {

        Bundle bundle = getIntent().getExtras();
        MainActivity.eDifficulty difficulty = (MainActivity.eDifficulty) bundle.getSerializable(Keys.DIFFICULTY);
        int numOfMines = bundle.getInt(Keys.NUM_OF_MINES);
        int boardSize = bundle.getInt(Keys.BOARD_SIZE);

        int buttonWidth = calculateButtonSize(difficulty);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid);


        Board board = new Board(getApplicationContext(),gridLayout, boardSize, buttonWidth, numOfMines);

    }


    public int calculateButtonSize(MainActivity.eDifficulty difficulty) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int fraction;

        if (difficulty.ordinal() <= MainActivity.eDifficulty.INTERMEDIATE.ordinal())
            fraction = SMALLER_FRACTION;
        else
            fraction = BIGGER_FRACTION;

        int theSmallerAxis = height < width ? height : width;
        return theSmallerAxis / fraction;
    }


}
