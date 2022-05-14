package com.example.message_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.message_app.Adapter.UserItemChatAdapter;
import com.example.message_app.model.Chat;
import com.example.message_app.model.User;
import com.example.message_app.model.userList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView rcv;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseAuth mAuth;
    private int i=0;
    private DatabaseReference reference;
    private DataSnapshot dataSnapshot;
    private UserItemChatAdapter UserItemChatAdapter;
    private List<userList> userIdList=new ArrayList<userList>();
    private String uid;


    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean compareTime(Long t1,long t2){
        Date date1=new Date(t1);
        Date date2=new Date(t2);
        return date1.before(date2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        rcv = view.findViewById(R.id.rcv_chat);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //userIdList.clear();
                User userCurrent = dataSnapshot.getValue(User.class);
                FirebaseDatabase.getInstance().getReference("chat_rooms").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userIdList.clear();
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            if(snapshot1.getKey().contains(firebaseUser.getUid())){
                                String id= snapshot1.getKey().replace(firebaseUser.getUid(), "");
                                id=id.replace("_","");
                                userList us=new userList(id, snapshot1.getKey(), 0L);
                                userIdList.add(us);
                            }
                        }
                        for (int j = 0; j < userIdList.size(); j++) {
                            int finalJ = j;
                            final int[] finali = {0};
                            FirebaseDatabase.getInstance().getReference("chat_rooms").child(userIdList.get(j).getChat()).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1:snapshot.getChildren()) {
                                        Chat chat=snapshot1.getValue(Chat.class);
                                        userIdList.get(finalJ).setTime(Long.parseLong(chat.getTimestamp()));
                                    }
                                    boolean isChange = false;
                                    if (finalJ==userIdList.size()-1){
                                        for (int k = 0; k < userIdList.size()-1; k++) {
                                            for (int l = k; l < userIdList.size(); l++) {
                                                if (compareTime(userIdList.get(k).getTime(),userIdList.get(l).getTime())){
                                                    Collections.swap(userIdList,l,k);
                                                    isChange=true;
                                                }
                                            }
                                        }
                                    }
                                    if (isChange || finali[0] !=finalJ){
                                        List<String> idList=new ArrayList<String>();
                                        for (userList us:
                                                userIdList) {
                                            idList.add(us.getId());
                                        }
                                        UserItemChatAdapter = new UserItemChatAdapter(getContext(), idList,true);
                                        rcv.setAdapter(UserItemChatAdapter);
                                        finali[0] =finalJ;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        /*if (i!=j ){
                            List<String> idList=new ArrayList<String>();
                            for (userList us:
                                 userIdList) {
                                idList.add(us.getId());
                            }
                            UserItemChatAdapter = new UserItemChatAdapter(getContext(), idList,true);
                            rcv.setAdapter(UserItemChatAdapter);
                            i=j;
                        }*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}