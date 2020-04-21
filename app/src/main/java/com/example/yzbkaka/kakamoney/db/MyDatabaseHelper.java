package com.example.yzbkaka.kakamoney.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yzbkaka on 20-4-21.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    /**
     * 建立account表语句
     */
    public static final String CREATE_ACCOUNT = "create table Account("
            + "id integer primary key autoincrement,"
            + "money integer,"  //金额
            + "message text," //备注
            + "kind integer,"  //流出or流入
            + "type integer,"  //消费类型
            + "year integer,"  //日期
            + "month integer,"
            + "day integer)";


    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
