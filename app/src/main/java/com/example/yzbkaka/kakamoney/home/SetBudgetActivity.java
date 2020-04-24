package com.example.yzbkaka.kakamoney.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.R;

public class SetBudgetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText budgetEditText;

    private String budget;

    private Button saveBudgetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        budgetEditText = (EditText)findViewById(R.id.budget_edit_text);
        saveBudgetButton = (Button)findViewById(R.id.save_budget_button);
        saveBudgetButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_budget_button:
                budget = budgetEditText.getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("budget",budget);
                editor.apply();
                Toast.makeText(SetBudgetActivity.this, budget, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }
}
