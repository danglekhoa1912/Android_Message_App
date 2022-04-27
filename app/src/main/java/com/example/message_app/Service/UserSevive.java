package com.example.message_app.Service;

import androidx.annotation.NonNull;

import com.example.message_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSevive {
    private static DatabaseReference reference;
    private static FirebaseAuth mAuth;
    private static User user;

    public static User getUser(){
        mAuth= FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 user=snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return user;
    }
}
