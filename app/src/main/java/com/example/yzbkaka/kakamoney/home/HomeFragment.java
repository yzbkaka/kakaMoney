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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.model.Account;
import com.example.yzbkaka.kakamoney.setting.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Toolbar toolbar;

    /**
     * 本月支出text
     */
    private TextView monthOutText;

    /**
     * 设置可见/不可见
     */
    private Button hideButton;

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

    private FloatingActionButton addButton;

    private int count = 0;

    private MyDatabaseHelper databaseHelper;

    private List<Account> accountList = new ArrayList<>();

    private AccountAdapter adapter;

    private Calendar calendar;

    private int year,month,day;

    /**
     * 本月支出总和
     */
    private float monthOutSum = 0;

    /**
     * 本月收入总和
     */
    private float monthInSum = 0;

    /**
     * 预算剩余
     */
    private float budgetLeft = 0;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar)view.findViewById(R.id.home_toolbar);
        monthOutText = (TextView)view.findViewById(R.id.month_out_text);
        hideButton = (Button)view.findViewById(R.id.hide_button);
        hideButton.setOnClickListener(this);
        monthInText = (TextView)view.findViewById(R.id.month_in_text);
        budgetLeftText = (TextView)view.findViewById(R.id.budget_left_text);
        budgetButton = (Button)view.findViewById(R.id.budget_button);
        budgetButton.setOnClickListener(this);
        recyclerView = (RecyclerView)view.findViewById(R.id.today_recycler_view);
        addButton = (FloatingActionButton)view.findViewById(R.id.add);
        addButton.setOnClickListener(this);
        databaseHelper = MyDatabaseHelper.getInstance();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        adapter = new AccountAdapter(accountList);
        adapter.setOnItemCLickListener(new AccountAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Account account = accountList.get(position);
                Intent intent = new Intent(MyApplication.getContext(),AlterActivity.class);
                intent.putExtra("account",account);
                startActivity(intent);
            }
        });
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        monthOutSum = getMonthOutSum();
        monthOutText.setText(String.valueOf(monthOutSum));
        monthInSum = getMonthInSum();
        monthInText.setText(String.valueOf(monthInSum));
        budgetLeft = getBudgetLeft();
        budgetLeftText.setText(String.valueOf(budgetLeft));
        accountList.clear();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"year=? and month =? and day=?",new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)},null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                Account account = new Account();
                account.setId(cursor.getInt(cursor.getColumnIndex("id")));
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
        LinearLayoutManager manager = new LinearLayoutManager(MyApplication.getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 获取本月总支出
     */
    public float getMonthOutSum(){
        float sum = 0;
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"year=? and month=? and kind=?",new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(Type.OUT)},null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                if(!cursor.getString(cursor.getColumnIndex("money")).equals("")){
                    sum = sum + Float.parseFloat(cursor.getString(cursor.getColumnIndex("money")));
                }
            }
        }
        return sum;
    }

    /**
     * 获取本月总收入
     */
    private float getMonthInSum(){
        float sum = 0;
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"year=? and month=? and kind=?",new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(Type.IN)},null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                if(!cursor.getString(cursor.getColumnIndex("money")).equals("")){
                    sum = sum + Float.parseFloat(cursor.getString(cursor.getColumnIndex("money")));
                }
            }
        }
        return sum;
    }

    /**
     * 获取剩余预算
     */
    private float getBudgetLeft(){
        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("data",MODE_PRIVATE);
        if(!sharedPreferences.getString("budget","0").equals("")){
            return Float.valueOf(sharedPreferences.getString("budget","0")) - monthOutSum;
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
            case R.id.hide_button:
                if(count % 2 == 0){
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

