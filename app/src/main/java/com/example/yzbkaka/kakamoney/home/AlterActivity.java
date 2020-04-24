package com.example.yzbkaka.kakamoney.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yzbkaka.kakamoney.R;

public class AlterActivity extends AppCompatActivity {

    private ImageView alterTypeImage;

    private TextView alterTypeName;

    private EditText alterMoneyEditText;

    private EditText alterMessageEditText;

    private Button alterTimeButton;

    private EditText alterTimeEditText;

    private Button alterDeleteButton;

    private Button alterSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter);
        alterTypeImage = (ImageView)findViewById(R.id.alter_type_image);
        alterTypeName = (TextView) findViewById(R.id.alter_type_name);
        alterMoneyEditText = (EditText)findViewById(R.id.alter_money_edit_text);
        alterMessageEditText = (EditText)findViewById(R.id.alter_message_edit_text);
        alterTimeButton = (Button)findViewById(R.id.alter_time_button);
        alterTimeEditText = (EditText) findViewById(R.id.alter_time_edit_text);
        alterDeleteButton = (Button)findViewById(R.id.alter_delete_button);
        alterSaveButton = (Button)findViewById(R.id.alter_save_button);
    }
}
