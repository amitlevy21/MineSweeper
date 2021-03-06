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
        Board.eState result = (Board.eState) bundle.getSerializable(Keys.RESULT);
        int minutes =  bundle.getInt(Keys.TIME);
        int goodCubes = bundle.getInt(Keys.GOOD_CUBES);
        int goodFlags = bundle.getInt(Keys.GOOD_FLAGS);
        Button tryAgain = (Button) findViewById(R.id.try_again);
        Button leaderBoard = (Button) findViewById(R.id.button_leader_board);

        if (result.equals(Board.eState.WIN)) {
            TextView textResult = (TextView) findViewById(R.id.result);
            textResult.setText(getString(R.string.result) + " " + getString(R.string.won_text));
        } else {
            TextView textResult = (TextView) findViewById(R.id.result);
            textResult.setText(getString(R.string.result) + " " + getString(R.string.lost_text));
        }
            TextView textTime = (TextView) findViewById(R.id.time);
            textTime.setText(getString(R.string.end_activity_time) + " " + minutes + " sec");

            TextView numOfGoodCubes = (TextView) findViewById(R.id.num_of_good_cubes);
            numOfGoodCubes.setText(getString(R.string.num_of_correct_cubes) + " " +goodCubes );

            TextView numOfGoodFlags = (TextView) findViewById(R.id.num_of_good_flags);
            numOfGoodFlags.setText(getString(R.string.num_of_correct_flags) + " " +goodFlags );


        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(intent);
                finish();
            }
        });

        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), LeaderBoardActivity.class);
                startActivity(intent);
            }
        });
    }
}
