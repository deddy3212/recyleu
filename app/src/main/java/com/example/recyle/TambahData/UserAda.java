package com.example.recyle.TambahData;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.example.recyle.LoginUser.ProfilUsers;
import com.example.recyle.R;
import com.google.firebase.auth.FirebaseAuth;

public class UserAda extends Fragment {

    CardView tes3;
    Button mlogin;

    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_ada,container,false);
        auth = FirebaseAuth.getInstance();

        mlogin = v.findViewById(R.id.BT_login);
        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent (UserAda.super.getContext() , ProfilUsers.class));
            }
        });


        return v;
    }
}
