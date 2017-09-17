package com.example.amit.minesweeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private boolean didAlreadyRequestLocationPermission;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    public enum eDifficulty {
        EASY, INTERMEDIATE, HARD
    }

    private static boolean played = false;
    private static String showDifficulty;

    public static final int[] BOARD_SIZE = {10, 10, 5};
    public static final int[] NUM_OF_MINES = {5, 10, 10};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView difficultyPlayed = (TextView) findViewById(R.id.last_mode_played);
        difficultyPlayed.setText(getString(R.string.last_mode_played) + " " + getString(R.string.not_played_yet));

        final Spinner difficultySpinner = (Spinner) findViewById(R.id.difficulty_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.difficulties_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter);

        difficultySpinner.setOnItemSelectedListener(this);

        Button start = (Button) findViewById(R.id.button_start);
        Button leaderBoard = (Button) findViewById(R.id.button_leader_board);



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get difficulty level selected
                int difficulty = difficultySpinner.getSelectedItemPosition();
                eDifficulty[] difficulties = eDifficulty.values();

                Intent intent = new Intent(view.getContext(), PlayActivity.class);
                intent.putExtra(Keys.DIFFICULTY, difficulties[difficulty]);
                intent.putExtra(Keys.BOARD_SIZE, BOARD_SIZE[difficulty]);
                intent.putExtra(Keys.NUM_OF_MINES, NUM_OF_MINES[difficulty]);

                showDifficulty = difficulties[difficulty].toString();
                played = true;
                startActivity(intent);

            }
        });

        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), LeaderBoardActivity.class);
                startActivity(intent);
            }
        });

        if (played) {
            difficultyPlayed = (TextView) findViewById(R.id.last_mode_played);
            difficultyPlayed.setText(getString(R.string.last_mode_played) + " " + showDifficulty);
        }
        askPermissions();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        adapterView.setSelection(i);

        TextView screenSize = (TextView) findViewById(R.id.board_size);
        screenSize.setText(getString(R.string.screen_size) + " " + BOARD_SIZE[i] + " X " + BOARD_SIZE[i]);

        TextView numOfMines = (TextView) findViewById(R.id.num_mines);
        numOfMines.setText(getString(R.string.num_mines) + " " + NUM_OF_MINES[i]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        adapterView.setSelection(0);
    }

    public void askPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
            String coarseLocationPermission = Manifest.permission.ACCESS_COARSE_LOCATION;
            if (getApplicationContext().checkSelfPermission(fineLocationPermission) != PackageManager.PERMISSION_GRANTED ||
                    getApplicationContext().checkSelfPermission(coarseLocationPermission) != PackageManager.PERMISSION_GRANTED) {
                // The user blocked the location services of THIS app / not yet approved
                if (!didAlreadyRequestLocationPermission) {
                    didAlreadyRequestLocationPermission = true;
                    String[] permissionsToAsk = new String[]{fineLocationPermission, coarseLocationPermission};
                    requestPermissions(permissionsToAsk, LOCATION_PERMISSION_REQUEST_CODE);
                }
            }
        }
    }


}
