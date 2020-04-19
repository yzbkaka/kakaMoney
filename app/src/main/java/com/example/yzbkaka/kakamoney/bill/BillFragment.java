package com.example.yzbkaka.kakamoney.bill;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yzbkaka.kakamoney.R;

/**
 * Created by yzbkaka on 20-4-19.
 */

public class BillFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bill,container,false);
        return view;
    }
}
