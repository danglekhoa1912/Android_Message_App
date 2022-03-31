package com.example.message_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        AnhXa();
        HandleAction();
    }

    private void AnhXa(){
        tvLogin=findViewById(R.id.tvLogin);

    }
    private void HandleAction(){
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }
}