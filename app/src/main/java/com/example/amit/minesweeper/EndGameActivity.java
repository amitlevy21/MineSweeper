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
        boolean result = (boolean) bundle.getSerializable("result");
        Button tryAgain = (Button) findViewById(R.id.try_again);

        if (result) {
            TextView textResult = (TextView) findViewById(R.id.result);
            textResult.setText(getString(R.string.result) + " you have won");
        } else {
            TextView textResult = (TextView) findViewById(R.id.result);
            textResult.setText(getString(R.string.result) + " you have lost");
        }

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);


            }
        });
    }
    //test
}
