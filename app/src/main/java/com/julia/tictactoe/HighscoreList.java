package com.julia.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.julia.tictactoe.database.DbHighscoresHelper;
import com.julia.tictactoe.database.highscores;
import java.util.ArrayList;
import java.util.List;

public class HighscoreList extends Activity{

    private BaseAdapter baseAdapter;
    private int indexScore;
    private int indexName;
    private ArrayList<String> scoresList;



@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore_list);

        bindToListView();
}


    public class Score {

        public final String name, score;

        public Score(String name, String score) {
            this.name = name;
            this.score = score;
        }
    }

    public void bindToListView() {

        DbHighscoresHelper dbHelper  = new DbHighscoresHelper(this);
        ListView mScoreListView = findViewById(R.id.highscore_list);

        SQLiteDatabase scoreDB = dbHelper.getReadableDatabase();
        List<Score> scoresList = new ArrayList<>();
        Cursor cursor = scoreDB.query(highscores.highscoreEntry.TABLE, new String[] {highscores.highscoreEntry._ID, highscores.highscoreEntry.COL_HIGHSCORE_SCORE, highscores.highscoreEntry.COL_HIGHSCORE_TITLE}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int indexScore = cursor.getColumnIndex(highscores.highscoreEntry.COL_HIGHSCORE_SCORE);
            int indexName = cursor.getColumnIndex(highscores.highscoreEntry.COL_HIGHSCORE_TITLE);

            scoresList.add(new Score(cursor.getString(indexName), cursor.getString(indexScore)));
        }

        cursor.close();
        scoreDB.close();

        if(baseAdapter == null) {
            mScoreListView.setAdapter(new CustomListAdapter(scoresList));
        }


    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
