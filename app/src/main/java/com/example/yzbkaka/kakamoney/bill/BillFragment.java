package com.example.yzbkaka.kakamoney.bill;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ParcelUuid;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.home.AccountAdapter;
import com.example.yzbkaka.kakamoney.home.AlterActivity;
import com.example.yzbkaka.kakamoney.home.SelectTimeActivity;
import com.example.yzbkaka.kakamoney.model.Account;
import com.example.yzbkaka.kakamoney.setting.Function;
import com.example.yzbkaka.kakamoney.setting.MyApplication;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yzbkaka on 20-4-19.
 */

public class BillFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "BillFragment";

    public static final int OUT_BUTTON = 1;

    public static final int IN_BUTTON = 2;

    private Toolbar toolbar;

    private Button billSelectTimeButton;

    private TextView billTimeText;

    private TextView billOutText;

    private TextView billInText;

    private Button showOutButton;

    private Button showInButton;

    private int buttonType = OUT_BUTTON;

    /**
     * 柱形统计图
     */
    private BarChart barChart;

    /**
     * 柱形图上的数据类型
     */
    private BarData barData;

    private RecyclerView recyclerView;

    private AccountAdapter adapter;

    private List<Account> accountList = new ArrayList<>();

    private MyDatabaseHelper databaseHelper;

    private Calendar calendar;

    private int year,month;

    private float sumOut,sumIn;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill,container,false);
        toolbar = (Toolbar)view.findViewById(R.id.bill_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        billSelectTimeButton = (Button)view.findViewById(R.id.bill_select_time_button);
        billSelectTimeButton.setOnClickListener(this);
        billTimeText = (TextView)view.findViewById(R.id.bill_time_text);
        billOutText = (TextView)view.findViewById(R.id.bill_out_text);
        billInText = (TextView)view.findViewById(R.id.bill_in_text);
        showOutButton = (Button)view.findViewById(R.id.show_out_button);
        showOutButton.setOnClickListener(this);
        showInButton = (Button)view.findViewById(R.id.show_in_button);
        showInButton.setOnClickListener(this);
        barChart = (BarChart)view.findViewById(R.id.bar_chart);
        recyclerView = (RecyclerView)view.findViewById(R.id.bill_recycler_view);
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
        initBarChartData();
        barChart.invalidate();  //数据改变，对图表进行刷新
        billTimeText.setText(year + "年" + month + "月" + "账单");
        sumOut = getSumOut();
        sumIn = getSumIn();
        billOutText.setText("￥" + String.valueOf(sumOut));
        billInText.setText("￥" + String.valueOf(sumIn));
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
    private void initBarChartData(){
        List<BarEntry> dataList = new ArrayList<>();  //数据集合
        int daysOfMonth = Function.getDaysOfMonth(month);  //获取该月有多少天
        for(int x = 1;x <= daysOfMonth;x++){
            float y = getYValues(x);  //获得当天的数据
            dataList.add(new BarEntry(x,y));  //向坐标里面添加数据
        }
        BarDataSet barDataSet = null;
        if(buttonType == OUT_BUTTON){
            barDataSet = new BarDataSet(dataList,"支出图");
            barDataSet.setColor(Color.parseColor("#FF595F"));  //设置图表颜色
        }else {
            barDataSet = new BarDataSet(dataList,"收入图");
            barDataSet.setColor(Color.parseColor("#02AE7C"));
        }
        barData = new BarData(barDataSet);
        initBarChart();
    }

    /**
     * 获取当天的金额作为y轴的值
     */
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

    /**
     * 设置图表样式
     */
    private void initBarChart(){
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        XAxis xAxis = barChart.getXAxis();  //获取x轴
        xAxis.setDrawGridLines(false);  //取消垂直线
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  //x轴刻度显示在下方
        xAxis.setLabelCount(3);  //设置x坐标轴刻度
        YAxis rYAxis = barChart.getAxisRight();  //获取y轴
        rYAxis.setDrawAxisLine(false);
        rYAxis.setDrawGridLines(false);  //取消横向网格线
        barChart.setData(barData);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bill_select_time_button:
                Intent intent = new Intent(MyApplication.getContext(),SelectMonthActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.show_out_button:
                buttonType = OUT_BUTTON;
                showInButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                showInButton.setTextColor(Color.parseColor("#00A8E1"));
                showOutButton.setBackgroundColor(Color.parseColor("#00A8E1"));
                showOutButton.setTextColor(Color.parseColor("#FFFFFF"));
                initBarChartData();
                barChart.invalidate();
                break;
            case R.id.show_in_button:
                buttonType = IN_BUTTON;
                showOutButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                showOutButton.setTextColor(Color.parseColor("#00A8E1"));
                showInButton.setBackgroundColor(Color.parseColor("#00A8E1"));
                showInButton.setTextColor(Color.parseColor("#FFFFFF"));
                initBarChartData();
                barChart.invalidate();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    month = data.getIntExtra("select_month",calendar.get(Calendar.MONTH)+1);
                    billTimeText.setText(year + "年" + month + "月" + "账单");
                }
                break;
        }
    }
}
