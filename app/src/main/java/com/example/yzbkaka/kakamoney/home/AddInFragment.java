package com.example.yzbkaka.kakamoney.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by yzbkaka on 20-4-20.
 */

public class AddInFragment extends Fragment implements View.OnClickListener {

    private EditText inText;

    private Button inMoney;
    private Button bonus;
    private Button borrow;
    private Button interest;
    private Button earning;
    private Button investment;
    private Button scrap;
    private Button refund;
    private Button accident;
    private Button inOther;

    private Button time;

    private TextView selectTimeText;

    private EditText inMessage;

    private int inType = Type.OTHER;

    private Calendar calendar;

    private String money;

    private String message;

    private int year,month,day;

    private MyDatabaseHelper databaseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_in,container,false);
        inText = (EditText)view.findViewById(R.id.in_text);
        initTypeButton(view);
        time = (Button)view.findViewById(R.id.out_time);
        time.setOnClickListener(this);
        selectTimeText = (TextView)view.findViewById(R.id.out_select_time_text);
        inMessage = (EditText)view.findViewById(R.id.in_message);
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  //设置时区
        year = calendar.get(Calendar.YEAR);  //获取年份
        month = calendar.get(Calendar.MONTH) + 1;  //获取月份
        day = calendar.get(Calendar.DATE);  //获取日子
        databaseHelper = MyDatabaseHelper.getInstance();
        return view;
    }

    private void initTypeButton(View view){
        inMoney = (Button)view.findViewById(R.id.in_money);
        bonus = (Button)view.findViewById(R.id.bonus);
        borrow = (Button)view.findViewById(R.id.borrow);
        interest = (Button)view.findViewById(R.id.interest);
        earning = (Button)view.findViewById(R.id.earning);
        investment = (Button)view.findViewById(R.id.invesement);
        scrap = (Button)view.findViewById(R.id.scrap);
        refund = (Button)view.findViewById(R.id.refund);
        accident = (Button)view.findViewById(R.id.accident);
        inOther = (Button)view.findViewById(R.id.in_other);

        inMoney.setOnClickListener(this);
        bonus.setOnClickListener(this);
        borrow.setOnClickListener(this);
        interest.setOnClickListener(this);
        earning.setOnClickListener(this);
        investment.setOnClickListener(this);
        scrap.setOnClickListener(this);
        refund.setOnClickListener(this);
        accident.setOnClickListener(this);
        inOther.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
