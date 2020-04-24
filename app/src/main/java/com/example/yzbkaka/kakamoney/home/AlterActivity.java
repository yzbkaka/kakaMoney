package com.example.yzbkaka.kakamoney.home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yzbkaka.kakamoney.R;
import com.example.yzbkaka.kakamoney.Type;
import com.example.yzbkaka.kakamoney.db.MyDatabaseHelper;
import com.example.yzbkaka.kakamoney.model.Account;

public class AlterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView alterTypeImage;

    private TextView alterTypeName;

    private EditText alterMoneyEditText;

    private EditText alterMessageEditText;

    private Button alterTimeButton;

    private TextView alterTimeText;

    private Button alterDeleteButton;

    private Button alterSaveButton;

    private MyDatabaseHelper databaseHelper;

    private SQLiteDatabase sqLiteDatabase;

    private Account account;

    private int year,month,day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter);
        alterTypeImage = (ImageView)findViewById(R.id.alter_type_image);
        alterTypeName = (TextView) findViewById(R.id.alter_type_name);
        alterMoneyEditText = (EditText)findViewById(R.id.alter_money_edit_text);
        alterMessageEditText = (EditText)findViewById(R.id.alter_message_edit_text);
        alterTimeButton = (Button)findViewById(R.id.alter_time_button);
        alterTimeButton.setOnClickListener(this);
        alterTimeText = (TextView) findViewById(R.id.alter_time_text);
        alterDeleteButton = (Button)findViewById(R.id.alter_delete_button);
        alterDeleteButton.setOnClickListener(this);
        alterSaveButton = (Button)findViewById(R.id.alter_save_button);
        alterSaveButton.setOnClickListener(this);
        databaseHelper = MyDatabaseHelper.getInstance();
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        initView();
    }

    /**
     * 显示之前已经保存的数据
     */
    private void initView(){
        Intent intent = getIntent();
        account = (Account) intent.getSerializableExtra("account");
        alterTypeImage.setImageResource(Type.TYPE_IMAGE[account.getType()]);
        alterTypeName.setText(Type.TYPE_NAME[account.getType()]);
        alterMoneyEditText.setText(account.getMoney());
        alterMessageEditText.setText(account.getMessage());
        year = account.getYear();
        month = account.getMonth();
        day = account.getDay();
        alterTimeText.setText(year + "年" + month + "月" + day + "日");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.alter_time_button:
                Intent intent = new Intent(this,SelectTimeActivity.class);
                startActivityForResult(intent,2);
                break;
            case R.id.alter_delete_button:
                sqLiteDatabase.delete("Account","id = ?",new String[]{String.valueOf(account.getId())});
                finish();
                break;
            case R.id.alter_save_button:
                String money = alterMoneyEditText.getText().toString();
                String message = alterMessageEditText.getText().toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put("money",money);
                contentValues.put("message",message);
                contentValues.put("year",year);
                contentValues.put("month",month);
                contentValues.put("day",day);
                sqLiteDatabase.update("Account",contentValues,"id = ?",new String[]{String.valueOf(account.getId())});
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 2:
                if(resultCode == RESULT_OK){
                    year = data.getIntExtra("year",account.getYear());
                    month = data.getIntExtra("month",account.getMonth());
                    day = data.getIntExtra("day",account.getDay());
                    alterTimeText.setText(year + "年" + month + "月" + day + "日");
                }
        }
    }
}
