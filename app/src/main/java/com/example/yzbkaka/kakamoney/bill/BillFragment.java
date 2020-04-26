package com.example.yzbkaka.kakamoney.bill;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.home.AccountAdapter;
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

/**
 * Created by yzbkaka on 20-4-19.
 */

public class BillFragment extends Fragment {

    private static final String TAG = "BillFragment";

    private Toolbar toolbar;

    private TextView billTimeText;

    private TextView billOutText;

    private TextView billInText;

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

    private List<Account> accountList;

    private MyDatabaseHelper databaseHelper;

    private Calendar calendar;

    private int year,month,day;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bill,container,false);
        toolbar = (Toolbar)view.findViewById(R.id.bill_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        billTimeText = (TextView)view.findViewById(R.id.bill_time_text);
        billOutText = (TextView)view.findViewById(R.id.bill_out_text);
        billInText = (TextView)view.findViewById(R.id.bill_in_text);
        barChart = (BarChart)view.findViewById(R.id.bar_chart);
        recyclerView = (RecyclerView)view.findViewById(R.id.bill_recycler_view);
        databaseHelper = MyDatabaseHelper.getInstance();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        adapter = new AccountAdapter(accountList);
        initBarChartData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        barChart.setNoDataText("暂无数据");  //设置没有数据是显示的文字
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
        BarDataSet barDataSet = new BarDataSet(dataList,"柱形图");
        barData = new BarData(barDataSet);
        initBarChart();
    }

    /**
     * 获取当天的金额作为y轴的值
     */
    private float getYValues(int x){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("Account",null,"kind = ? and year = ? and month = ? and day = ?",new String[]{String.valueOf(Type.OUT),String.valueOf(year),String.valueOf(month),String.valueOf(x)},null,null,null);
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
        description.setText("支出图");
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

}
