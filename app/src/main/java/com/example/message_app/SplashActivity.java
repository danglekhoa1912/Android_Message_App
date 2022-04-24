package com.example.message_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private Intent  myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mUser!=null){
                      myIntent=new Intent(SplashActivity.this,HomeActivity.class);
                }
                else {
                      myIntent=new Intent(SplashActivity.this,LoginActivity.class);
                }

                startActivity(myIntent);
                finish();
            }
        },2000);
    }
}