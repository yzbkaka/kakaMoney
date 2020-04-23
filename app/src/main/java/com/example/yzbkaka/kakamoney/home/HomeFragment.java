package com.example.yzbkaka.kakamoney.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.model.Account;
import com.example.yzbkaka.kakamoney.setting.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Toolbar toolbar;

    /**
     * 本月支出text
     */
    private TextView monthOutText;

    private Button eye;

    /**
     * 本月收入text
     */
    private TextView monthInText;

    /**
     * 本月预算text
     */
    private TextView budgetLeftText;

    /**
     * 预算按钮
     */
    private Button budgetButton;

    private RecyclerView recyclerView;

    private FloatingActionButton add;

    private int count = 0;

    private MyDatabaseHelper databaseHelper;

    private List<Account> accountList = new ArrayList<>();

    private AccountAdapter adapter;

    private Calendar calendar;

    private int year,month,day;

    /**
     * 本月支出总和
     */
    private int monthOutSum = 0;

    /**
     * 本月收入总和
     */
    private int monthInSum = 0;

    /**
     * 预算剩余
     */
    private int budgetLeft = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar)view.findViewById(R.id.home_toolbar);
        monthOutText = (TextView)view.findViewById(R.id.month_spend);
        eye = (Button)view.findViewById(R.id.eye);
        eye.setOnClickListener(this);
        monthInText = (TextView)view.findViewById(R.id.month_income);
        budgetLeftText = (TextView)view.findViewById(R.id.month_budget);
        budgetButton = (Button)view.findViewById(R.id.budget_button);
        budgetButton.setOnClickListener(this);
        recyclerView = (RecyclerView)view.findViewById(R.id.today_recycler_view);
        add = (FloatingActionButton)view.findViewById(R.id.add);
        add.setOnClickListener(this);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        monthOutSum = getMonthOutSum();
        monthOutText.setText(String.valueOf(monthOutSum));
        monthInSum = getMonthInSum();
        monthInText.setText(String.valueOf(monthInSum));
        budgetLeft = getBudgetLeft();
        Log.d(TAG, "onResume:budgetLeft " + budgetLeft) ;
        budgetLeftText.setText(String.valueOf(budgetLeft));
        accountList.clear();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"year=? and month =? and day=?",new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)},null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                Account account = new Account();
                account.setMoney(cursor.getString(cursor.getColumnIndex("money")));
                account.setMessage(cursor.getString(cursor.getColumnIndex("message")));
                account.setKind(cursor.getInt(cursor.getColumnIndex("kind")));
                account.setType(cursor.getInt(cursor.getColumnIndex("type")));
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
    }

    /**
     * 获取本月总支出
     */
    public int getMonthOutSum(){
        int sum = 0;
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"year=? and month=? and kind=?",new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(Type.OUT)},null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                if(!cursor.getString(cursor.getColumnIndex("money")).equals("")){
                    sum = sum + Integer.parseInt(cursor.getString(cursor.getColumnIndex("money")));
                }
            }
        }
        return sum;
    }

    /**
     * 获取本月总收入
     */
    private int getMonthInSum(){
        int sum = 0;
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"year=? and month=? and kind=?",new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(Type.IN)},null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                if(!cursor.getString(cursor.getColumnIndex("money")).equals("")){
                    sum = sum + Integer.parseInt(cursor.getString(cursor.getColumnIndex("money")));
                }
            }
        }
        return sum;
    }

    /**
     * 获取剩余预算
     */
    private int getBudgetLeft(){
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("data",MODE_PRIVATE);
        if(!sharedPreferences.getString("budget","0").equals("")){
            return Integer.valueOf(sharedPreferences.getString("budget","0"));
        }
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                Intent addIntent = new Intent(MyApplication.getContext(),AddActivity.class);
                startActivity(addIntent);
                break;
            case R.id.budget_button:
                Intent budgetIntent = new Intent(MyApplication.getContext(),SetBudgetActivity.class);
                startActivity(budgetIntent);
                break;
            case R.id.eye:
                if(count % 2 != 0){
                    monthOutText.setText("***");
                    monthInText.setText("***");
                    budgetLeftText.setText("***");
                }else{
                    monthOutText.setText(String.valueOf(monthOutSum));
                    monthInText.setText(String.valueOf(monthInSum));
                    budgetLeftText.setText(String.valueOf(budgetLeft));
                }
                count++;
                break;
        }
    }
}

