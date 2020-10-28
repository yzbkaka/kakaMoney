package com.example.yzbkaka.kakamoney.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.setting.MyApplication;

/**
 * Created by yzbkaka on 20-4-21.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static MyDatabaseHelper databaseHelper;

    /**
     * 建立account表语句
     */
    public static final String CREATE_ACCOUNT = "create table Account("
            + "id integer primary key autoincrement,"
            + "money text,"  //金额
            + "message text," //备注
            + "kind integer,"  //流出or流入
            + "type integer,"  //消费类型
            + "year integer,"  //日期
            + "month integer,"
            + "day integer)";

    public static final String CREATE_USER = "create table User("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "password text)";



    private MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static MyDatabaseHelper getInstance(){
        if(databaseHelper == null){
            databaseHelper = new MyDatabaseHelper(MyApplication.getContext(),"kakaMoneyStore.db",null,1);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ACCOUNT);
        sqLiteDatabase.execSQL(CREATE_USER);
        Log.e("yzbkaka","创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
