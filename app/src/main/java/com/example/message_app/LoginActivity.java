package com.example.message_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout mainLayout;
    private TextInputEditText txtUserName, txtPassword;
    private Button btnLogin,btnLoginWithGG;
    private TextView tvRegister;
    private Intent  myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);


        mainLayout = findViewById(R.id.main_container);
        init();
        AnhXa();
        HandleAction();
    }

    private void init() {
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                });
                return false;
            }
        });
    }

    private void AnhXa(){
        txtUserName=findViewById(R.id.userName);
        txtPassword=findViewById(R.id.password);
        tvRegister=findViewById(R.id.tvRegister);
        btnLogin=findViewById(R.id.btnLogin);
        btnLoginWithGG=findViewById(R.id.btnLoginWithGG);
    }

    private void HandleAction(){
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  myIntent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(myIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIntent=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
