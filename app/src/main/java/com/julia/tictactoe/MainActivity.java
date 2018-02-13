package com.julia.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button pvp_btn = findViewById(R.id.pvp);
        pvp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pvpIntent = new Intent(MainActivity.this, BoardActivity.class);
                startActivity(pvpIntent);
            }
        });

        Button highScore = findViewById(R.id.highScores);
        highScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scoreIntent = new Intent(MainActivity.this, HighscoreList.class);
                startActivity(scoreIntent);
            }
        });

        Button pvc_btn = findViewById(R.id.pvc);
        pvc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pvpIntent = new Intent(MainActivity.this, BoardActicity_PVC.class );
                startActivity(pvpIntent);
            }
        });
    }


        @Override
        public void onBackPressed() {
             moveTaskToBack(true);
        }
}
