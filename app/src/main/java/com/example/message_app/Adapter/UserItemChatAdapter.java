package com.example.message_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.message_app.MessageActivity;
import com.example.message_app.R;
import com.example.message_app.model.Chat;
import com.example.message_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserItemChatAdapter extends RecyclerView.Adapter<UserItemChatAdapter.ViewHolder> {
    private Context context;
    private List<String> userIdList;
    FirebaseUser mUser;
    String uid;
    private void getLastMessage(String check){

    }
    public UserItemChatAdapter(Context context, List<String> userIdList) {
        this.context = context;
        this.userIdList = userIdList;
    }

    @NonNull
    @Override
    public UserItemChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = mUser.getUid();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_item, parent, false);
        return new UserItemChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemChatAdapter.ViewHolder holder, int position) {
        DatabaseReference databaseListfriend = FirebaseDatabase.getInstance().getReference("User").child(userIdList.get(position));
        databaseListfriend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                //Log.d("user",String.valueOf(snapshot));
                holder.user_name.setText(user.getUserName());
                String uid2=snapshot.getKey();
                FirebaseDatabase.getInstance().getReference("chat_rooms").child(uid+"_"+uid2).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Chat chat = snapshot1.getValue(Chat.class);
                                if (chat.getSender().equals(uid)) {
                                    holder.user_chat.setText("Bạn: " + chat.getMess());
                                } else {
                                    holder.user_chat.setText(holder.user_name.getText().toString() +":"+ chat.getMess());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("chat_rooms").child(snapshot.getKey()+'_'+uid ).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                                Chat chat = snapshot1.getValue(Chat.class);
                                if (chat.getSender().equals(uid)) {
                                    if(chat.getImageUrl().equals("")){
                                        holder.user_chat.setText("Bạn: " + chat.getMess());
                                    }
                                    else {
                                        holder.user_chat.setText("Bạn:Đã gửi một bức ảnh");
                                    }
                                }

                                else {
                                    if(chat.getImageUrl().equals("")){
                                        holder.user_chat.setText(holder.user_name.getText().toString() +":"+ chat.getMess());
                                    }
                                    else {
                                        holder.user_chat.setText(holder.user_name.getText().toString() +":Đã gửi một bức ảnh");
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (user.getAvatar().equals("default")) {
                    holder.img_avatar.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(context).load(user.getAvatar()).into(holder.img_avatar);
                }
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, MessageActivity.class);
                        intent.putExtra("userId", userIdList.get(holder.getAdapterPosition()));
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (userIdList != null) {
            return userIdList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView img_avatar;
        private TextView user_name, user_chat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            user_name = itemView.findViewById(R.id.user_name);
            user_chat = itemView.findViewById(R.id.user_chat);
        }
    }
}
