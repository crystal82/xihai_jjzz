package com.chipsea.code.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2017/3/9.
 */

public class FormatsTable {
    private AirDBHelper dbHelper;
    private static FormatsTable instance;
    /**
     * 析构函数
     */
    private FormatsTable(Context context) {
        dbHelper = AirDBHelper.getInstance(context);
    }

    /**
     * 得到当前实体类
     */
    public static FormatsTable getInstance(Context context) {
        if (instance == null) {
            instance = new FormatsTable(context);
        }
        return instance;
    }
    public String[][] getAllMaches(){
        String[][] macth ;
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from formats where device_id=1", null);
            int i = 0 ;
            macth = new String[10000][2] ;
            while (c.moveToNext()) {
                int fid = Integer.parseInt(c.getString(c.getColumnIndex("fid")));
                String matchs = c.getString(c.getColumnIndex("matchs"));
                macth[fid][0]=matchs;
                macth[fid][1]= "0";
            }
            c.close();
            db.close();
            return macth;
        }
    }
}
