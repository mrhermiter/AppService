package com.example.dmk.appservice;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by dmk on 03.09.15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_TABLE_LOCATIONS = "locations";
    public static final String DATABASE_TABLE_TASKS = "tasks";

    public static final String LONG = "longitude";
    public static final String LAT = "latitude";
    public static final String TST = "timest";

    public static final String TID = "taskID";
    public static final String TITL = "title";
    public static final String DESCR = "description";

    private static final String DATABASE_CREATE_TABLE_LOCATIONS = "create table "
            + DATABASE_TABLE_LOCATIONS + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + LONG
            + " text not null, " + LAT
            + " text not null, " + TST + " text not null);";

    private static final String DATABASE_CREATE_TABLE_TASKS = "create table "
            + DATABASE_TABLE_TASKS + " ("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + TID  + " text not null, "
            + TITL  + " text not null, "
            + DESCR  + " text not null, "
            + LONG + " text not null, "
            + LAT  + " text not null, "
            + TST  + " text not null);";

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_TABLE_LOCATIONS);
        db.execSQL(DATABASE_CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_LOCATIONS);
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE_TASKS);
        onCreate(db);
    }
}
