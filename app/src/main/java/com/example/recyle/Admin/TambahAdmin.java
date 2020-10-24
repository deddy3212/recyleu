package com.example.recyle.Admin;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;



public class TambahAdmin extends AppCompatActivity implements View.OnClickListener{
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private EditText txtidAdmin;
    private EditText txtnama;
    private EditText Tphone;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_admin);

        setTitle("Add Admin App");
        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.button_back).setOnClickListener(this);
        findViewById(R.id.ButtonSave).setOnClickListener(this);

        txtEmailAddress = findViewById(R.id.edit_text_email);
        txtPassword = findViewById(R.id.edit_text_password);
        txtnama = findViewById(R.id.edit_text_name);
        txtidAdmin = findViewById(R.id.edit_text_nim);
        Tphone = findViewById(R.id.telp);
    }

    public void konfir() {
        new AlertDialog.Builder(com.example.recyle.Admin.TambahAdmin.this)
                .setTitle("Warning !!!")
                .setMessage("Do you want Save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        com.example.recyle.Admin.TambahAdmin.this.addAdmin();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }


    public void addAdmin() {
        final String phone = Tphone.getText().toString().trim();
        final String email = txtEmailAddress.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        final String name = txtnama.getText().toString().trim();
        final String idadmin = txtidAdmin.getText().toString().trim();

        if (idadmin.length() != 9 ) {
            txtidAdmin.setError("NIM not valid"); txtidAdmin.requestFocus(); return;
        }
        if (idadmin.isEmpty()) {
            txtidAdmin.setError(getString(R.string.input_error_nim)); txtidAdmin.requestFocus(); return;
        }
        if (password.length() < 6) {
            txtPassword.setError(getString(R.string.input_error_password_length)); txtPassword.requestFocus(); return;
        }
        if (password.isEmpty()) {
            txtPassword.setError(getString(R.string.input_error_password)); txtPassword.requestFocus(); return;
        }
        if (name.isEmpty()) {
            txtnama.setError(getString(R.string.input_error_nama)); txtnama.requestFocus(); return;
        }
        if (email.isEmpty()) {
            txtEmailAddress.setError(getString(R.string.input_error_email)); txtEmailAddress.requestFocus(); return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmailAddress.setError(getString(R.string.input_error_email_invalid)); txtEmailAddress.requestFocus();return;
        }
        if (phone.isEmpty()) {
            Tphone.setError(getString(R.string.input_error_phone)); Tphone.requestFocus(); return;
        }
        if (phone.length() < 9 ) {
            Tphone.setError(getString(R.string.input_error_phone_invalid)); Tphone.requestFocus(); return;
        }
        if (phone.length() > 15){
            Tphone.setError(getString(R.string.input_error_phone_invalid)); Tphone.requestFocus(); return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(this,
                "Please wait...","Processing...", true);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Admin insert = new Admin(idadmin, password, name, email, phone);
                                                FirebaseDatabase.getInstance().getReference("Administrator")
                                                        .child(idadmin).setValue(insert);
                                                Toast.makeText(TambahAdmin.this,
                                                        "Registrasi sukses.. cek email untuk memverifikasi data Anda",
                                                        Toast.LENGTH_LONG).show();
                                                Intent back = new Intent();
                                                setResult(RESULT_OK, back);
                                                finish();
                                            } else {
                                                Toast.makeText(TambahAdmin.this,
                                                        getString(R.string.registration_fail), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    } );
                        } else {
                            Toast.makeText(TambahAdmin.this, task.getException()
                                    .getMessage(), Toast.LENGTH_LONG).show();
                        } progressDialog.dismiss();
                    }
                });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_back:
                cancel();
                break;
            case R.id.ButtonSave:
                konfir();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Back !!!");
        builder.setMessage("Are you want to leave this page?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void cancel() {
        txtEmailAddress.setText("");
        txtnama.setText("");
        txtPassword.setText("");
        Tphone.setText("");
        txtidAdmin.setText("");
        Intent back = new Intent();
        setResult(RESULT_OK, back);
        finish();
    }
}
