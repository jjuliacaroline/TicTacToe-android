package com.julia.tictactoe.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.julia.tictactoe.database.highscores.DB_NAME;
import static com.julia.tictactoe.database.highscores.DB_VERSION;

public class DbHighscoresHelper extends SQLiteOpenHelper {


    public DbHighscoresHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + highscores.highscoreEntry.TABLE +
                " ( " + highscores.highscoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                highscores.highscoreEntry.COL_HIGHSCORE_SCORE + " INTEGER, " +
                highscores.highscoreEntry.COL_HIGHSCORE_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + highscores.highscoreEntry.TABLE);
        onCreate(db);
    }
}
