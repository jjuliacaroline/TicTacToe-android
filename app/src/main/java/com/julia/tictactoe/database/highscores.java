package com.julia.tictactoe.database;

import android.provider.BaseColumns;

public class highscores {

    public static final String DB_NAME = " com.julia.tictactoe.database";
    public static final int DB_VERSION = 1;

    public class highscoreEntry implements BaseColumns {

        public static final String TABLE = "highscores";
        public static final String COL_HIGHSCORE_SCORE = "score";
        public static final String COL_HIGHSCORE_TITLE = "title";

    }



}