package com.example.amit.minesweeper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Bundle bundle = getIntent().getExtras();
        boolean result = (boolean) bundle.getSerializable(Keys.RESULT);
        long timeToShow = ((long) bundle.getSerializable(Keys.TIME))/1000;
        int goodCubes = (int) bundle.getSerializable(Keys.GOOD_CUBES);
        int goodFlags = (int) bundle.getSerializable(Keys.GOOD_FLAGS);
        Button tryAgain = (Button) findViewById(R.id.try_again);

        if (result) {
            TextView textResult = (TextView) findViewById(R.id.result);
            textResult.setText(getString(R.string.result) + " " + getString(R.string.won_text));
        } else {
            TextView textResult = (TextView) findViewById(R.id.result);
            textResult.setText(getString(R.string.result) + " " + getString(R.string.lost_text));
        }
            TextView textTime = (TextView) findViewById(R.id.time);
            textTime.setText(getString(R.string.end_activity_time) + " " + timeToShow + " sec");

            TextView numOfGoodCubes = (TextView) findViewById(R.id.num_of_good_cubes);
            numOfGoodCubes.setText(getString(R.string.num_of_correct_cubes) + " " +goodCubes );

            TextView numOfGoodFlags = (TextView) findViewById(R.id.num_of_good_flags);
            numOfGoodFlags.setText(getString(R.string.num_of_correct_flags) + " " +goodFlags );


        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);


            }
        });
    }
}
