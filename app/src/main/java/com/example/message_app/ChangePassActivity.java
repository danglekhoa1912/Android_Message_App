package com.example.message_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.message_app.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangePassActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    private DatabaseReference reference;
    private TextView textViewName,friend;
    private CircleImageView profile_image;
    ImageButton btn_back;
    private Button btnOK;
    private TextInputEditText passOld,passNew,passNewCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        AnhXa();
        mAuth=FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        getUser();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        passNew.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if (!checkPassword(passNew.getText().toString())){
                        passNew.setError(getString(R.string.valid_pass));
                    }
                    else if(passNew.getText().toString().isEmpty())
                        passNew.setError(getString(R.string.empty_input));
                    else
                        passNew.setError(null);
                    passNewCheck.setText(passNewCheck.getText().toString());
                }
            }
        });
        passNewCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String s1=passNew.getText().toString();
                String s2=passNewCheck.getText().toString();

                if(!b){
                    if(!s1.equals(s2)){
                        passNewCheck.setError(getString(R.string.incorrect_pass));
                    }
                    else passNewCheck.setError(null);
                }
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            String s1=passNew.getText().toString();
            String s2=passNewCheck.getText().toString();

            @Override
            public void onClick(View view) {
                if(passOld.getText().toString().equals("") || passNew.getText().toString().equals("") || passNewCheck.getText().toString().equals("")){
                    Toast.makeText( ChangePassActivity.this, getString(R.string.message_input), Toast.LENGTH_SHORT).show();
                }else{
                    if(passNew.getText().toString().equals(passNewCheck.getText().toString())) {
                        if(mUser != null){
                            AuthCredential  credential = EmailAuthProvider.getCredential(mUser.getEmail(),passOld.getText().toString());
                            mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mUser.updatePassword(passNew.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskChangePass) {
                                                        if (taskChangePass.isSuccessful()) {
                                                            Toast.makeText( ChangePassActivity.this, getString(R.string.change_pass_success), Toast.LENGTH_SHORT).show();
                                                            ChangePassActivity.this.finish();
                                                        }
                                                        else{
                                                            Toast.makeText( ChangePassActivity.this, getString(R.string.change_pass_fail), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }else{
                                        Toast.makeText( ChangePassActivity.this, getString(R.string.incorrect_pass), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    else {
                        Toast.makeText( ChangePassActivity.this, getString(R.string.incorrect_pass), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void AnhXa(){
        textViewName=findViewById(R.id.textViewName);
        profile_image=findViewById(R.id.profile_image);
        btn_back=findViewById(R.id.btn_back);
        passOld = findViewById(R.id.pass_old);
        passNew = findViewById(R.id.pass_new);
        passNewCheck = findViewById(R.id.pass_new_check);
        btnOK = findViewById(R.id.btnOK);

        //friend=findViewById(R.id.friend);
    }

    private void getUser(){

        reference= FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                textViewName.setText(user.getUserName());
                if(user.getAvatar().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getAvatar()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void HandleAction(){

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(ChangePassActivity.this,LoginActivity.class));
                finish();
                return true;

        }
        return false;
    }
}






