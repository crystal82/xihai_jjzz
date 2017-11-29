package com.chipsea.code.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chipsea.mode.entity.AirModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class ModelTable {
    private AirDBHelper dbHelper;
    private static ModelTable instance;
    /**
     * 析构函数
     */
    private ModelTable(Context context) {
        dbHelper = AirDBHelper.getInstance(context);
    }

    /**
     * 得到当前实体类
     */
    public static ModelTable getInstance(Context context) {
        if (instance == null) {
            instance = new ModelTable(context);
        }
        return instance;
    }
    public List<AirModel> getMatchByRets(List<String> rets){
        List<AirModel> models = new ArrayList<>() ;
        for (int i = 0; i < rets.size(); i++) {
            models.add(getModelByKey(rets.get(i))) ;
        }
        return models ;
    }
    public AirModel getModelByKey(String key){
        synchronized (dbHelper) {
            AirModel airModel = null ;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from model where m_keyfile=? and device_id=1", new String[]{key});
            while (c.moveToNext()) {
                airModel =  creat(c) ;
            }
            c.close();
            db.close();
            return airModel;
        }
    }
    public int[][] getAllBrandRand(){
        int[][] macth ;
        synchronized (dbHelper) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor c = null;
            c = db.rawQuery("select * from model where device_id=1", null);
            int i = 0 ;
            macth = new int[c.getCount()][2] ;
            while (c.moveToNext()) {
                int m_format_id = Integer.parseInt(c.getString(c.getColumnIndex("m_format_id")));
                int m_rank = c.getInt(c.getColumnIndex("m_rank"));
                macth[i][0]= m_format_id;
                macth[i][1]= m_rank;
                i++ ;
            }
            c.close();
            db.close();
            return macth;
        }
    }
    public AirModel creat(Cursor c){
        AirModel airModel = new AirModel() ;
        airModel.setM_code(c.getInt(c.getColumnIndex("m_code")));
        airModel.setM_format_id(c.getInt(c.getColumnIndex("m_format_id")));
        airModel.setM_key_squency(c.getInt(c.getColumnIndex("m_key_squency")));
        airModel.setM_keyfile(c.getInt(c.getColumnIndex("m_keyfile")));
        airModel.setM_label(c.getString(c.getColumnIndex("m_label")));
        airModel.setM_rank(c.getInt(c.getColumnIndex("m_rank")));
        airModel.setM_search_string(c.getString(c.getColumnIndex("m_search_string")));
        return airModel ;
    }
}
