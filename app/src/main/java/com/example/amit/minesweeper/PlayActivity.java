package com.example.amit.minesweeper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yalantis.starwars.TilesFrameLayout;
import com.yalantis.starwars.interfaces.TilesFrameLayoutListener;



public class PlayActivity extends AppCompatActivity implements Board.BoardListener, TilesFrameLayoutListener {

    public static final int BIGGER_FRACTION = 6;
    public static final int SMALLER_FRACTION = 12;
    private static final int DEFAULT_ANIMATION_DURATION = 1600;


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


        Board board = new Board(this, gridLayout, boardSize, buttonWidth, numOfMines);
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
                seconds++;
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

        if(state.equals(Board.eState.WIN) || state.equals(Board.eState.LOSE)) {
            final Intent intent = new Intent(this, EndGameActivity.class);

            intent.putExtra(Keys.RESULT, state);
            intent.putExtra(Keys.TIME, seconds);


            intent.putExtra(Keys.GOOD_CUBES, numOfPressedBlocks);
            intent.putExtra(Keys.GOOD_FLAGS, numOfFlags);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            if(state.equals(Board.eState.LOSE)) {
                mTilesFrameLayout.startAnimation();
            }
            else { // WIN
                Drawable image = getDrawable(R.drawable.win_chuck_norris_approved);
                image.setAlpha(0);
                GridLayout grid = (GridLayout) findViewById(R.id.grid);
                final ImageView imageView = new ImageView(this);
                imageView.setVisibility(View.GONE);
                grid.addView(imageView);
                //// TODO: 04/09/17
                //GridLayout.LayoutParams lp = new GridLayout.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT);

                //imageView.setLayoutParams(lp);
                imageView.setBackground(image);

                ObjectAnimator fadeIn = ObjectAnimator.ofFloat(imageView, "alpha", 0.0f, 1.0f);
                fadeIn.setDuration(DEFAULT_ANIMATION_DURATION);
                fadeIn.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        imageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                final Button quit = (Button) findViewById(R.id.button_quit);
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int mScreenHeight = displaymetrics.heightPixels;

                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, -mScreenHeight);

                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                        float value = (float) animation.getAnimatedValue();
                        quit.setTranslationY(value);
                    }
                });

                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
                valueAnimator.start();
                fadeIn.start();

            }

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(intent);
                }
            }, DEFAULT_ANIMATION_DURATION);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        board.setBoardListener(null); // clear reference to the PlayActivity for garbage collector to clean
    }

    @Override
    public void onAnimationFinished() {
        finish();
    }


}
