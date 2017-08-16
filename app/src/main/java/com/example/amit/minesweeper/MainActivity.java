package com.example.amit.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public enum eDifficulty {
        EASY, INTERMEDIATE, HARD
    }

    static boolean played = false;
    static String showDifucllty;

    public static final int[] SCREEN_SIZE = {10, 10, 5};
    public static final int[] NUM_OF_MINES = {5, 10, 10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner difficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulties_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        difficultySpinner.setOnItemSelectedListener(this);

        Button start = (Button) findViewById(R.id.button_start);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get difficulty level selected
                int difficulty = difficultySpinner.getSelectedItemPosition();
                eDifficulty[] difficulties = eDifficulty.values();

                Intent intent = new Intent(view.getContext(), PlayActivity.class);
                intent.putExtra(Keys.DIFFICULTY, difficulties[difficulty]);
                intent.putExtra(Keys.SCREEN_SIZE, SCREEN_SIZE[difficulty]);
                intent.putExtra(Keys.NUM_OF_MINES, NUM_OF_MINES[difficulty]);

                showDifucllty = difficulties[difficulty].toString();
                played = true;
                startActivity(intent);

            }
        });

        if (played) {
            afterPlayedView();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        adapterView.setSelection(i);

        TextView screenSize = (TextView) findViewById(R.id.screen_size);
        screenSize.setText(getString(R.string.screen_size) + " " + SCREEN_SIZE[i] + " X " + SCREEN_SIZE[i]);

        TextView numOfMines = (TextView) findViewById(R.id.num_mines);
        numOfMines.setText(getString(R.string.num_mines) + " " + NUM_OF_MINES[i]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        adapterView.setSelection(0);
    }

    public void afterPlayedView() {
        TextView difficultyPlayed = (TextView) findViewById(R.id.last_mode_played);
        difficultyPlayed.setText(getString(R.string.last_mode_played) + " " + showDifucllty);
    }

}
