package com.chipsea.code.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/3/9.
 */

public class AirDBHelper  extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "1.db";
    private static final int DATABASE_VERSION = 1;
    private static AirDBHelper instance ;
    public static AirDBHelper getInstance(Context ctx){
        if(instance == null){
            instance = new AirDBHelper(ctx) ;
        }
        return instance ;
    }
    /** Create a helper object for the Events database */
    private AirDBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
