package com.example.yzbkaka.kakamoney;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText userName;

    private EditText userPassword;

    private Button loginButton;

    private TextView registerButton;

    private String name = "";

    private String password = "";

    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userName = (EditText)findViewById(R.id.user_name);
        userPassword = (EditText)findViewById(R.id.user_password);
        loginButton = (Button) findViewById(R.id.login);
        registerButton = (TextView)findViewById(R.id.register);
        myDatabaseHelper = MyDatabaseHelper.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = userName.getText().toString();
                password = userPassword.getText().toString();
                if(!name.equals("") && !password.equals("")) {
                    String getName = "";
                    String getPassword = "";
                    SQLiteDatabase sqLiteDatabase = myDatabaseHelper.getWritableDatabase();
                    Cursor cursor = sqLiteDatabase.query("User",null,null,null,null,null,null);
                    if(cursor.moveToFirst()) {
                        do {
                            getName = cursor.getString(cursor.getColumnIndex("name"));
                            getPassword = cursor.getString(cursor.getColumnIndex("password"));
                            if(name.equals(getName) && password.equals(getPassword)) {
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                cursor.close();
                                finish();
                            }
                        }while(cursor.moveToNext());
                    }
                    cursor.close();
                    Toast.makeText(LoginActivity.this, "输入错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
