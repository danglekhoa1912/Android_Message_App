package com.example.message_app;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.message_app.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView user_avatar;
    private EditText inputMobile,inputBirthdate,inputUserName;
    private Button btnEdit;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        mAuth=FirebaseAuth.getInstance();
        AnhXa(view);
        reference= FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                inputUserName.setText(user.getUserName());
                inputMobile.setText(user.getMobile());
                //inputEmail.setText(user.get);
                if(user.getAvatar().equals("default")){
                    user_avatar.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(ProfileFragment.this).load(user.getAvatar()).into(user_avatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (btnEdit.getText().toString().equals("Sửa thông tin")){
                    //inputEmail.setEnabled(true);
                    inputUserName.setEnabled(true);
                    inputMobile.setEnabled(true);
                    inputBirthdate.setEnabled(true);
                    btnEdit.setText("Lưu thay đổi");
                }
                else {
                    btnEdit.setText("Sửa thông tin");
                    inputUserName.setEnabled(false);
                    inputMobile.setEnabled(false);
                    inputBirthdate.setEnabled(false);
                    //UserProfileChangeRequest
                    //update tt vao firebase
                    reference= FirebaseDatabase.getInstance().getReference("User").child(mAuth.getCurrentUser().getUid());
                    Map<String,Object> change=new HashMap<String,Object>();
                    change.put("userName",inputUserName.getText().toString());
                    reference.updateChildren(change);
                    change.put("mobile",inputMobile.getText().toString());
                    reference.updateChildren(change);
                    Toast.makeText(getContext(), "Đã hoàn thành", Toast.LENGTH_SHORT).show();
                }
            }
        });
        inputMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String s=inputMobile.getText().toString();
                    if (!validMobile(s)){
                        inputMobile.setError("Số điện thoại không đúng định dạng");
                        btnEdit.setEnabled(false);
                    }
                    else {
                        inputMobile.setError(null);
                        btnEdit.setEnabled(true);
                    }
                }
                else{
                    if (inputMobile.getError()!=null) {
                        String s = inputMobile.getText().toString();
                        if (validMobile(s)) {
                            inputMobile.setError(null);
                            btnEdit.setEnabled(true);
                        }
                    }
                }
            }
        });
        return view;
    }
    private void AnhXa(View view){
        inputUserName=view.findViewById(R.id.inputUserName);
        inputMobile=view.findViewById(R.id.inputMobile);
        inputBirthdate=view.findViewById(R.id.inputBirthdate);
        btnEdit=view.findViewById(R.id.btnEdit);
        user_avatar=view.findViewById(R.id.user_avatar);
    }
    public static boolean validMobile(String mb){
        if (mb.length()==10) {
            return mb.matches("-?\\d+?");
        }
        return false;
    }
}