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

public class SetBudgetActivity extends AppCompatActivity {

    private EditText budgetEditText;

    private String budget;

    private Button saveBudgetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_budget);
        budgetEditText = (EditText)findViewById(R.id.budget_edit_text);
        saveBudgetButton = (Button)findViewById(R.id.save_budget);
        budget = String.valueOf(budgetEditText.getText());
        saveBudgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("budget",budget);
                editor.apply();
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
