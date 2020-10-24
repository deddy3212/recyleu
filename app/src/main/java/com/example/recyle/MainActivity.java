package com.example.recyle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyle.Admin.Dashboard_Admin;


import com.example.recyle.TambahData.UserAda;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class MainActivity extends AppCompatActivity {

BottomNavigationView bottomNav;
    Fragment er = null;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

//option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        //getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
       if (item.getItemId() == R.id.help) {
            progressDialog.show();
            login();
        }

        return true;
    }

//sampai sini


    //bottomnavigation mulai dari sini
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.nav_fav:
                            selectedFragment = new FavFragment();
                            break;

                        case R.id.nav_menu:
                            selectedFragment = new MenuFrag();
                            break;

                        case R.id.nav_person:
                            selectedFragment = er;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container
                    ,selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user ==null){
            er = new LoginFrag();
        }
        else {
            er = new UserAda();
        }
    }

    //sampai sini

    public void login() {
        progressDialog.dismiss();
        final EditText txtEmailLogin;
        TextView button3;
        final EditText txtPwd;
        Button button1, button2;
        FirebaseDatabase mDatabase;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("Login Administrator");

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.login, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        button1 = dialogView.findViewById(R.id.button_login);
        button2 = dialogView.findViewById(R.id.button_cancel);
        button3 = dialogView.findViewById(R.id.lupa_password);

        txtEmailLogin = dialogView.findViewById(R.id.edit_text_username);
        txtPwd = dialogView.findViewById(R.id.edit_text_password);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
                progressDialog.show();
                forgot();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmailLogin.getText().toString();
                String password = txtPwd.getText().toString();

                if (email.isEmpty()) {
                    txtEmailLogin.setError("Input username");
                    txtEmailLogin.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    txtPwd.setError("Input password");
                    txtPwd.requestFocus();
                    return;
                } else {
                    final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this,
                            "Please wait...", "Proccessing...", true);
                    (firebaseAuth.signInWithEmailAndPassword(email, password)).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()) {
                                        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                            Toast.makeText(MainActivity.this, "Login successful",
                                                    Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase.getInstance();
                                            Intent i3 = new Intent(getApplicationContext(), Dashboard_Admin.class);
                                            startActivity(i3);
                                        } else {
                                            Toast.makeText(MainActivity.this,
                                                    "Akun anda belum terverifikasi... " +
                                                            "Silahkan Cek Email Anda",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        Log.e("ERROR", task.getException().toString());
                                        txtEmailLogin.requestFocus();
                                        Toast.makeText(MainActivity.this, task.getException()
                                                .getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

    }

    private void forgot() {
        progressDialog.dismiss();
        final EditText edtEmail;
        final FirebaseAuth mAuth;
        Button btnBack;
        Button btnResetPassword;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.reset_password, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        edtEmail = dialogView.findViewById(R.id.edt_reset_email);
        btnResetPassword = dialogView.findViewById(R.id.btn_reset_password);
        btnBack = dialogView.findViewById(R.id.btn_back);

        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    edtEmail.setError("Enter your email!");
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                                    builder.setCancelable(false);
                                    builder.setTitle("Warning ");
                                    builder.setMessage("Check email to reset your password!");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            login();
                                        }
                                    });
                                    android.app.AlertDialog alert = builder.create();
                                    alert.show();

                                } else {
                                    edtEmail.setError("Fail to send reset password email!\nEmail not valid");
                                }
                            }
                        });
            }
        });

    }



}
