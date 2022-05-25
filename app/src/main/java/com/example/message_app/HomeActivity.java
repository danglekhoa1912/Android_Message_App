package com.example.message_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.message_app.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.prefs.Preferences;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private TextView textViewName,friend;
    private CircleImageView profile_image;

    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        AnhXa();


        mAuth=FirebaseAuth.getInstance();

        getUser();

        ChatFragment firstFragment = new ChatFragment();
        ListFriendFragment secondFragment = new ListFriendFragment();
        ProfileFragment thirdFragment = new ProfileFragment();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, firstFragment).commit();
                        return true;

                    case R.id.list_friend:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, secondFragment).commit();
                        return true;

                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, thirdFragment).commit();
                        return true;
                }
                return false;
            }
        } );
        bottomNavigationView.setSelectedItemId(R.id.chat);
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

    private void AnhXa(){
        textViewName=findViewById(R.id.textViewName);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        profile_image=findViewById(R.id.profile_image);
        //friend=findViewById(R.id.friend);
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
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                finish();
                return true;
            case R.id.change_pass:
                startActivity(new Intent(HomeActivity.this,ChangePassActivity.class));
                return true;
            case R.id.change_language:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle(R.string.choose_language)
                        .setItems(R.array.language, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i){
                                    case 0:
                                        changeLanguage("vi");
                                        saveSetting("lang","vi");
                                        return;
                                    case 1:
                                        changeLanguage("en");
                                        saveSetting("lang","en");
                                        return;
                                    default:
                                        return;
                                }
                            }
                        });
                builder.show();
                return true;
            case R.id.help:
                startActivity(new Intent(HomeActivity.this,TutorialActivity.class));
                return true;
        }
        return false;
    }


    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("User").child(mUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("status", status);
        reference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    protected void onStart() {
        super.onStart();
        status("online");
    }

    public void changeLanguage(String language){
        Locale locale=new Locale(language);
        Configuration config= new Configuration();
        config.locale=locale;
        getBaseContext().getResources().updateConfiguration(
                config,
                getBaseContext().getResources().getDisplayMetrics()
        );
        Intent intent=new Intent(HomeActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    public void saveSetting(String key,String value){
        SharedPreferences preferences =getSharedPreferences("MySetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }


}