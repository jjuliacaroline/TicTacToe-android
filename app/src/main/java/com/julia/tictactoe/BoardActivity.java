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
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.julia.tictactoe.database.DbHighscoresHelper;
import com.julia.tictactoe.database.highscores;

public class BoardActivity extends Activity implements View.OnClickListener {

    private boolean player1Turn = true;
    private int roundCount;
    private Button[][] buttons = new Button[3][3];
    private int player1Points;
    private int player2Points;
    private static DbHighscoresHelper dbHelper;
    private ListView mScoreListView;
    private int turnsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        createTable();

        dbHelper = new DbHighscoresHelper(this);
        mScoreListView = findViewById(R.id.highscore_list);

        TextView whoseTurn = (TextView) findViewById(R.id.whose_turn);
        whoseTurn.setText("Player 1's turn");
    }

    private void createTable() {

        //create the table
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String btn_id = "button_" + i + j;
                int resID = getResources().getIdentifier(btn_id, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {

        TextView whoseTurn = (TextView) findViewById(R.id.whose_turn);


        if (checkForWin()) {
            Toast.makeText(this, "Game over", Toast.LENGTH_SHORT).show();
            return;
        }

        //checks if the clicked button is taken
        if (!((Button) v).getText().equals("")) {
            return;
        }

        //let's check whose turn it is, puts the mark in the button
        if (player1Turn) {
            ((Button) v).setText("X");
            turnsCount++;
        } else {
            ((Button) v).setText("O");
            turnsCount++;
        }

        if (player1Turn) {
            whoseTurn.setText("Player 2's turn");

        } else {
            whoseTurn.setText("Player 1's turn");
        }

        roundCount++;

        //win and draw logic
        if (checkForWin()) {
            if (player1Turn) {
                winnerPlayer1();
                turnsCount++;
            } else {
                winnerPlayer2();
                turnsCount++;
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }

    }

    //checks if there's a win
    private boolean checkForWin() {
        String[][] checkBoard = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //checks the board for matches
                checkBoard[i][j] = buttons[i][j].getText().toString();
            }
        }

        //lets go through all the rows
        for (int i = 0; i < 3; i++) {
            if (checkBoard[i][0].equals(checkBoard[i][1])
                    && checkBoard[i][0].equals(checkBoard[i][2])
                    && !checkBoard[i][0].equals("")) {
                return true; //there's a winner
            }
        }

        //lets go through columns
        for (int i = 0; i < 3; i++) {
            if (checkBoard[0][i].equals(checkBoard[1][i])
                    && checkBoard[0][i].equals(checkBoard[2][i])
                    && !checkBoard[0][i].equals("")) {
                return true; //there's a winner
            }
        }

        //lets check diagonals
        if (checkBoard[0][0].equals(checkBoard[1][1])
                && checkBoard[0][0].equals(checkBoard[2][2])
                && !checkBoard[0][0].equals("")) {
            return true; //there's a winner
        }
        //lets check diagonals
        if (checkBoard[0][2].equals(checkBoard[1][1])
                && checkBoard[0][2].equals(checkBoard[2][0])
                && !checkBoard[0][2].equals("")) {
            return true; //there's a winner
        }

        return false; //no winner
    }


    // what happens if player 1 wins
    public void winnerPlayer1() {
        TextView whoseTurn = (TextView) findViewById(R.id.whose_turn);
        Toast.makeText(getApplicationContext(), "Player 1 won", Toast.LENGTH_SHORT).show();
        whoseTurn.setText("Player 1 won!");
        player1Points++;
        enterName(player1Points);
    }

    //what happens if player 2 wins
    public void winnerPlayer2() {
        TextView whoseTurn = (TextView) findViewById(R.id.whose_turn);
        Toast.makeText(getApplicationContext(), "Player 2 won", Toast.LENGTH_SHORT).show();
        whoseTurn.setText("Player 2 won!");
        player2Points++;
        enterName(player2Points);
    }

    // what happens if it's a draw
    private void draw() {
        TextView whoseTurn = (TextView) findViewById(R.id.whose_turn);
        whoseTurn.setText("It's a draw!");

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
                        Intent i = new Intent(BoardActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                })
                .setCancelable(false);
        drawDialog.show();
    }


    // method for getting the name of the player
    public boolean enterName(final int points) {
        if (checkForWin()) {
            final EditText editText = new EditText(this);
            editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
            final android.support.v7.app.AlertDialog alert = new android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("You won!")
                    .setMessage("Enter your name")
                    .setView(editText)
                    .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String getText = editText.getText().toString();

                            if (!getText.equals("")) {
                                new backGroundStuff(points, getText).execute();
                                Intent intent = new Intent(BoardActivity.this, HighscoreList.class);
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
                            Intent i = new Intent(BoardActivity.this, MainActivity.class);
                            startActivity(i);
                        }
                    })
                    .setCancelable(false)
                    .create();
            alert.show();

        }
        return false;
    }

    //SQL database work is done here, in the background
    public static class backGroundStuff extends AsyncTask<String, Void, Boolean> {

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
        protected Boolean doInBackground(String... params) {
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

                } c.close();

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


