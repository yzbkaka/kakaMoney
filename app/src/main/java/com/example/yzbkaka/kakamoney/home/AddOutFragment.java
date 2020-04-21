package com.example.yzbkaka.kakamoney.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.setting.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzbkaka on 20-4-20.
 */

public class AddOutFragment extends Fragment implements View.OnClickListener {

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

    private EditText outMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_out,container,false);
        outText = (EditText)view.findViewById(R.id.out_text);
        initTypeButton(view);
        time = (Button)view.findViewById(R.id.out_time);
        outMessage = (EditText)view.findViewById(R.id.out_message);
        return view;
    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.food:
                setEnable(food);
                Toast.makeText(MyApplication.getContext(), "food", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bus:
                setEnable(bus);
                break;
            case R.id.fun:
                setEnable(fun);
                break;
            case R.id.connect:
                setEnable(connect);
                break;
            case R.id.house:
                setEnable(house);
                break;
            case R.id.education:
                setEnable(education);
                break;
            case R.id.aid:
                setEnable(aid);
                break;
            case R.id.trip:
                setEnable(trip);
                break;
            case R.id.lend:
                setEnable(lend);
                break;
            case R.id.other:
                setEnable(other);
                break;

        }
    }

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
