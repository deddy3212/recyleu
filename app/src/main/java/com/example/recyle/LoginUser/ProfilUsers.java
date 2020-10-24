package com.example.recyle.LoginUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recyle.HomeFragment;
import com.example.recyle.MenuUser.Kadaluwarsa;
import com.example.recyle.MenuUser.Menunggu;
import com.example.recyle.MenuUser.Total;
import com.example.recyle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ProfilUsers extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView tvemail, nameTV, phonetv;
    ImageView notif, avatar, sampul, menunggu, tiketlama, keluar;
    FloatingActionButton fab;

    ProgressDialog pd;
    Uri image_uri;

    //for chekingprofile or cover
    String profileOrCoverPhoto;

    //storage
    StorageReference storageReference;
    String storagePath = "Users_Profile_Cover_Imgs/";

    //permission constans
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //arrays of permissions
    String cameraPermissions[];
    String storagePermissions[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_users);
        //actionbar and its title
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Profile ");
        notif = findViewById(R.id.NotifikasiUser);
        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilUsers.super.getApplicationContext(), Total.class));
            }
        });
        menunggu = findViewById(R.id.NotifikasiUser3);
        menunggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(ProfilUsers.super.getApplicationContext(), Menunggu.class));
            }
        });
        tiketlama = findViewById(R.id.NotifikasiUserlama);
        tiketlama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilUsers.super.getApplicationContext(), Kadaluwarsa.class));
            }
        });
        keluar = findViewById(R.id.metu);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfir();
            }
        });
       // init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();

        //init arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //ini views
        tvemail =findViewById(R.id.emailTV);
        nameTV = findViewById(R.id.nameTV);
        phonetv = findViewById(R.id.phone);
        avatar = findViewById(R.id.avatarId);
        sampul = findViewById(R.id.gambarsampul);
        fab = findViewById(R.id.editan);

        pd = new ProgressDialog(this);

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds:dataSnapshot.getChildren()){
                    String email = ""+ds.child("email").getValue();
                    String name = ""+ds.child("name").getValue();
                    String phone = ""+ds.child("phone").getValue();
                    String image = ""+ds.child("image").getValue();
                    String cover = ""+ds.child("cover").getValue();
                    //set data
                    tvemail.setText(email);
                    nameTV.setText(name);
                    phonetv.setText(phone);

                    try{
                        // if image is receive then set
                        Picasso.get().load(image).into(avatar);
                    }
                    catch (Exception e){
                        //if image set default
                        Picasso.get().load(R.drawable.ic_add_a_photo_white_24dp).into(avatar);

                    }

                    try{
                        // if image is receive then set
                        Picasso.get().load(cover).into(sampul);
                    }
                    catch (Exception e){
                        //if image set default

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //editan button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditProfilDialog();
            }
        });
    }

    private boolean checkStoragePermissions(){
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermissions(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }


    private boolean checkCameraPermissions(){

        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermissions(){
            ActivityCompat.requestPermissions(this, storagePermissions, CAMERA_REQUEST_CODE);

    }


    private void showEditProfilDialog() {
        /*menampilkan pilihan dari show dialog
        *1) Edit Profil
        *2) Edit Sampul
        *3) Edit Nama
        *4) Edit Nomor Telepon*/

        //opsi untuk menampilkan dialog
        String options [] = {"Edit Nama", "Edit Nomor Telepon"};
        //a;ert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setTitel
        builder.setTitle("Pilih Aksi");
        //set item dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0){
                    //edit nama
                    pd.setMessage("Memperbaharui Nama");
                    showNamePhoneUpdateDialog("name");
                }
                else if (which == 1){
                    //edit no hp
                    pd.setMessage("Memperbaharui No Hp");
                    showNamePhoneUpdateDialog("phone");
                }

            }
        });

        builder.create().show();

    }

    private void showNamePhoneUpdateDialog(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update" + key);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);

        //edit text
        final EditText editText = new EditText(this);
        editText.setHint("Enter" + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);
        //add button dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //input text
                String value = editText.getText().toString().trim();
                //validasi apabila user malakukan entaer atau tidak
                if (!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    pd.dismiss();
                                    Toast.makeText(ProfilUsers.this, "Updated..", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    pd.dismiss();
                                    Toast.makeText(ProfilUsers.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                }
                else {
                    Toast.makeText(ProfilUsers.this, "Please Enter" +key, Toast.LENGTH_SHORT).show();
                }


            }
        });
        //add button dialog to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        //create and show
        builder.create().show();
    }

    private void ShowPicDialog() {
        String options [] = {"Camera" , "Gallery"};
        //a;ert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setTitel
        builder.setTitle("Pilih Gambar Dari");
        //set item dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0){
                    //klik Kamera
                    if (!checkCameraPermissions()){
                        requestCameraPermissions();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                else if (which == 1){
                    //klik Gallery
                    if (!checkStoragePermissions()){
                        requestStoragePermissions();
                    }
                    else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                //picking from camera
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults [0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults [1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        //permissions enable
                        pickFromCamera();
                    }
                    else {
                        //permissions denied
                        Toast.makeText(this, "Please Enable & Camera storage permissions",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                //picking from Gallery
                if (grantResults.length>0){
                    boolean writeStorageAccepted = grantResults [1] == PackageManager.PERMISSION_GRANTED;
                    if ( writeStorageAccepted){
                        //permissions enable
                        pickFromGallery();
                    }
                    else {
                        //permissions denied
                        Toast.makeText(this, "Please Enable storage permissions",Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if (requestCode == RESULT_OK){

           if (requestCode == IMAGE_PICK_GALLERY_CODE){
               //image is picking from gallery
               image_uri = data.getData();
               uploadProfileCoverPhoto(image_uri);
           }
           if (requestCode == IMAGE_PICK_CAMERA_CODE){

               uploadProfileCoverPhoto(image_uri);

           }
       }

    }

    private void uploadProfileCoverPhoto(Uri uri) {
         pd.show();
        //path name of picture to storage firebase
        String filePathName = storagePath+ "" +profileOrCoverPhoto+ "_"+ user.getUid();
        StorageReference ref = storageReference.child(filePathName);
        ref.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image sudah di upload ke database
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        //check if image is upload or not and url is received
                        if (uriTask.isSuccessful()){
                            //image uploaded
                            //add  update database
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(profileOrCoverPhoto, downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                            Toast.makeText(getApplicationContext(), "Image updated...", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(getApplicationContext(), "Error Updating Image...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        else {
                            //error
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "some error accored", Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



    }
    private void pickFromGallery() {
        //pick image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }
    private void pickFromCamera() {
        //intent of picking image from camera device
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descripstion");
        //put image
        image_uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // intent to starts
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

        
    }

    private void cekUserstatus(){
        //get current user

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
           tvemail.setText(user.getEmail());
        }
        else {
            startActivity(new Intent(ProfilUsers.this, HomeFragment.class));
            finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        cekUserstatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout_user,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_user) {
            konfir();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void konfir(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Warning !!!");
        builder.setMessage("Do you want to Logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                logout();
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
        alert.getButton(BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void logout(){
        Toast.makeText(ProfilUsers.this, "Logout Berhasil.", Toast.LENGTH_LONG).show();
        Intent logout = new Intent();
        setResult(RESULT_OK, logout);
        FirebaseAuth.getInstance().signOut();
        finish();
    }

}
