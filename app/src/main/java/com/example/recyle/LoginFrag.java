package com.example.recyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recyle.LoginUser.LoginUser_Activity;
import com.example.recyle.LoginUser.RegisterActivity;

public class LoginFrag extends Fragment {
  Button mregister,mlogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmet_login,container,false);


       mregister = v.findViewById(R.id.BT_register);
       mregister.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent (LoginFrag.super.getContext() , RegisterActivity.class));
           }
       });



       mlogin = v.findViewById(R.id.BT_login);
       mlogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent (LoginFrag.super.getContext() , LoginUser_Activity.class));
           }
       });

        return v;

    }
}
