package com.example.message_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.message_app.Adapter.MessageAdapter;
import com.example.message_app.model.Chat;
import com.example.message_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    public static final int ID_SENDER_LEFT=0;
    public static final int ID_SENDER_RIGHT=1;
    public int positionIdSender;

    CircleImageView profile_image;
    TextView userName;
    ImageButton btn_send;
    EditText text_view;

    FirebaseUser mUser;
    DatabaseReference reference;

    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mchat;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        AnhXa();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        String userId=intent.getStringExtra("userId");
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        reference=FirebaseDatabase.getInstance().getReference("User").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                userName.setText(user.getUserName());
                if(user.getAvatar().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(MessageActivity.this).load(user.getAvatar()).into(profile_image);
                }
                ReadMessage(mUser.getUid(),userId,user.getAvatar());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=text_view.getText().toString();
                if(!msg.equals("")){
                    SendMessage(mUser.getUid(),userId,msg,"");
                }
                else {
                    Toast.makeText(MessageActivity.this,"Vui lòng nhập tin nhắn bạn muốn gừi",Toast.LENGTH_SHORT).show();
                }
                text_view.setText("");
            }
        });

    }

    private void AnhXa(){
        profile_image=findViewById(R.id.profile_image);
        userName=findViewById(R.id.textViewName);
        btn_send=findViewById(R.id.btn_send);
        text_view=findViewById(R.id.text_send);
        recyclerView=findViewById(R.id.recycler_view);

        intent=getIntent();
    }

    private void SendMessage(String sender,String receiver,String mess,String imageUrl){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("chat_rooms");
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("mess",mess);
        hashMap.put("imageUrl",imageUrl);
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis()));
        if(positionIdSender==ID_SENDER_LEFT){
            reference.child(sender+"_"+receiver).push().setValue(hashMap);
        }
        else {
            reference.child(receiver + "_" + sender).push().setValue(hashMap);
        }
    }

    private void ReadMessage(String sender,String receiver,String imageUrl){
        mchat=new ArrayList<>();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("chat_rooms");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(sender+"_"+receiver)) {
                    reference=FirebaseDatabase.getInstance().getReference("chat_rooms").child(sender+"_"+receiver);
//        reference=FirebaseDatabase.getInstance().getReference("chat_rooms");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mchat.clear();
                            for(DataSnapshot snapshot1: snapshot.getChildren()){
                                Chat chat=snapshot1.getValue((Chat.class));
                                mchat.add(chat);
                                messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imageUrl);
                                recyclerView.setAdapter(messageAdapter);
                            }
                            positionIdSender=ID_SENDER_LEFT;

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if(snapshot.hasChild(receiver+"_"+sender)){
                    reference=FirebaseDatabase.getInstance().getReference("chat_rooms").child(receiver+"_"+sender);
//        reference=FirebaseDatabase.getInstance().getReference("chat_rooms");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            mchat.clear();
                            for(DataSnapshot snapshot1: snapshot.getChildren()){
                                Chat chat=snapshot1.getValue((Chat.class));
                                mchat.add(chat);
                                messageAdapter=new MessageAdapter(MessageActivity.this,mchat,imageUrl);
                                recyclerView.setAdapter(messageAdapter);
                            }
                            positionIdSender=ID_SENDER_RIGHT;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
