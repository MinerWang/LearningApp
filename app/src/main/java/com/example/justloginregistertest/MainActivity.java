package com.example.justloginregistertest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText liEd;

    private DBOpenHelper mDBOpenHelper;
    ArrayList<User> list;
    private String trueName;
    private LinearLayout lil;
    private TextView ll1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=new ArrayList<User>();
        initView();
        mDBOpenHelper = new DBOpenHelper(this);
    }

    private void initView() {
        // 初始化控件对象
        Button mBtMainLogout = findViewById(R.id.bt_main_logout);
        Button libt =findViewById(R.id.libt);
        liEd = findViewById(R.id.lieditText);
        Button Btaudio = findViewById(R.id.bt_audio);
        Button Btvidio = findViewById(R.id.bt_vidio);
        Button Btcamera = findViewById(R.id.camerabt);
        // 绑定点击监听器

        mBtMainLogout.setOnClickListener(this);
        libt.setOnClickListener(this);
        Btaudio.setOnClickListener(this);
        Btvidio.setOnClickListener(this);
        Btcamera.setOnClickListener(this);
         lil=(LinearLayout) findViewById(R.id.lilinearLayout);
         ll1 = findViewById(R.id.tte);
    }

    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.bt_main_logout:
                Intent intent = new Intent(this, loginActivity.class);
                startActivity(intent);
             //   finish();

            case R.id.bt_audio:
                Intent intent1 = new Intent(this, playaudio.class);
                startActivity(intent1);
              //  finish();
            case R.id.bt_vidio:
                Intent intent2 = new Intent(this, playvideo.class);
                startActivity(intent2);
                //finish();
            case R.id.camerabt:
                Intent intent3 = new Intent(this, cameraActivity.class);
                startActivity(intent3);
                //finish();
            case R.id.libt:

                trueName = liEd.getText().toString().trim();

                if (!TextUtils.isEmpty(trueName)) {
                    ArrayList<User> data = mDBOpenHelper.getAllData(trueName);
                     //显示出查询的姓的数据表的那一行信息
                        for(int i = 0;i<data.size();i++){
                            TextView tv=new TextView(this);
                            tv.setText(data.get(i).toString());
                            tv.setTextSize(20);
                            lil.addView(tv);
                        }
                } else {
                    Toast.makeText(this, "请输入姓氏", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }


}
