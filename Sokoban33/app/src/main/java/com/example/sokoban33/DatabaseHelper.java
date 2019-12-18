package com.example.sokoban33;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "scores.db";
    public static final String TABLE_NAME = "score";
    public static final String COL_1 = "name";
    public static final String COL_2 = "score";
    public static final String COL_3 = "level";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table " + TABLE_NAME + " (NAME TEXT, SCORE INTEGER, LEVEL INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, int score, int level) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, name);
        contentValues.put(COL_2, score);
        contentValues.put(COL_3, level);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if(result == -1)
            return false;

        return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY score", null);

        return res;
    }

    public void deleteScoreData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

}
