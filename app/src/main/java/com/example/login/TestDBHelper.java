package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class TestDBHelper extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "answer.db";

    private static final String TABLE_NAME = "UserAnswer";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_ID = "_id";
    private Context context;

    public TestDBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String query = "CREATE TABLE " + TABLE_NAME
//                + " ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + COLUMN_CONTENT + " TEXT); ";
        db.execSQL("CREATE TABLE IF NOT EXISTS UserAnswer (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void InsertAnswer(String _content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CONTENT, _content);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1)
        {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "데이터 추가 성공", Toast.LENGTH_SHORT).show();
        }

    }
}
