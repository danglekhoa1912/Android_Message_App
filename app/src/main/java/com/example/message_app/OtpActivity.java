package com.example.message_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.Console;
import java.io.Writer;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    private EditText inputCode1,inputCode2,inputCode3,inputCode4,inputCode5,inputCode6;
    private TextView textMobile;
    private FirebaseAuth mAuth;
    private Button btnVerify;
    private String verificationID,name,password,mobile;

    private void anhXa(){
        inputCode1=findViewById(R.id.inputCode1);
        inputCode2=findViewById(R.id.inputCode2);
        inputCode3=findViewById(R.id.inputCode3);
        inputCode4=findViewById(R.id.inputCode4);
        inputCode5=findViewById(R.id.inputCode5);
        inputCode6=findViewById(R.id.inputCode6);
        btnVerify=findViewById(R.id.btnVerify);
        textMobile=findViewById(R.id.textMobile);
        textMobile.setText(mobile);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mAuth = FirebaseAuth.getInstance();
        anhXa();
        getValue();
//        sendVerificationCode(textMobile.getText().toString());
        sendVerificationCode(mobile);
        /*PhoneAuthProvider.getInstance().verifyPhoneNumber("+84947549001",
                60,
                TimeUnit.SECONDS,
                OtpActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential){

                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e){

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //super.onCodeSent(s, forceResendingToken);
                        textMobile.setText(s);
                    }
                });
         ;*/
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=inputCode1.getText().toString()+inputCode2.getText().toString()+inputCode3.getText().toString()+inputCode4.getText().toString()+inputCode5.getText().toString()+inputCode6.getText().toString();
//                sendVerificationCode("972257687");
                if(TextUtils.isEmpty(otp)){
                    Toast.makeText(OtpActivity.this,"Mã OTP không chính xác.Vui lòng nhập lại",Toast.LENGTH_LONG).show();
                }
                else{
                    verifycode(otp);
                }
            }
        });
    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+1"+mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)       // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                verifycode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
//            textMobile.setText(s);
            //mVerificationId = s;
            //mResendToken = forceResendingToken;
            verificationID=s;
        }
    };

    private void verifycode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,code);
        signinByCredentials(credential);
        AuthCredential credentialMail = EmailAuthProvider.getCredential(mobile+"@gmail.com", password);
        mAuth.getCurrentUser().linkWithCredential(credentialMail).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("acbt", "linkWithCredential:success");
                    FirebaseUser user = task.getResult().getUser();
                } else {
                    Log.d("acbt", "linkWithCredential:failure", task.getException());
                    Toast.makeText(OtpActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signinByCredentials(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(OtpActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(OtpActivity.this,HomeActivity.class));
                }
            }
        });
    }

    private void getValue(){
        name=getIntent().getStringExtra("name");
        password=getIntent().getStringExtra("pass");
        mobile=getIntent().getStringExtra("mobile").trim();
        Log.d("ad",mobile);
    }
}