package com.example.yzbkaka.kakamoney;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerName;

    private EditText registerPassword;

    private Button registerButton;

    private String name = "";

    private String password = "";

    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerName = (EditText)findViewById(R.id.register_name);
        registerPassword = (EditText)findViewById(R.id.register_password);
        registerButton = (Button)findViewById(R.id.register_button);
        databaseHelper = MyDatabaseHelper.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = registerName.getText().toString();
                password = registerPassword.getText().toString();
                if(!name.equals("") && !password.equals("")) {
                    SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    Log.e("yzbkaka",name + "||" + password);
                    contentValues.put("name",name);
                    contentValues.put("password",password);
                    sqLiteDatabase.insert("User",null,contentValues);
                }else {
                    Toast.makeText(RegisterActivity.this, "输入错误！", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}

