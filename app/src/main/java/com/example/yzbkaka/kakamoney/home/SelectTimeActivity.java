package com.example.yzbkaka.kakamoney.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yzbkaka.kakamoney.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.Calendar;

public class SelectTimeActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        calendarView = (MaterialCalendarView)findViewById(R.id.calendar_view);
        calendar = Calendar.getInstance();
        final int nowYear = calendar.get(Calendar.YEAR);
        final int nowMonth = calendar.get(Calendar.MONTH) + 1;
        final int nowDay = calendar.get(Calendar.DATE);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int year = date.getYear();
                int month = date.getMonth()+1;
                int day = date.getDay();
                Intent intent = new Intent();
                intent.putExtra("year",year);
                intent.putExtra("month",month);
                intent.putExtra("day",day);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
