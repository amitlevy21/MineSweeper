package com.example.amit.minesweeper;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;


public class PlayActivity extends AppCompatActivity {

    public static final int FRACTION = 5;
    boolean won;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Bundle bundle = getIntent().getExtras();
        final MainActivity.eDifficulty difficulty = (MainActivity.eDifficulty) bundle.getSerializable(Keys.DIFFICULTY);
        int numOfMines = bundle.getInt(Keys.NUM_OF_MINES);
        int screenSize = bundle.getInt(Keys.SCREEN_SIZE);
        final String result = "result";


        Button quit = (Button) findViewById(R.id.button_quit);

        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), EndGameActivity.class);

                won = false;
                intent.putExtra(result, won);

                startActivity(intent);

            }
        });


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int theSmallerAxis = height < width ? height : width;
        int buttonWidth = theSmallerAxis / FRACTION;

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid);

        Board board = Board.getInstance(gridLayout, screenSize*screenSize);

        gridLayout.setRowCount(screenSize);
        gridLayout.setColumnCount(screenSize);



        for (int i = 0; i < screenSize * screenSize; i++) {
            Block block = new Block(this, buttonWidth, buttonWidth);
            block.setLayoutParams(new ViewGroup.LayoutParams(buttonWidth, buttonWidth));
            board.addBlock(block);
        }


    }
}
