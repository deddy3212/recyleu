package com.example.recyle.Admin;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.R;


public class LihatAdmin extends AppCompatActivity {
    TextView pIdadmin,nama,password,email, telp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lihat_admin);


        setTitle("Data Admin");

        pIdadmin= findViewById(R.id.edit_text_nim);
        password= findViewById(R.id.edit_text_password);
        nama= findViewById(R.id.edit_text_name);
        email= findViewById(R.id.edit_text_email);
        telp= findViewById(R.id.telp);
        Intent in=getIntent();
        Bundle bun=this.getIntent().getExtras();
        this.setTitle(bun.getString("email"));

        pIdadmin.setText(bun.getString("idadmin"));
        password.setText("*******");
        //password.setText(bun.getString("password"));
        nama.setText(bun.getString("nama"));
        email.setText(bun.getString("email"));
        telp.setText(bun.getString("telp"));

    }
}