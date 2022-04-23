package com.example.message_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private LinearLayout mainLayout;
    private TextInputEditText inputEmail, inputPassword;
    private Button btnLogin, btnLoginWithGG;
    private TextView tvRegister;
    private Intent myIntent;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        AnhXa();
        init();
        HandleAction();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


    }

    private void init() {
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
                            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        }
                    }
                });
                return false;
            }
        });
    }

    private void AnhXa() {
        mainLayout = findViewById(R.id.main_container_login);
        inputEmail = findViewById(R.id.inputEmailLogin);
        inputPassword = findViewById(R.id.inputPasswordLogin);
        tvRegister = findViewById(R.id.tvRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginWithGG = findViewById(R.id.btnLoginWithGG);
    }

    private void HandleAction() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myIntent = new Intent(LoginActivity.this, OtpActivity.class);
                startActivity(myIntent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }

    private void Login() {
        String email = inputEmail.getText().toString();
        String pass = inputPassword.getText().toString();
        //Khi tat ca deu hop le
        if(!email.isEmpty()&&!pass.isEmpty()){
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                        myIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

}
