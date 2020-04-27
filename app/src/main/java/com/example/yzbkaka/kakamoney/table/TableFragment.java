package com.example.yzbkaka.kakamoney.table;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.example.yzbkaka.kakamoney.bill.SelectMonthActivity;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.home.AccountAdapter;
import com.example.yzbkaka.kakamoney.home.AlterActivity;
import com.example.yzbkaka.kakamoney.model.Account;
import com.example.yzbkaka.kakamoney.setting.Function;
import com.example.yzbkaka.kakamoney.setting.MyApplication;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yzbkaka on 20-4-27.
 */

public class TableFragment extends Fragment implements View.OnClickListener {

    public static final int OUT_BUTTON = 1;

    public static final int IN_BUTTON = 2;

    private Toolbar toolbar;

    private Button tableSelectTimeButton;

    private TextView tableTimeText;

    private TextView tableOutText;

    private TextView tableInText;

    private Button showOutButton;

    private Button showInButton;

    private int buttonType = OUT_BUTTON;

    /**
     * 圆饼图
     */
    private PieChart pieChart;

    /**
     * 柱形图上的数据类型
     */
    private PieData pieData;

    private RecyclerView recyclerView;

    private AccountAdapter adapter;

    private List<Account> accountList = new ArrayList<>();

    private MyDatabaseHelper databaseHelper;

    private Calendar calendar;

    private int year,month;

    private float sumOut,sumIn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table,container,false);
        toolbar = (Toolbar)view.findViewById(R.id.table_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        tableSelectTimeButton = (Button)view.findViewById(R.id.table_select_time_button);
        tableSelectTimeButton.setOnClickListener(this);
        tableTimeText = (TextView)view.findViewById(R.id.table_time_text);
        tableOutText = (TextView)view.findViewById(R.id.table_out_text);
        tableInText = (TextView)view.findViewById(R.id.table_in_text);
        showOutButton = (Button)view.findViewById(R.id.table_out_button);
        showOutButton.setOnClickListener(this);
        showInButton = (Button)view.findViewById(R.id.table_in_button);
        showInButton.setOnClickListener(this);
        pieChart = (PieChart)view.findViewById(R.id.pie_chart);
        recyclerView = (RecyclerView)view.findViewById(R.id.table_recycler_view);
        databaseHelper = MyDatabaseHelper.getInstance();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
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
        initPieChartData();
        pieChart.invalidate();  //数据改变，对图表进行刷新
        tableTimeText.setText(year + "年" + month + "月" + "账单");
        sumOut = getSumOut();
        sumIn = getSumIn();
        tableOutText.setText("￥" + String.valueOf(sumOut));
        tableInText.setText("￥" + String.valueOf(sumIn));
        initRecyclerView();
        adapter.notifyDataSetChanged();
    }

    /**
     * 获得当月总支出
     */
    private float getSumOut(){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        float sum = 0;
        Cursor cursor = sqLiteDatabase.query("Account",null,"kind = ? and year = ? and month = ?",new String[]{String.valueOf(Type.OUT),String.valueOf(year),String.valueOf(month)},null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                sum = sum + Float.parseFloat(cursor.getString(cursor.getColumnIndex("money")));
            }
            cursor.close();
        }
        return sum;
    }

    /**
     * 获得当月总收入
     */
    private float getSumIn(){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        float sum = 0;
        Cursor cursor = sqLiteDatabase.query("Account",null,"kind = ? and year = ? and month = ?",new String[]{String.valueOf(Type.IN),String.valueOf(year),String.valueOf(month)},null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                sum = sum + Float.parseFloat(cursor.getString(cursor.getColumnIndex("money")));
            }
            cursor.close();
        }
        return sum;
    }

    /**
     * 从数据库获取数据
     */
    private void initRecyclerView(){
        accountList.clear();
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"year=? and month =?",new String[]{String.valueOf(year), String.valueOf(month)},null,null,null);
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
        manager.setSmoothScrollbarEnabled(true);  //设置流畅滑动
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 设置图表上的数据
     */
    private void initPieChartData(){
        List<PieEntry> dataList = new ArrayList<>();  //数据集合
        int daysOfMonth = Function.getDaysOfMonth(month);  //获取该月有多少天
        for(int x = 1;x <= daysOfMonth;x++){
            float y = getYValues(x);  //获得当天的数据
            dataList.add(new PieEntry(x,y));  //向坐标里面添加数据
        }
        PieDataSet pieDataSet = null;
        if(buttonType == OUT_BUTTON){
            pieDataSet = new PieDataSet(dataList,"支出图");
            pieDataSet.setColor(Color.parseColor("#FF595F"));  //设置图表颜色
        }else {
            pieDataSet = new PieDataSet(dataList,"收入图");
            pieDataSet.setColor(Color.parseColor("#02AE7C"));
        }
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        //initPieChart();
    }

    private float getYValues(int x){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = null;
        if(buttonType == OUT_BUTTON){
            cursor = sqLiteDatabase.query("Account",null,"kind = ? and year = ? and month = ? and day = ?",new String[]{String.valueOf(Type.OUT),String.valueOf(year),String.valueOf(month),String.valueOf(x)},null,null,null);
        }else{
            cursor = sqLiteDatabase.query("Account",null,"kind = ? and year = ? and month = ? and day = ?",new String[]{String.valueOf(Type.IN),String.valueOf(year),String.valueOf(month),String.valueOf(x)},null,null,null);
        }
        float sum = 0;
        if(cursor != null){
            while (cursor.moveToNext()){
                sum = sum + Float.parseFloat(cursor.getString(cursor.getColumnIndex("money")));
            }
            cursor.close();
        }
        return sum;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.table_select_time_button:
                Intent intent = new Intent(MyApplication.getContext(),SelectMonthActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.table_out_button:
                buttonType = OUT_BUTTON;
                showInButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                showInButton.setTextColor(Color.parseColor("#00A8E1"));
                showOutButton.setBackgroundColor(Color.parseColor("#00A8E1"));
                showOutButton.setTextColor(Color.parseColor("#FFFFFF"));
                initPieChartData();
                pieChart.invalidate();
                break;
            case R.id.table_in_button:
                buttonType = IN_BUTTON;
                showOutButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                showOutButton.setTextColor(Color.parseColor("#00A8E1"));
                showInButton.setBackgroundColor(Color.parseColor("#00A8E1"));
                showInButton.setTextColor(Color.parseColor("#FFFFFF"));
                initPieChartData();
                pieChart.invalidate();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    month = data.getIntExtra("select_month",calendar.get(Calendar.MONTH)+1);
                    tableTimeText.setText(year + "年" + month + "月" + "账单");
                }
                break;
        }
    }
}
