 package com.julia.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.julia.tictactoe.database.DbHighscoresHelper;
import com.julia.tictactoe.database.highscores;
import java.util.Random;

public class BoardActicity_PVC extends Activity implements View.OnClickListener {


    private int roundCount;
    public String currentPlayer = "O";
    private Button[][] buttons = new Button[3][3];
    private int player1Points;
    private DbHighscoresHelper dbHelper;
    private ListView mScoreListView;
    private String compText = "X";
    private String playText = "O";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_pvc);

        createTable();

        TextView whose_turn_pvc = findViewById(R.id.whose_turn_pvc);
        dbHelper  = new DbHighscoresHelper(this);
        mScoreListView = findViewById(R.id.highscore_list);
        whose_turn_pvc.setText("Your turn");

        currentPlayer = playText;

    }


    private void createTable() {

        //create the table
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                String btn_id = "btn_" + i + j;
                int resID = getResources().getIdentifier(btn_id, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
    }





    @Override
    public void onClick(final View v) {

        final TextView whose_turn_pvc = findViewById(R.id.whose_turn_pvc);

        // if there's a win, stop user from playing
        if (checkForWin()) {
            Toast.makeText(this, "Game over", Toast.LENGTH_SHORT).show();
            return;
        }

        //is the button empty
        if (!((Button) v).getText().equals("")) {
            return;
        }

        //game logic
        if (currentPlayer.equals(playText)) {
            ((Button) v).setText(playText);
            whose_turn_pvc.setText("Computer is playing...");

            if (checkForWin()) {
                whose_turn_pvc.setText("You won!");
                player1Points++;
            }

            roundCount++;

            if (playerMove(v, whose_turn_pvc)) {
                currentPlayer = compText;
                compMove(whose_turn_pvc, v);
                roundCount++;
            }

        } else {
            return;
        }
    }

    //checks the buttons for win
    private boolean checkForWin() {
        String[][] checkBoard = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //checks the board for matches
                checkBoard[i][j] = buttons[i][j].getText().toString();
            }
        }

        //lets go through all the rows to see if this is the winning row
        for (int i = 0; i < 3; i++) {
            if(checkBoard[i][0].equals(checkBoard[i][1])
                    && checkBoard[i][0].equals(checkBoard[i][2])
                    && ! checkBoard[i][0].equals("")) {
                return true; //there's a winner
            }
        }

        //lets go through columns to see if this is the winning column
        for (int i = 0; i < 3; i++) {
            if(checkBoard[0][i].equals(checkBoard[1][i])
                    && checkBoard[0][i].equals(checkBoard[2][i])
                    && ! checkBoard[0][i].equals("")) {
                return true; //there's a winner
            }
        }

        //lets check diagonals to check if this is the winning diagonal
        if(checkBoard[0][0].equals(checkBoard[1][1])
                && checkBoard[0][0].equals(checkBoard[2][2])
                && ! checkBoard[0][0].equals("")) {
            return true; //there's a winner
        }
        //lets check diagonals to check if this is the winning diagonal
        if(checkBoard[0][2].equals(checkBoard[1][1])
                && checkBoard[0][2].equals(checkBoard[2][0])
                && ! checkBoard[0][2].equals("")) {
            return true; //there's a winner
        }

        return false; //no winner
    }


    //what happens if player wins
    public void winnerPlayer1() {
        TextView whose_turn_pvc = findViewById(R.id.whose_turn_pvc);
        whose_turn_pvc.setText("You won!");
        enterName(player1Points);

    }

    //what happens if computer wins
    public void winnerComputer() {
        TextView whose_turn_pvc = findViewById(R.id.whose_turn_pvc);
        whose_turn_pvc.setText("The computer won!");
        Toast.makeText(getApplicationContext(), "computer won!", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder computerDialog = new AlertDialog.Builder(this);
                computerDialog.setMessage("Uh oh, computer won!");

                computerDialog.setPositiveButton("Play again!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                })
                        .setNegativeButton("Back to main menu", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(BoardActicity_PVC.this, MainActivity.class);
                            startActivity(i);
                        }
                    })
                        .setCancelable(false);
        computerDialog.show();
    }

    //what happens if it's a draw
    private void draw() {
        TextView whose_turn_pvc = findViewById(R.id.whose_turn_pvc);
        whose_turn_pvc.setText("It's a draw!");

        AlertDialog.Builder drawDialog = new AlertDialog.Builder(this)
                .setMessage("It's a draw!")
                .setPositiveButton("Play again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("Back to main menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(BoardActicity_PVC.this, MainActivity.class);
                        startActivity(i);
                    }
                })
                .setCancelable(false);
        drawDialog.show();


    }

    //method for getting the player name
    public boolean enterName(final int points) {
        if(checkForWin()) {
            final EditText editText = new EditText(this);

            android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("You won!")
                    .setMessage("Enter your name")
                    .setView(editText)
                    .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String getText = editText.getText().toString();
                            if (!getText.equals("")) {
                                new backGroundStuff(points, getText).execute();
                                Intent intent = new Intent(BoardActicity_PVC.this, HighscoreList.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getBaseContext(), "You must enter a name!", Toast.LENGTH_SHORT).show();
                                enterName(points);
                            }

                        }

                    })

                    .setNeutralButton("Play again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            startActivity(getIntent());
                        }
                    })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(BoardActicity_PVC.this, MainActivity.class);
                            startActivity(i);
                        }
                    })
                    .setCancelable(false)
                    .create();


            alert.show();

        }
        return false;
    }

    //player makes his move
    public boolean playerMove(View v, TextView whose_turn_pvc) {
        ((Button) v).setText(playText);
        whose_turn_pvc.setText("Computer is playing...");

        if (checkForWin()) {
            winnerPlayer1();
            player1Points++;
            return false;
        }
            if (roundCount == 9) {
                draw();
                return false;
            }

         return true;

    }


    //computer makes his move
    public void compMove(final TextView whose_turn_pvc, final View v) {

          final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                whose_turn_pvc.setText("Your turn");
                computerSetMark();

                if (checkForWin()) {
                    winnerComputer();
                }

                if (roundCount == 9) {
                    draw();
                }

                currentPlayer = playText;
            }
        }, 800);
    }


    //way computer finds the button it sets the mark using random
    public void computerSetMark() {

        Random rand = new Random();

        if (checkForWin()) {
            Toast.makeText(this, "Game over", Toast.LENGTH_SHORT).show();
            return;
        }
        Button randomButton = buttons[rand.nextInt(3)][rand.nextInt(3)];

        while (!randomButton.getText().equals("") && !checkForWin()) {
            randomButton = buttons[rand.nextInt(3)][rand.nextInt(3)];
        }

        if (randomButton.getText().equals("")) {
            randomButton.setText(compText);
        } else {
            return;
        }

    }


    //SQL database work is done here, in the background
    public class backGroundStuff extends AsyncTask<String, Void, Boolean> {

        int points;
        String getText;

        backGroundStuff(int points, String getText) {
            this.points = points;
            this.getText = getText;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(String...params) {

            SQLiteDatabase scoreDB = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(highscores.highscoreEntry.COL_HIGHSCORE_TITLE, this.getText);
            values.put(highscores.highscoreEntry.COL_HIGHSCORE_SCORE, this.points);

            Cursor c = scoreDB.rawQuery("SELECT * FROM highscores WHERE title LIKE '" + this.getText + "'", null);

            if (c.getCount() == 0) {
                c.close();
                scoreDB.insert(highscores.highscoreEntry.TABLE, null, values);
            } else {

                if (c.moveToFirst()) {
                    int pointsExist = c.getInt(c.getColumnIndex("score"));

                    int newPoint = this.points + pointsExist;
                    values.put(highscores.highscoreEntry.COL_HIGHSCORE_SCORE, newPoint);
                    scoreDB.update(highscores.highscoreEntry.TABLE, values, "title LIKE '" + this.getText + "'", null);

                }
                c.close();

            }

            scoreDB.close();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

        }

    }

}




