package com.example.message_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnGetApi;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        AnhXa();

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        btnGetApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent myIntent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }

    private void AnhXa(){
        tvUserName=findViewById(R.id.tvuserName);
        btnGetApi=findViewById(R.id.btnGetApi);
    }



}