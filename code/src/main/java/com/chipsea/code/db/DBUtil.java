package com.chipsea.code.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chipsea.code.util.LogUtil;

/**
 * chipsea数据库操作类
 *
 * @author kenneth
 */
public class DBUtil extends SQLiteOpenHelper {

    // =====================================定义常量=======================================
    public static final String TAG = "DBUtil";

    private static DBUtil instance; // 当前实体类
    // 版本号，字段名
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "vendomagic.db"; // 必须要求db后缀~~！
    private Context context;

    // =====================================定义变量=======================================

    /**
     * 析构函数
     */
    private DBUtil(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    /**
     * 建表
     * @param tableSql
     */
    public void createTable(String tableSql) {
        getWritableDatabase().execSQL(tableSql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i(TAG, "onUpgrade from " + oldVersion + " to " + newVersion);

    }

    @Override
    public void close() {
        synchronized (this) {
            try {
                getWritableDatabase().close();
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
            }
        }
    }

    // ===================================自定义函数===================================

    /**
     * 得到当前实体类
     */
    public static DBUtil getInstance(Context context) {
        if (instance == null) {
            instance = new DBUtil(context);
        }
        return instance;
    }

    /**
     * 插入数据
     *
     * @param table
     * @param values
     * @return
     **/
    public long insert(String table, ContentValues values) {
        synchronized (this) {
            SQLiteDatabase db = this.getWritableDatabase();
            long err = db.insert(table, null, values);
            db.close();
            return err;
        }
    }

    /**
     * 更新数据
     */
    public int update(String table, ContentValues values, String where,
                      String[] whereArgs) {
        synchronized (this) {
            try {
                SQLiteDatabase db = this.getWritableDatabase();
                int ret = db.update(table, values, where, whereArgs);
                db.close();
                return ret;

            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                return -1;
            }
        }
    }

    /**
     * 查找数据 当使用完毕之后，必须记得c.close释放Cursor!
     *
     * @param sql
     * @return
     */
    public Cursor query(String sql) {
        LogUtil.e("query", sql);
        synchronized (this) {
            return query(sql, null);
        }
    }

    /**
     * 查找数据 当使用完毕之后，必须记得c.close释放Cursor!
     *
     * @param selectionArgs
     * @return
     */
    public Cursor query(String sql, String[] selectionArgs) {
        LogUtil.e("query", sql);
        synchronized (this) {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.rawQuery(sql, selectionArgs);
        }
    }

    /**
     * 删除数据
     */
    public int delete(String table, String where, String[] whereArgs) {
        synchronized (this) {
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                int ret = db.delete(table, where, whereArgs);
                db.close();
                return ret;
            } catch (Exception e) {
                LogUtil.e(TAG, e.getMessage());
                return -1;
            }
        }
    }

}
