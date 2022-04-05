package com.example.message_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.message_app.model.Chat;
import com.example.message_app.model.Message;
import com.example.message_app.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout mainLayout;
    private TextView tvLogin;
    private TextInputEditText inputUserName,inputEmail,inputPassword,inputRePassword;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        AnhXa();
        HandleAction();
        mAuth=FirebaseAuth.getInstance();
    }

    private void AnhXa(){
        mainLayout = findViewById(R.id.main_container_register);
        tvLogin=findViewById(R.id.tvLogin);
        inputUserName=findViewById(R.id.inputUserName);
        inputEmail=findViewById(R.id.inputEmail);
        inputPassword=findViewById(R.id.inputPassword);
        inputRePassword=findViewById(R.id.inputRePassword);
        btnSignUp=findViewById(R.id.btnSignUp);
    }


    private void HandleAction(){

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

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveLoginActivity();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    private void Register(){
        String email=inputEmail.getText().toString();
        String pass=inputPassword.getText().toString();

        //Khi tat ca deu hop le
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"Register Success",Toast.LENGTH_LONG).show();
                    mUser=mAuth.getCurrentUser();
                    String userId=mUser.getUid();

                    reference= FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    List<String> list = new ArrayList<String>();
                    list.add("");
                    List<Chat> listChat=new ArrayList<Chat>();
                    listChat.add(new Chat("1","1",new Message("hello","1")));
                    User user=new User(inputUserName.getText().toString(),"default",list,list,listChat);
                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            moveLoginActivity();
                        }
                    });

                }else {
                    Toast.makeText(RegisterActivity.this,"Register faill",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void moveLoginActivity(){
        Intent myIntent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(myIntent);
    }

}