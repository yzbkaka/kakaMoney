package com.example.yzbkaka.kakamoney.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.setting.MyApplication;

import org.w3c.dom.Text;


public class HomeFragment extends Fragment {

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private Toolbar toolbar;

    private TextView monthSpend;

    private Button eye;

    private TextView monthIncome;

    private TextView monthBudget;

    private RecyclerView recyclerView;

    private FloatingActionButton add;

    private int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar)view.findViewById(R.id.home_toolbar);
        monthSpend = (TextView)view.findViewById(R.id.month_spending);
        eye = (Button)view.findViewById(R.id.eye);
        monthIncome = (TextView)view.findViewById(R.id.month_income);
        monthBudget = (TextView)view.findViewById(R.id.month_budget);
        recyclerView = (RecyclerView)view.findViewById(R.id.today_recycler_view);
        add = (FloatingActionButton)view.findViewById(R.id.add);
        initView();
        return view;
    }

    private void initView(){
        if(count % 2 == 0){

        }else {

        }
        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getContext(),AddActivity.class);
                startActivity(intent);
            }
        });
    }
}

