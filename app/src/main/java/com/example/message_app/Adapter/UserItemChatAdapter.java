package com.example.message_app.Adapter;

import static android.content.ContentValues.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserItemChatAdapter extends RecyclerView.Adapter<UserItemChatAdapter.ViewHolder> {
    private Context context;
    private List<String> userIdList;
    final String[] key = new String[3];
    FirebaseUser mUser;
    String uid;
    DataSnapshot snapshot_chat_left,snapshot_chat_right;
    private boolean isChat;

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public static void noti() {
//        AudioAttributes attrs = new AudioAttributes.Builder()
//                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                .build();
//        AudioManager audioManager;
//        SoundPool sp = new SoundPool.Builder()
//                .setMaxStreams(10)
//                .setAudioAttributes(attrs)
//                .build();
//        sp.play(R.raw.noti, 1, 1, 1, 0, 1f);
//    }



    public UserItemChatAdapter(Context context, List<String> userIdList,boolean isChat) {
        this.context = context;
        this.userIdList = userIdList;
        this.isChat =isChat;
    }
    @NonNull
    @Override
    public UserItemChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = mUser.getUid();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_item, parent, false);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes attributes=new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();

            NotificationChannel channel=new NotificationChannel("My Notification","My Notification",NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(uri,attributes);
            NotificationManager manager= context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        return new UserItemChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemChatAdapter.ViewHolder holder, int position) {
        DatabaseReference databaseListfriend = FirebaseDatabase.getInstance().getReference("User").child(userIdList.get(position));
        databaseListfriend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                holder.user_name.setText(user.getUserName());
                String uid2 = snapshot.getKey();
                FirebaseDatabase.getInstance().getReference("chat_rooms").child(uid + "_" + uid2).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Chat chat = snapshot1.getValue(Chat.class);
                                if (chat.getSender().equals(uid)) {
                                    holder.user_chat.setTypeface(null, Typeface.NORMAL);
                                    holder.user_chat.setText("Bạn: " + chat.getMess());
                                } else {
                                    holder.user_chat.setText(holder.user_name.getText().toString() + ":" + chat.getMess());
                                    if (chat.getStatus().equals("sent")) {
                                        holder.user_chat.setTypeface(null, Typeface.BOLD);
                                        sendNotification(holder,chat);
                                    } else holder.user_chat.setTypeface(null, Typeface.NORMAL);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                FirebaseDatabase.getInstance().getReference("chat_rooms").child(uid2 + '_' + uid).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Chat chat = snapshot1.getValue(Chat.class);
                                if (chat.getSender().equals(uid)) {
                                    holder.user_chat.setTypeface(null, Typeface.NORMAL);
                                    holder.user_chat.setText("Bạn: " + chat.getMess());
                                } else {
                                    holder.user_chat.setText(holder.user_name.getText().toString() + ": " + chat.getMess());
                                    if (chat.getStatus().equals("sent")) {
                                        holder.user_chat.setTypeface(null, Typeface.BOLD);
                                        sendNotification(holder,chat);
                                    } else holder.user_chat.setTypeface(null, Typeface.NORMAL);
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
                if(isChat){
                    if(user.getStatus().equals("online")){
                        holder.img_onl.setVisibility(View.VISIBLE);
                        holder.img_off.setVisibility(View.GONE);
                    }else{
                        holder.img_onl.setVisibility(View.GONE);
                        holder.img_off.setVisibility(View.VISIBLE);
                    }
                }else{
                    holder.img_onl.setVisibility(View.GONE);
                    holder.img_off.setVisibility(View.GONE);
                }
                FirebaseDatabase.getInstance().getReference("chat_rooms").child(uid + "_" + uid2).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                snapshot_chat_left = snapshot1;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                key[2] = "false";
                FirebaseDatabase.getInstance().getReference("chat_rooms").child(uid2 + '_' + uid).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                snapshot_chat_right = snapshot1;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        key[2]="false";
                        Chat chat =snapshot_chat_right==null?new Chat("","","","","",""): snapshot_chat_right.getValue(Chat.class);
                        if (chat.getStatus().equals("sent") && chat.getSender().equals(uid2)&&snapshot_chat_right!=null) {
                            key[0] = snapshot_chat_right.getKey();
                            key[1] =uid2+"_"+uid;
                            key[2] = "true";
                        }
                        chat = snapshot_chat_left==null?new Chat("","","","","",""):snapshot_chat_left.getValue(Chat.class);
                        if (chat.getStatus().equals("sent") && chat.getSender().equals(uid2)&&snapshot_chat_left!=null) {
                            key[0] = snapshot_chat_left.getKey();
                            key[1] =uid+"_"+uid2;
                            key[2] = "true";
                        }
                        if (key[2].equals("true")) {
                            Map<String, Object> change = new HashMap<>();
                            change.put("status", "seen");
                            FirebaseDatabase.getInstance().getReference("chat_rooms").child(key[1]).child(key[0]).updateChildren(change);
                        }
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
        public ImageView img_onl;
        public ImageView img_off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            user_name = itemView.findViewById(R.id.user_name);
            user_chat = itemView.findViewById(R.id.user_chat);
            img_off = itemView.findViewById(R.id.img_off);
            img_onl = itemView.findViewById(R.id.img_onl);
        }
    }

    public void sendNotification(UserItemChatAdapter.ViewHolder holder,Chat chat){

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notifyIntent = new Intent(context, MessageActivity.class);
        notifyIntent.putExtra("userId", userIdList.get(holder.getAdapterPosition()));
        PendingIntent notifyPendingIntent=PendingIntent.getActivity(context,new Random().nextInt(),notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"My Notification");
        builder.setContentTitle("Bạn có tin nhắn mời từ " + holder.user_name.getText().toString());
        builder.setContentText(chat.getMess());
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        builder.setSound(uri);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setContentIntent(notifyPendingIntent);

        NotificationManagerCompat.from(context).notify(new Random().nextInt(),builder.build());
    }

}
