package com.example.recyle.LoginUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyle.PesanTiket.Tambah_Pesanan;
import com.example.recyle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText mEmail, mPassword;
    TextView mpunya_akun;
    Button mRegistrasi;

    //progres dialog
    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //init view
        mEmail = findViewById(R.id.email_edt);
        mPassword = findViewById(R.id.password_edt);
        mRegistrasi = findViewById(R.id.register_btn);
        mpunya_akun = findViewById(R.id.punya_akun);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrasi User....");

        //regitrasi klik
        mRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //input email,password
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Invalid Email");
                    mEmail.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Masukkan Password Anda",
                            Toast.LENGTH_SHORT).show();
                }
                else
                    registerEmail(email,password); //registrasi user
            }

        });
        mpunya_akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginUser_Activity.class));
                finish();
            }
        });

    }

    private void registerEmail(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success,
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            //saat user sudah regitrasi
                            //menggunakan hashmap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            //put info in hashmap
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            hashMap.put("name", "");
                            hashMap.put("phone", ""); //akan ditambahkan saat editprofil
                            hashMap.put("image", "");
                            hashMap.put("cover", "");
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            //path
                            DatabaseReference reference = database.getReference("Users");
                            //put data
                            reference.child(uid).setValue(hashMap);
                            Toast.makeText(RegisterActivity.this, "Regestered..\n" +user.getEmail()
                                    , Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, ProfilUsers.class));
                                        finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
