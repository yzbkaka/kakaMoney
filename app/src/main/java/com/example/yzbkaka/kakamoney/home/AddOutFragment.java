package com.example.yzbkaka.kakamoney.home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.setting.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yzbkaka on 20-4-20.
 */

public class AddOutFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "AddOutFragment";

    private EditText outText;

    private Button food;
    private Button bus;
    private Button fun;
    private Button connect;
    private Button house;
    private Button education;
    private Button aid;
    private Button trip;
    private Button lend;
    private Button other;

    private Button time;

    private TextView selectTimeText;

    private EditText outMessage;

    private int outType = Type.OTHER;

    private Calendar calendar;

    private String money;

    private String message;

    private int year,month,day;

    private MyDatabaseHelper databaseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_out,container,false);
        outText = (EditText)view.findViewById(R.id.out_text);
        initTypeButton(view);
        time = (Button)view.findViewById(R.id.out_time);
        time.setOnClickListener(this);
        selectTimeText = (TextView)view.findViewById(R.id.out_select_time_text);
        outMessage = (EditText)view.findViewById(R.id.out_message);
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  //设置时区
        year = calendar.get(Calendar.YEAR);  //获取年份
        month = calendar.get(Calendar.MONTH) + 1;  //获取月份
        day = calendar.get(Calendar.DATE);  //获取日子
        databaseHelper = MyDatabaseHelper.getInstance();
        return view;
    }

    /**
     * 初始化消费类型button
     */
    private void initTypeButton(View view){
        food = (Button)view.findViewById(R.id.food);
        bus = (Button)view.findViewById(R.id.bus);
        fun = (Button)view.findViewById(R.id.fun);
        connect = (Button)view.findViewById(R.id.connect);
        house = (Button)view.findViewById(R.id.house);
        education = (Button)view.findViewById(R.id.education);
        aid = (Button)view.findViewById(R.id.aid);
        trip = (Button)view.findViewById(R.id.trip);
        lend = (Button)view.findViewById(R.id.lend);
        other = (Button)view.findViewById(R.id.other);

        food.setOnClickListener(this);
        bus.setOnClickListener(this);
        fun.setOnClickListener(this);
        connect.setOnClickListener(this);
        house.setOnClickListener(this);
        education.setOnClickListener(this);
        aid.setOnClickListener(this);
        trip.setOnClickListener(this);
        lend.setOnClickListener(this);
        other.setOnClickListener(this);
    }

    /**
     * 将金额、消费类型、备注和日期存储到数据库
     */
    public void saveOut(){
        /*while (outType == -1){
            Toast.makeText(MyApplication.getContext(), "请选择流出类型", Toast.LENGTH_SHORT).show();

        }*/
        money = outText.getText().toString();  //获取输入的消费金额
        Log.d(TAG, "money1: " + money);
        message = outMessage.getText().toString();  //获取输入的备注信息
        Log.d(TAG, "message1: " + message);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("money",money);
        contentValues.put("message",message);
        contentValues.put("kind",Type.OUT);
        contentValues.put("type",outType);
        contentValues.put("year",year);
        contentValues.put("month",month);
        contentValues.put("day",day);
        sqLiteDatabase.insert("Account",null,contentValues);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.food:
                setEnable(food);
                outType = Type.FOOD;
                break;
            case R.id.bus:
                setEnable(bus);
                outType = Type.BUS;
                break;
            case R.id.fun:
                setEnable(fun);
                outType = Type.FUN;
                break;
            case R.id.connect:
                setEnable(connect);
                outType = Type.CONNECT;
                break;
            case R.id.house:
                setEnable(house);
                outType = Type.HOUSE;
                break;
            case R.id.education:
                setEnable(education);
                outType = Type.EDUCATION;
                break;
            case R.id.aid:
                setEnable(aid);
                outType = Type.AID;
                break;
            case R.id.trip:
                setEnable(trip);
                outType = Type.TRIP;
                break;
            case R.id.lend:
                setEnable(lend);
                outType = Type.LEND;
                break;
            case R.id.other:
                setEnable(other);
                outType = Type.OTHER;
                break;
            case R.id.out_time:
                Intent intent = new Intent(MyApplication.getContext(),SelectTimeActivity.class);
                startActivityForResult(intent,1);

        }
    }

    /**
     * 获取日历返回过来的时间
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    year = data.getIntExtra("year",calendar.get(Calendar.YEAR));
                    month = data.getIntExtra("month",calendar.get(Calendar.MONTH)+1);
                    day = data.getIntExtra("day",calendar.get(Calendar.DATE));
                    selectTimeText.setText(year + "年" + month + "月" + day + "日");
                }
                break;
        }
    }

    /**
     * 实现点击一个button，其他button颜色恢复
     */
    private void setEnable(Button button){
        List<Button> buttonList = new ArrayList<>();
        if(buttonList.size() == 0){
            buttonList.add(food);
            buttonList.add(bus);
            buttonList.add(fun);
            buttonList.add(connect);
            buttonList.add(house);
            buttonList.add(education);
            buttonList.add(aid);
            buttonList.add(trip);
            buttonList.add(lend);
            buttonList.add(other);
        }
        for (int i = 0; i <buttonList.size() ; i++) {
            buttonList.get(i).setEnabled(true);
        }
        button.setEnabled(false);
    }
}

