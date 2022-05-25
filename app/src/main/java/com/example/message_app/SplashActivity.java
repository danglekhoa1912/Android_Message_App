package com.example.message_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;


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

        if(getSetting("lang")!=null){
            changeLanguage(getSetting("lang"));
        }
        else changeLanguage("vi");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mUser!=null&&mUser.isEmailVerified()){
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

    public void changeLanguage(String language){
        Locale locale=new Locale(language);
        Configuration config= new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(
                config,
                getBaseContext().getResources().getDisplayMetrics()
        );
    }

    public String getSetting(String key){
        SharedPreferences preferences =getSharedPreferences("MySetting", Context.MODE_PRIVATE);
            String data=preferences.getString(key,null);
            return data;
    }
}