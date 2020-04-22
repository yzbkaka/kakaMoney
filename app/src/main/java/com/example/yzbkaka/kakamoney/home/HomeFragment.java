package com.example.yzbkaka.kakamoney.home;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.model.Account;
import com.example.yzbkaka.kakamoney.setting.MyApplication;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Toolbar toolbar;

    /**
     * 本月支出text
     */
    private TextView monthSpend;

    private Button eye;

    private TextView monthIncome;

    private TextView monthBudget;

    private RecyclerView recyclerView;

    private FloatingActionButton add;

    private int count = 0;

    private MyDatabaseHelper databaseHelper;

    private List<Account> accountList = new ArrayList<>();

    private AccountAdapter adapter;

    private Calendar calendar;

    private int year,month,day;

    private int monthSpendSum = 0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar)view.findViewById(R.id.home_toolbar);
        monthSpend = (TextView)view.findViewById(R.id.month_spend);
        eye = (Button)view.findViewById(R.id.eye);
        monthIncome = (TextView)view.findViewById(R.id.month_income);
        monthBudget = (TextView)view.findViewById(R.id.month_budget);
        recyclerView = (RecyclerView)view.findViewById(R.id.today_recycler_view);
        add = (FloatingActionButton)view.findViewById(R.id.add);
        databaseHelper = MyDatabaseHelper.getInstance();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        initView();
        return view;
    }

    private void initView(){
        if(count % 2 != 0){

        }else {

        }
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getContext(),AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        accountList.clear();
        monthSpendSum = getMonthSpendSum();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"year=? and month =? and day=?",new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)},null,null,null);
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                Account account = new Account();
                account.setMoney(cursor.getString(cursor.getColumnIndex("money")));
                account.setMessage(cursor.getString(cursor.getColumnIndex("message")));
                Log.d(TAG, "message: " + account.getMessage());
                account.setKind(cursor.getInt(cursor.getColumnIndex("kind")));
                account.setType(cursor.getInt(cursor.getColumnIndex("type")));
                Log.d(TAG, "type: " + account.getType());
                account.setYear(cursor.getInt(cursor.getColumnIndex("year")));
                account.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
                account.setDay(cursor.getInt(cursor.getColumnIndex("day")));
                accountList.add(account);
            }
            cursor.close();
        }
        adapter = new AccountAdapter(accountList);
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        monthSpend.setText(String.valueOf(monthSpendSum));
    }

    /**
     * 获取本月总支出
     */
    public int getMonthSpendSum(){
        int sum = 0;
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"year=? and month =?",new String[]{String.valueOf(year), String.valueOf(month)},null,null,null);
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                if(!cursor.getString(cursor.getColumnIndex("money")).equals("")){
                    sum = sum + Integer.parseInt(cursor.getString(cursor.getColumnIndex("money")));
                }
            }
        }
        return sum;
    }

    @Override
    public void onClick(View view) {

    }
}

