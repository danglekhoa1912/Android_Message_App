package com.example.message_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
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

import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout mainLayout;
    private TextView tvLogin,lblErrorMoblie;
    private TextInputEditText inputMoblie,inputUserName,inputEmail,inputPassword,inputRePassword;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static int checkUsername(String s){
        s=unAccent(s);
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        if (s.indexOf(" ")!=-1){ //Chuoi co dau cach
            return 1;
        }
        else if (m.find()){ //Chuoi co ki tu dac biet
            return 2;
        }
        else if (s.length()>20){ //Chuoi dai qua 20 ki tu
            return 3;
        }
        return 0; // Chuoi hop le (khong thuộc 3 đk trên)
    }
    public static boolean checkPassword(String password)
    {
        if(password.length()>=8)
        {
            return password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$!~/?,<>%^&+=])(?=\\S+$).{8,}");

        }
        else
            return false;

    }

    public boolean validBirthday(LocalDate birthday){
        int age= 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            age = LocalDate.now().getYear()-birthday.getYear();
        }
        if(age>=18&&age<=130)
            return true;
        return false;
    }
    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        // return pattern.matcher(temp).replaceAll("");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
    }

    public static boolean validMobile(String mb, TextInputEditText $mobile){
        if (mb.indexOf("00")==0){
            return false;
        }
        else if (mb.indexOf("0")==0){
            mb=mb.substring(1);
            $mobile.setText(mb);
            return validMobile(mb,$mobile);
        }
        else if (mb.length()==9){
            return mb.matches("-?\\d+?");
        }

        return false;
    }

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
        inputMoblie=findViewById(R.id.inputMoblie);
        lblErrorMoblie=findViewById(R.id.lblErrorMoblie);
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

        inputMoblie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                lblErrorMoblie.setText(inputMoblie.getText());
            }

            @Override
            public void afterTextChanged(Editable editable) {

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