package com.example.message_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private LinearLayout mainLayout;
    private TextView tvLogin;
    private TextInputEditText inputMoblie,inputUserName,inputEmail,inputPassword,inputRePassword;
    private Button btnSignUp,btnDate;
    private EditText birthday;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference reference;
    private int lastSelectedYear=2000;
    private int lastSelectedMonth;
    private int lastSelectedDayOfMonth;

    public static boolean checkUsername(String s){
        s=unAccent(s);
        return !s.matches("[^A-Za-z0-9]");
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

    public static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        // return pattern.matcher(temp).replaceAll("");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean validMobile(String mb, TextInputEditText $mobile){
        if (mb.indexOf("00")==0){
            mb=mb.substring(1);
            $mobile.setText(mb);
            return validMobile(mb,$mobile);
        }
        else if (mb.length()==10){
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
        btnDate=findViewById(R.id.btnDate);
        birthday=findViewById(R.id.birthday);
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

        inputMoblie.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String s=inputMoblie.getText().toString();
                    if (!validMobile(s,inputMoblie)){
                        inputMoblie.setError(getString(R.string.valid_phone));
                    }
                    else {
                        inputMoblie.setError(null);
                    }
                }
                else{
                    if (inputMoblie.getError()!=null) {
                        String s = inputMoblie.getText().toString();
                        if (validMobile(s, inputMoblie))
                            inputMoblie.setError(null);
                    }
                }
            }
        });
        inputPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if (!checkPassword(inputPassword.getText().toString())){
                        inputPassword.setError(getString(R.string.valid_pass));
                    }
                    else if(inputPassword.getText().toString().isEmpty())
                        inputPassword.setError(getString(R.string.empty_input));
                    else
                        inputPassword.setError(null);
                    inputRePassword.setText(inputRePassword.getText().toString());
                }
            }
        });
        inputRePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String s1=inputPassword.getText().toString();
                String s2=inputRePassword.getText().toString();
                if(!b){
                    if(!s1.equals(s2)){
                        inputRePassword.setError(getString(R.string.incorrect_pass));
                    }
                    else inputRePassword.setError(null);
                }
            }
        });
        inputUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b) {
                    if (!checkUsername(inputUserName.getText().toString())) {
                        inputUserName.setError(getString(R.string.valid_name));
                    } else inputUserName.setError(null);
                }
            }
        });
        inputEmail.setOnFocusChangeListener((new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    if(!validEmail(inputEmail.getText().toString())){
                        inputEmail.setError(getString(R.string.valid_emali));
                    } else inputEmail.setError(null);
                }
            }
        }));
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveActivity(LoginActivity.class);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validSignUp()){
                    AlertDialog.Builder alertDialog;
                    alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    alertDialog.setMessage(getString(R.string.valid_input));
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    AlertDialog alert=alertDialog.create();
                    alert.show();
                }
                else {
//                    Intent intent=new Intent(RegisterActivity.this,OtpActivity.class);
//                    intent.putExtra("mobile",inputMoblie.getText().toString());
//                    intent.putExtra("pass",inputPassword.getText().toString());
//                    intent.putExtra("name",inputUserName.getText().toString());
//
//                    startActivity(intent);
                    Register();
                }
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSelectDate();
            }
        });
    }
    private boolean validSignUp(){
        if (!validMobile(inputMoblie.getText().toString(),inputMoblie)){
            return false;
        }
        if (!checkUsername(inputUserName.getText().toString())) return false;
        if (!checkPassword(inputPassword.getText().toString())) return false;
        if (!inputPassword.getText().toString().equals(inputRePassword.getText().toString())) return false;
        return true;
    }
    private void Register(){
        String email=inputEmail.getText().toString();
        String pass=inputPassword.getText().toString();

        //Khi tat ca deu hop le
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,getString(R.string.register_success),Toast.LENGTH_LONG).show();
                    mUser=mAuth.getCurrentUser();
                    String userId=mUser.getUid();

                    mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this,
                                        getString(R.string.verification_email_success) + mUser.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this,
                                        getString(R.string.verification_email_fail),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    reference= FirebaseDatabase.getInstance().getReference("User").child(userId);
                    List<String> list = new ArrayList<String>();
                    User user=new User(inputUserName.getText().toString(),"default",inputMoblie.getText().toString(),list,list, inputUserName.getText().toString().toLowerCase(),"offline");
                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            moveActivity(LoginActivity.class);
                        }
                    });


                }else {
                    Toast.makeText(RegisterActivity.this,getString(R.string.register_fail),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void buttonSelectDate() {
        //final boolean isSpinnerMode = this.checkBoxIsSpinnerMode.isChecked();

        // Date Select Listener.
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                birthday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                lastSelectedYear = year;
                if (LocalDate.now().getYear()-year<18){
                    birthday.setError(getString(R.string.valid_birth));
                }
                else birthday.setError(null);
                lastSelectedMonth = monthOfYear;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };

        DatePickerDialog datePickerDialog = null;

            datePickerDialog = new DatePickerDialog(this,
                    dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);

        // Show
        datePickerDialog.show();
    }
    private void moveActivity(Class screen){
        Intent myIntent=new Intent(RegisterActivity.this,screen);
        startActivity(myIntent);
    }

}