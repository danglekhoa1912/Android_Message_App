package com.example.message_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.message_app.Adapter.UserAdapter;
import com.example.message_app.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListFriendFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<String> userIdList;
    private EditText search_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_friend, container,false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userIdList = new ArrayList<>();
        readFriendUsers();

        search_users = view.findViewById(R.id.seach_edit_text);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().equals("")) {
                    searchUsers(charSequence.toString().trim().toLowerCase());
                }
                else{
                    readFriendUsers();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void searchUsers(String input){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("User").orderByChild("search").startAt(input).endAt(input + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userIdList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot data: snapshot.getChildren()){
                        if(!data.getKey().equals(firebaseUser.getUid()))
                            userIdList.add(data.getKey());
                    }
                }
                //userAdapter = new UserAdapter(getContext(), userIdList);
               // recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference("User").orderByChild("mobile").startAt(input).endAt(input + "\uf8ff").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              //  userIdList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot data: snapshot.getChildren()){
                        if(!data.getKey().equals(firebaseUser.getUid()))
                            userIdList.add(data.getKey());
                    }
                }
                    userAdapter = new UserAdapter(getContext(), userIdList,true);
                    recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readFriendUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userIdList.clear();
                    User userCurrent = dataSnapshot.getValue(User.class);
                    if(userCurrent.getListFriend()!=null)
                        userIdList = userCurrent.getListFriend();
                    userAdapter = new UserAdapter(getContext(), userIdList,true);
                    recyclerView.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}