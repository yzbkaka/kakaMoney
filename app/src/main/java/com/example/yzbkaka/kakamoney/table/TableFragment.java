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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.bill.SelectMonthActivity;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.home.AccountAdapter;
import com.example.yzbkaka.kakamoney.home.AlterActivity;
import com.example.yzbkaka.kakamoney.model.Account;
import com.example.yzbkaka.kakamoney.model.Table;
import com.example.yzbkaka.kakamoney.setting.Function;
import com.example.yzbkaka.kakamoney.setting.MyApplication;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.w3c.dom.Text;

import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yzbkaka on 20-4-27.
 */

public class TableFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "TableFragment";

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

    private PieData pieData;

    private RecyclerView recyclerView;

    private TableAdapter adapter;

    /**
     * 类别和总金额的映射
     */
    private Map<Integer,Float> map = new HashMap<>();

    private List<Table> tableList = new ArrayList<>();

    private List<Integer> colorList = new ArrayList<>();

    private MyDatabaseHelper databaseHelper;

    private Calendar calendar;

    private int year,month;

    private float sumOut,sumIn;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table,container,false);
        toolbar = (Toolbar)view.findViewById(R.id.table_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("报表");
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
        adapter = new TableAdapter();
        databaseHelper = MyDatabaseHelper.getInstance();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        colorList = getColorList();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecyclerView();
        adapter.notifyDataSetChanged();
        sumOut = getSumOut();
        sumIn = getSumIn();
        initPieChartData();
        pieChart.invalidate();  //数据改变，对图表进行刷新
        tableTimeText.setText(year + "年" + month + "月" + "账单");
        tableOutText.setText("￥" + String.valueOf(sumOut));
        tableInText.setText("￥" + String.valueOf(sumIn));

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
        tableList.clear();
        map.clear();  //map数据清空
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = null;
        if (buttonType == OUT_BUTTON){
            cursor = sqLiteDatabase.query("Account",null,"kind = ? and year=? and month =?",new String[]{String.valueOf(Type.OUT),String.valueOf(year), String.valueOf(month)},null,null,null);
        }else{
            cursor = sqLiteDatabase.query("Account",null,"kind = ? and year=? and month =?",new String[]{String.valueOf(Type.IN),String.valueOf(year), String.valueOf(month)},null,null,null);
        }
        if(cursor != null){
            while (cursor.moveToNext()){
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                float money = Float.valueOf(cursor.getString(cursor.getColumnIndex("money")));
                if(map.containsKey(type)){
                    map.put(type,map.get(type)+money);
                }else{
                    map.put(type,money);
                }
            }
            cursor.close();
        }

        for(Map.Entry<Integer,Float> entry : map.entrySet()){
            Table table = new Table();
            table.setTableType(entry.getKey());
            table.setTableSum(entry.getValue());
            tableList.add(table);
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
        PieDataSet pieDataSet = null;
        if(buttonType == OUT_BUTTON){
            Log.d(TAG, "initPieChartData: " + "执行out");
            for(int i = 0;i < tableList.size();i++){
                float percent = tableList.get(i).getTableSum();
                String typeName = Type.TYPE_NAME[tableList.get(i).getTableType()];
                Log.d(TAG, "percent:" + percent + " " + "typeName:" + typeName);
                dataList.add(new PieEntry(percent,typeName));
            }
            pieDataSet = new PieDataSet(dataList,"支出图");
            pieDataSet.setColors(colorList);
        }else {
            for(int i = 0;i < tableList.size();i++){
                float percent = tableList.get(i).getTableSum();
                String typeName = Type.TYPE_NAME[tableList.get(i).getTableType()];
                dataList.add(new PieEntry(percent,typeName));
            }
            pieDataSet = new PieDataSet(dataList,"收入图");
            pieDataSet.setColors(colorList);
        }
        pieData = new PieData(pieDataSet);
        initPieChart(pieDataSet);
    }

    /**
     * 设置饼图样式
     */
    private void initPieChart(PieDataSet pieDataSet){
        Description description = new Description();  //取消右下角文字
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setTransparentCircleRadius(0f);  //设置半透明圆环的半径, 0为透明
        pieChart.setRotationAngle(-15);  ////设置初始旋转角度
        pieDataSet.setValueLinePart1OffsetPercentage(80f);   //数据连接线距图形片内部边界的距离，为百分数
        pieDataSet.setValueLineColor(Color.LTGRAY);  //设置连接线的颜色
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);  //连接线在饼状图外面
        pieDataSet.setSliceSpace(1f);  // 设置饼块之间的间隔
        pieDataSet.setHighlightEnabled(true);
        //设置半透明圆环的半径, 0为透明
        pieChart.setTransparentCircleRadius(0f);

        //设置初始旋转角度
        pieChart.setRotationAngle(-15);

        //数据连接线距图形片内部边界的距离，为百分数
        pieDataSet.setValueLinePart1OffsetPercentage(80f);

        //设置连接线的颜色
        pieDataSet.setValueLineColor(Color.LTGRAY);
        // 连接线在饼状图外面
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        // 设置饼块之间的间隔
        pieDataSet.setSliceSpace(1f);
        pieDataSet.setHighlightEnabled(true);

        pieChart.setEntryLabelColor(Color.parseColor("#000000"));  //设置类别文字
        pieChart.setEntryLabelTextSize(7.5f);

        // 设置piecahrt图表点击Item高亮是否可用
        pieChart.setHighlightPerTapEnabled(true);

        // 绘制内容value，设置字体颜色大小
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(11f);
        pieData.setValueTextColor(Color.DKGRAY);
        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
    }

    /**
     * 设置饼图颜色
     */
    private List<Integer> getColorList(){
        List<Integer> list = new ArrayList<>();
        list.add(Color.parseColor("#8CEBFF"));
        list.add(Color.parseColor("#C5FF8C"));
        list.add(Color.parseColor("#FFF78C"));
        list.add(Color.parseColor("#FFD28C"));
        list.add(Color.parseColor("#19CAAD"));
        list.add(Color.parseColor("#8CC7B5"));
        list.add(Color.parseColor("#ECAD9E"));
        list.add(Color.parseColor("#FF595F"));
        list.add(Color.parseColor("#02AE7C"));
        list.add(Color.parseColor("#D1BA74"));
        return list;
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
                initRecyclerView();
                initPieChartData();
                pieChart.invalidate();
                break;
            case R.id.table_in_button:
                buttonType = IN_BUTTON;
                showOutButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
                showOutButton.setTextColor(Color.parseColor("#00A8E1"));
                showInButton.setBackgroundColor(Color.parseColor("#00A8E1"));
                showInButton.setTextColor(Color.parseColor("#FFFFFF"));
                initRecyclerView();
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


    /**
     * 内部类，RecyclerVIew适配器
     */
    class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder>{

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView typeImage;
            TextView typeName;
            TextView typeNumber;
            TextView money;

            public ViewHolder(View view) {
                super(view);
                typeImage = (ImageView)view.findViewById(R.id.table_type_image);
                typeName = (TextView)view.findViewById(R.id.table_type_name);
                typeNumber = (TextView)view.findViewById(R.id.table_number);
                money = (TextView)view.findViewById(R.id.table_money);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.table_recycler_view_item,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Table table = tableList.get(position);
            if(buttonType == OUT_BUTTON){
                holder.typeImage.setImageResource(Type.TYPE_IMAGE[table.getTableType()]);
                holder.typeName.setText(Type.TYPE_NAME[table.getTableType()]);
                holder.typeNumber.setText(String.valueOf( (float)Math.round((table.getTableSum()/sumOut)*100*10)/10) + "%");
                holder.money.setText("￥" + String.valueOf(table.getTableSum()));
                holder.money.setTextColor(Color.parseColor("#FF595F"));
            }else{
                holder.typeImage.setImageResource(Type.TYPE_IMAGE[table.getTableType()]);
                holder.typeName.setText(Type.TYPE_NAME[table.getTableType()]);
                holder.typeNumber.setText(String.valueOf(String.valueOf( (float)Math.round((table.getTableSum()/sumIn)*100*10)/10) + "%"));
                holder.money.setText("￥" + String.valueOf(table.getTableSum()));
                holder.money.setTextColor(Color.parseColor("#02AE7C"));
            }


        }

        @Override
        public int getItemCount() {
            return tableList.size();
        }
    }

}
