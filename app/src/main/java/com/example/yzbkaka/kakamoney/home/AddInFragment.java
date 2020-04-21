package com.example.yzbkaka.kakamoney.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.yzbkaka.kakamoney.R;

/**
 * Created by yzbkaka on 20-4-20.
 */

public class AddInFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_in,container,false);
        return view;
    }
}
