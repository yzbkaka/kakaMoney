package com.example.yzbkaka.kakamoney.bill;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.yzbkaka.kakamoney.R;
import com.prolificinteractive.materialcalendarview.CalendarMode;

import java.util.Calendar;

public class SelectMonthActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    private Button selectMonthButton;

    private Calendar calendar;

    private int month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_month);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        selectMonthButton = (Button)findViewById(R.id.select_month_button);
        calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH) + 1;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                RadioButton radioButton = (RadioButton)findViewById(id);
                String text = radioButton.getText().toString();
                if(text.length() == 2){
                    month = Integer.valueOf(text.substring(0,1));
                }else if(text.length() == 3){
                    month = Integer.valueOf(text.substring(0,2));
                }
                Intent intent = new Intent();
                intent.putExtra("select_month",month);
                setResult(RESULT_OK,intent);
            }
        });
        selectMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
