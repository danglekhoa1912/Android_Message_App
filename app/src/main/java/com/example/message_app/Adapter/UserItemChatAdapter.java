package com.example.message_app.Adapter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.message_app.ChatFragment;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserItemChatAdapter extends RecyclerView.Adapter<UserItemChatAdapter.ViewHolder> {
    private Context context;
    private List<String> userIdList;
    FirebaseUser mUser;
    String uid;
    private boolean isChat;


    public UserItemChatAdapter(Context context, List<String> userIdList, boolean isChat) {
        this.context = context;
        this.userIdList = userIdList;
        this.isChat = isChat;
    }

    private String convertTime(Long t1) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date date1 = new Date(t1);
        return date1.getHours() + " : " + date1.getMinutes();
    }

    @NonNull
    @Override
    public UserItemChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        uid = mUser.getUid();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_item, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();

            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(uri, attributes);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
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
                if (user.getAvatar().equals("default")) {
                    holder.img_avatar.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(context).load(user.getAvatar()).into(holder.img_avatar);
                }
                if (isChat) {
                    if (user.getStatus().equals("online")) {
                        holder.img_onl.setVisibility(View.VISIBLE);
                        holder.img_off.setVisibility(View.GONE);
                    } else {
                        holder.img_onl.setVisibility(View.GONE);
                        holder.img_off.setVisibility(View.VISIBLE);
                    }
                } else {
                    holder.img_onl.setVisibility(View.GONE);
                    holder.img_off.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseListfriend.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                holder.user_name.setText(user.getUserName());
                String uid2 = snapshot.getKey();
                FirebaseDatabase.getInstance().getReference("chat_rooms").child(uid + "_" + uid2).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ChatFragment firstFragment = new ChatFragment();
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Chat chat = snapshot1.getValue(Chat.class);
                                if (chat.getSender().equals(uid)) {
                                    holder.user_chat.setTypeface(null, Typeface.NORMAL);
                                    holder.user_chat.setText("Bạn: " + chat.getMess());
                                    holder.time.setText(convertTime(Long.parseLong(chat.getTimestamp())));
                                } else {
                                    holder.user_chat.setText(holder.user_name.getText().toString() + ":" + chat.getMess());
                                    holder.time.setText(convertTime(Long.parseLong(chat.getTimestamp())));
                                    if (chat.getStatus().equals("sent")) {
                                        if (holder.user_chat.getTypeface() == null)
                                            sendNotification(holder, chat);
                                        holder.user_chat.setTypeface(null, Typeface.BOLD);
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
                                    holder.time.setText(convertTime(Long.parseLong(chat.getTimestamp())));
                                } else {
                                    holder.user_chat.setText(holder.user_name.getText().toString() + ": " + chat.getMess());
                                    holder.time.setText(convertTime(Long.parseLong(chat.getTimestamp())));
                                    if (chat.getStatus().equals("sent")) {
                                        if (holder.user_chat.getTypeface() == null)
                                            sendNotification(holder, chat);
                                        holder.user_chat.setTypeface(null, Typeface.BOLD);
                                    } else holder.user_chat.setTypeface(null, Typeface.NORMAL);
                                }
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
                        Intent intent = new Intent(context, MessageActivity.class);
                        intent.putExtra("userId", userIdList.get(holder.getAdapterPosition()));
                        intent.putExtra("check", true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        private TextView user_name, user_chat, time;
        public ImageView img_onl;
        public ImageView img_off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_avatar = itemView.findViewById(R.id.img_avatar);
            user_name = itemView.findViewById(R.id.user_name);
            user_chat = itemView.findViewById(R.id.user_chat);
            img_off = itemView.findViewById(R.id.img_off);
            img_onl = itemView.findViewById(R.id.img_onl);
            time = itemView.findViewById(R.id.time);
        }
    }

    public void sendNotification(UserItemChatAdapter.ViewHolder holder, Chat chat) {

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent notifyIntent = new Intent(context, MessageActivity.class);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "My Notification");
        builder.setContentTitle("Bạn có tin nhắn mời từ " + holder.user_name.getText().toString());
        builder.setContentText(chat.getMess());
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);
        builder.setSound(uri);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        if (holder.getAdapterPosition() != -1) {
            notifyIntent.putExtra("userId", userIdList.get(holder.getAdapterPosition()));
            notifyIntent.putExtra("check",true);
            PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, new Random().nextInt(), notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(notifyPendingIntent);
        }

        NotificationManagerCompat.from(context).notify(new Random().nextInt(), builder.build());
    }

}
