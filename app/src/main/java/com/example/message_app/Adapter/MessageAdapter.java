package com.example.message_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.message_app.R;
import com.example.message_app.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT=0;
    public static final int MSG_TYPE_RIGHT=1;
    public static final int IMG_TYPE_LEFT=2;
    public static final int IMG_TYPE_RIGHT=3;

    private Context context;
    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser mUser;

    public MessageAdapter(Context context, List<Chat> mChat,String imageUrl) {
        this.context = context;
        this.mChat = mChat;
        this.imageUrl=imageUrl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else if(viewType==IMG_TYPE_LEFT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_image_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else if(viewType==IMG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_image_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat=mChat.get(position);
        if(chat.getImageUrl().equals("")){
            holder.show_message.setText(chat.getMess());
        }
        else {
            Glide.with(context).load(chat.getImageUrl()).into(holder.show_image);
        }
        if(imageUrl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(context).load(imageUrl).into(holder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_image,show_image;

        public ViewHolder(View view) {
            super(view);
            show_message = view.findViewById(R.id.show_message);
            profile_image = view.findViewById(R.id.profile_image);
            show_image=view.findViewById(R.id.show_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(mUser.getUid())){
            if(mChat.get(position).getImageUrl().equals(""))
                return MSG_TYPE_RIGHT;
            else
                return IMG_TYPE_RIGHT;
        }
        else
            if(mChat.get(position).getImageUrl().equals(""))
                return MSG_TYPE_LEFT;
            else
                return IMG_TYPE_LEFT;
    }
}
