package com.example.message_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnGetApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        AnhXa();
        btnGetApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void AnhXa(){
        tvUserName=findViewById(R.id.tvuserName);
        btnGetApi=findViewById(R.id.btnGetApi);
    }



}