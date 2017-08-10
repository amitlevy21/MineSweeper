package com.example.amit.minesweeper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public enum eDifficulty {
        EASY, INTERMEDIATE, HARD
    }

    public static final String[] SCREEN_SIZE = {"10 X 10", "10 X 10", "5 X 5"};
    public static final int[] NUM_OF_MINES = {5, 10, 10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner difficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulties_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        difficultySpinner.setOnItemSelectedListener(this);

        Button start = (Button) findViewById(R.id.button_start);
        //get difficulty level selected
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // switch case to find level then start right activity
                Toast.makeText(getApplicationContext(),"hi", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        eDifficulty[] difficulties = eDifficulty.values();

        adapterView.setSelection(i);

        TextView screenSize = (TextView) findViewById(R.id.screen_size);
        screenSize.setText(getString(R.string.screen_size) + " " + SCREEN_SIZE[i]);

        TextView numOfMines = (TextView) findViewById(R.id.num_mines);
        numOfMines.setText(getString(R.string.num_mines) + " " + NUM_OF_MINES[i]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        adapterView.setSelection(0);
    }
}
