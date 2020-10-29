package com.example.yzbkaka.kakamoney;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class RecommendActivity extends AppCompatActivity {

    private LinearLayout one;

    private LinearLayout two;

    private LinearLayout three;

    private LinearLayout four;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        one = (LinearLayout)findViewById(R.id.one);
        two = (LinearLayout)findViewById(R.id.two);
        three = (LinearLayout)findViewById(R.id.four);
        four = (LinearLayout)findViewById(R.id.four);
    }

    @Override
    protected void onResume() {
        super.onResume();
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendActivity.this,WebActivity.class);
                startActivity(intent);
                finish();
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendActivity.this,WebActivity.class);
                startActivity(intent);
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendActivity.this,WebActivity.class);
                startActivity(intent);
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendActivity.this,WebActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RecommendActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
