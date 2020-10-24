package com.example.recyle.LoginUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyle.HomeFragment;
import com.example.recyle.MainActivity;
import com.example.recyle.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginUser_Activity extends AppCompatActivity {

    private static final int RC_SIGN_IN =100 ;
    GoogleSignInClient mGoogleSignInClient;
    EditText mEmail, mPassword;
    Button BT_login;
    TextView tv_tidakpunyaakun, lupaan;
    AlertDialog alertDialog;

    SignInButton mSignInButton;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user_);

        //sebelum mauth
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.email_edt);
        mPassword = findViewById(R.id.password_edt);
        BT_login = findViewById(R.id.loginBTN);
        tv_tidakpunyaakun = findViewById(R.id.tidak_punya_akun);
        mSignInButton = findViewById(R.id.google);

        lupaan = findViewById(R.id.lupa);
        lupaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgot();
            }
        });

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //begin google log in
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        BT_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Input data
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Invalid Email");
                    mEmail.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginUser_Activity.this, "Masukkan Password Anda",
                            Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email,password);
                }

            }
        });

        // tidak punya akun
        tv_tidakpunyaakun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginUser_Activity.this, RegisterActivity.class));
                finish();
            }
        });

        pd = new ProgressDialog(this);

    }

    private void forgot() {

        //alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");

        //set Linier Layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set dialog
       final EditText emilET = new EditText( this);
        emilET.setHint("Email");
        emilET.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emilET.setMinEms(16);

        linearLayout.addView(emilET);
        linearLayout.setPadding(10,10, 10, 10);

        builder.setView(linearLayout);

        //button
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //input email
                String email = emilET.getText().toString().trim();
                beginRecover(email);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dialog dismis

                dialogInterface.dismiss();

            }
        });

        builder.create().show();



    }

    private void beginRecover(String email) {
        pd.setMessage("Sending email..");
        pd.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(LoginUser_Activity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginUser_Activity.this,"Failed", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                //get and show proper
                Toast.makeText(LoginUser_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        pd.setMessage("Masuk...");
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginUser_Activity.this, ProfilUsers.class));
                            finish();
                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginUser_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginUser_Activity.this, ""+e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user !=null){
            Intent intent = new Intent(LoginUser_Activity.this, ProfilUsers.class);
            startActivity(intent);
                      finish();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "" +e.getMessage(), Toast.LENGTH_SHORT).show();
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();


                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
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

                            }
                            //show user Toast
                            Toast.makeText(LoginUser_Activity.this, ""+ user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginUser_Activity.this, ProfilUsers.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginUser_Activity.this, "Login Failed" , Toast.LENGTH_SHORT).show();
                        // updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginUser_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
