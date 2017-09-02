package com.example.amit.minesweeper;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import com.yalantis.starwars.TilesFrameLayout;
import com.yalantis.starwars.interfaces.TilesFrameLayoutListener;


public class PlayActivity extends AppCompatActivity implements Board.BoardListener, TilesFrameLayoutListener {

    public static final int BIGGER_FRACTION = 6;
    public static final int SMALLER_FRACTION = 12;


    private int seconds = 0;

    private Board board;
    private TilesFrameLayout mTilesFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Button quit = (Button) findViewById(R.id.button_quit);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EndGameActivity.class);
                intent.putExtra(Keys.RESULT, false);
                intent.putExtra(Keys.TIME, seconds);
                int cubes = board.getNumOfPressedBlocks();
                int goodFlags = board.getNumOfGoodFlags();
                intent.putExtra(Keys.GOOD_CUBES,cubes);
                intent.putExtra(Keys.GOOD_FLAGS,goodFlags);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();

            }
        });
        board = buildBoard();
        tickEndlessly();

        mTilesFrameLayout = (TilesFrameLayout) findViewById(R.id.tiles_frame_layout);
        mTilesFrameLayout.setOnAnimationFinishedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTilesFrameLayout.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTilesFrameLayout.onPause();
    }

    public Board buildBoard() {

        Bundle bundle = getIntent().getExtras();
        MainActivity.eDifficulty difficulty = (MainActivity.eDifficulty) bundle.getSerializable(Keys.DIFFICULTY);
        int numOfMines = bundle.getInt(Keys.NUM_OF_MINES);
        int boardSize = bundle.getInt(Keys.BOARD_SIZE);

        int buttonWidth = calculateButtonSize(difficulty);

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid);


        Board board = new Board(this, gridLayout, boardSize, buttonWidth, numOfMines, difficulty);
        board.setBoardListener(this);
        return board;
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


    private void tickEndlessly() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    tick();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void tick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView textTime = (TextView) findViewById(R.id.timer);
                textTime.setText(getString(R.string.play_activity_time) + " " + seconds);
            }
        });

    }

    @Override
    public void onUpdate(int numOfPressedBlocks, int numOfFlags, Board.eState state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                TextView flagsOnPlay = (TextView) findViewById(R.id.flags);
                TextView CubesOnPlay = (TextView) findViewById(R.id.score);
                flagsOnPlay.setText(getString(R.string.flags) + " " + board.getNumOfFlags());
                CubesOnPlay.setText(getString(R.string.score) + " " + board.getNumOfPressedBlocks());
            }
        });

        switch (state) {
            case IN_PROGRESS: break;
            case WIN:
            case LOSE:
                final Intent intent = new Intent(this, EndGameActivity.class);

                intent.putExtra(Keys.RESULT, state);
                intent.putExtra(Keys.TIME, seconds);


                intent.putExtra(Keys.GOOD_CUBES, numOfPressedBlocks);
                intent.putExtra(Keys.GOOD_FLAGS, numOfFlags);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                mTilesFrameLayout.startAnimation();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(intent);
                    }
                }, 1600);

                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        board.setBoardListener(null); // clear reference to the PlayActivity for garbage collector to clean
    }

    @Override
    public void onAnimationFinished() {
        endGame();
        finish();
    }

    private void endGame() {

    }
}
