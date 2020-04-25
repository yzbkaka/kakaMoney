package com.example.yzbkaka.kakamoney.home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.setting.Function;
import com.example.yzbkaka.kakamoney.setting.MyApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

/**
 * Created by yzbkaka on 20-4-20.
 */

public class AddInFragment extends Fragment implements View.OnClickListener {

    private EditText inText;

    private Button salary;
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
        time = (Button)view.findViewById(R.id.in_time);
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

    public void saveIn(){
        money = inText.getText().toString();  //获取输入的消费金额
        message = inMessage.getText().toString();  //获取输入的备注信息
        int isNumber = Function.isNumber(money);
        switch (isNumber){
            case 1:  //如果输入合法
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("money",money);
                contentValues.put("message",message);
                contentValues.put("kind",Type.IN);
                contentValues.put("type",inType);
                contentValues.put("year",year);
                contentValues.put("month",month);
                contentValues.put("day",day);
                sqLiteDatabase.insert("Account",null,contentValues);
                Toast.makeText(MyApplication.getContext(), "存储成功", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                if(!money.equals("")){
                    Toast.makeText(MyApplication.getContext(), "输入错误，存储失败，请重新输入合法金额", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void initTypeButton(View view){
        salary = (Button)view.findViewById(R.id.salary);
        bonus = (Button)view.findViewById(R.id.bonus);
        borrow = (Button)view.findViewById(R.id.borrow);
        interest = (Button)view.findViewById(R.id.interest);
        earning = (Button)view.findViewById(R.id.earning);
        investment = (Button)view.findViewById(R.id.invesement);
        scrap = (Button)view.findViewById(R.id.scrap);
        refund = (Button)view.findViewById(R.id.refund);
        accident = (Button)view.findViewById(R.id.accident);
        inOther = (Button)view.findViewById(R.id.in_other);

        salary.setOnClickListener(this);
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
        switch (view.getId()){
            case R.id.salary:
                setEnable(salary);
                inType = Type.SALARY;
                break;
            case R.id.bonus:
                setEnable(bonus);
                inType = Type.BONUS;
                break;
            case R.id.borrow:
                setEnable(borrow);
                inType = Type.BORROW;
                break;
            case R.id.interest:
                setEnable(interest);
                inType = Type.INTEREST;
                break;
            case R.id.earning:
                setEnable(earning);
                inType = Type.EARNING;
                break;
            case R.id.invesement:
                setEnable(investment);
                inType = Type.INVESEMENT;
                break;
            case R.id.scrap:
                setEnable(scrap);
                inType = Type.SCRAP;
                break;
            case R.id.refund:
                setEnable(refund);
                inType = Type.REFUND;
                break;
            case R.id.accident:
                setEnable(accident);
                inType = Type.ACCIDENT;
                break;
            case R.id.in_other:
                setEnable(inOther);
                inType = Type.IN_OTHER;
                break;
            case R.id.out_time:
                Intent intent = new Intent(MyApplication.getContext(),SelectTimeActivity.class);
                startActivityForResult(intent,2);
                break;
        }
    }

    /**
     * 获取日历返回过来的时间
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
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
            buttonList.add(salary);
            buttonList.add(bonus);
            buttonList.add(borrow);
            buttonList.add(interest);
            buttonList.add(earning);
            buttonList.add(investment);
            buttonList.add(scrap);
            buttonList.add(refund);
            buttonList.add(accident);
            buttonList.add(inOther);
        }
        for (int i = 0; i <buttonList.size() ; i++) {
            buttonList.get(i).setEnabled(true);
        }
        button.setEnabled(false);
    }


}
