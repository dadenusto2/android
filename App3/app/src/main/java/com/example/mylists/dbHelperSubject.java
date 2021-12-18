package com.example.mylists;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelperSubject extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app2.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE = "subjects"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ID_STUDENT = "id_student";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MARK = "mark";

    public dbHelperSubject(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(new StringBuilder().append("CREATE TABLE IF NOT EXISTS").append(DATABASE_NAME).append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ").append(COLUMN_ID_STUDENT).append(" INTEGER, ").append(COLUMN_NAME).append(" TEXT, ").append(COLUMN_MARK).append(" INTEGER);").toString());
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
