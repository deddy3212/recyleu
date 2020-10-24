package com.example.recyle.TambahData;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.recyle.MainActivity;
import com.example.recyle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.HashMap;

public class TambahDataRekreasi extends AppCompatActivity {

    ActionBar actionBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReferenceuserDb;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constans
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;



    //persmission array
    String [] cameraPermissions;
    String [] storagePermissions;

    ProgressDialog pd;


    EditText ed_JudulRekreasi, ed_isiRekreasi;
    ImageView im_gambar1;
    Button upload_btn;

    //user info
    String name,email,uid,dp;

    Uri image_uri = null;

    //progres bar


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tambah_rekreasi);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Add Rekreasi");
        //enable tombol kembali di action bar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        pd = new ProgressDialog(this);



        //ini permissions array
        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();
        checkuserStatus();

        actionBar.setSubtitle(email);

        //get some info of current user to include in a post
        databaseReferenceuserDb = FirebaseDatabase.getInstance().getReference("Users");
        Query query = databaseReferenceuserDb.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    name = ""+ ds.child("name").getValue();
                    email = ""+ ds.child("email").getValue();
                    dp = ""+ ds.child("image").getValue();


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //init viewes
        ed_JudulRekreasi = findViewById(R.id.tv_judulRekreasi);
        ed_isiRekreasi = findViewById(R.id.tv_diskripsi_Rekreasi);

        im_gambar1 = findViewById(R.id.tv_imageInteraksi);

        //ambil camera dan galeri dari image view
        im_gambar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tampilkan image pick dialog
                showImagePickDialog();

            }
        });

        // upload klik listener
        upload_btn = findViewById(R.id.btn_upload_Rekreasi);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String judul_rekreasi = ed_JudulRekreasi.getText().toString().trim();
                String isi_rekreasi = ed_isiRekreasi.getText().toString().trim();

                if (TextUtils.isEmpty(judul_rekreasi)){
                    Toast.makeText(TambahDataRekreasi.this, "Masukkan Judul",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(isi_rekreasi)){
                    Toast.makeText(TambahDataRekreasi.this, "Isi jangan Kosong",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (image_uri == null){
                    uploadData(judul_rekreasi, isi_rekreasi, "noImage");
                }
                else {

                    uploadData(judul_rekreasi, isi_rekreasi, String.valueOf(image_uri));

                }

            }
        });


    }

    private void uploadData(final String judul_rekreasi, final String isi_rekreasi, final String uri) {
        pd.setMessage("Publikasi Rekreasi...");
        pd.show();


        final String timeStamp = String.valueOf(System.currentTimeMillis());

        String filpathAndName = "Rekreasi/" + "rekreasi" + timeStamp;


        if (!uri.equals("noImage")){
            //postingan dengan gambar
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filpathAndName);
            ref.putFile(Uri.parse(uri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());

                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()){

                                HashMap<Object, String> hashMap = new HashMap<>();
                                //put post info
                                hashMap.put("uid", uid);
                                hashMap.put("uName", name);
                                hashMap.put("uEmail", email);
                                hashMap.put("uDp", dp);
                                hashMap.put("pId", timeStamp);
                                hashMap.put("pJudul_Rekreasi", judul_rekreasi);
                                hashMap.put("pIsi_Rekreasii", isi_rekreasi);
                                hashMap.put("pImage", downloadUri);
                                hashMap.put("pTime", timeStamp);

                                //path to store post data
                                pd.dismiss();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Rekreasi");
                                //put data in this refe
                                //oke
                                databaseReference.child(timeStamp).setValue(hashMap)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //add to database
                                                pd.dismiss();
                                                Toast.makeText(TambahDataRekreasi.this, "Rekreasi dipublikasi.." ,
                                                        Toast.LENGTH_SHORT).show();
                                                //reset views
                                                ed_JudulRekreasi.setText("");
                                                ed_isiRekreasi.setText("");
                                                im_gambar1.setImageURI(null);
                                                image_uri = null;


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failde add
                                                pd.dismiss();
                                                Toast.makeText(TambahDataRekreasi.this, "" +e.getMessage(),
                                                        Toast.LENGTH_SHORT).show();

                                            }
                                        });



                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(TambahDataRekreasi.this, ""+e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            //postingan tidak dengan gambar

            HashMap<Object, String> hashMap = new HashMap<>();
            //put post info
            hashMap.put("uid", uid);
            hashMap.put("uName", name);
            hashMap.put("uEmail", email);
            hashMap.put("uDp", dp);
            hashMap.put("pId", timeStamp);
            hashMap.put("pJudulRekreasi", judul_rekreasi);
            hashMap.put("pIsiRekreasi", isi_rekreasi);
            hashMap.put("pImage", "noImage");
            hashMap.put("pTime", timeStamp);

            //path to store post data
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Rekreasi");
            //put data in this refe
            databaseReference.child(timeStamp).setValue(hashMap)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //add to database
                            pd.dismiss();
                            Toast.makeText(TambahDataRekreasi.this, "Rekreasi dipublikasi.." ,
                                    Toast.LENGTH_SHORT).show();
                            //reset views
                            ed_JudulRekreasi.setText("");
                            ed_isiRekreasi.setText("");
                            im_gambar1.setImageURI(null);
                            image_uri = null;



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failde add
                            pd.dismiss();
                            Toast.makeText(TambahDataRekreasi.this, "" +e.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    });

        }
    }

    private void checkuserStatus() {
        //get current user
        FirebaseUser user =  firebaseAuth.getCurrentUser();
        if (user != null){
            //user is signed
            //set email of logged in user
            email = user.getEmail();
            uid = user.getUid();

            //
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void showImagePickDialog() {
        String [] options = {"Kamera","Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Gambar Dari");
        //set options to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int pilih) {
                //item click handle

                if (pilih==0){
                    //clicked camera
                    if (!checkCameraPermissions()){
                        requestCameraPermissions();
                    }
                    else {
                        pickFromCamera();
                    }
                }
                if (pilih==1){
                    //gallery checked
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

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        //intent to pick image from camera
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        image_uri = getContentResolver().insert( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermissions(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermissions(){
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermissions(){
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //handle permissions

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if ( cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }
                    else {
                        Toast.makeText(this, "Camera & Storage both permissions are neccessary...",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        pickFromGallery();
                    }
                    else {
                        Toast.makeText(this,"Please enable Storage permissions ...",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                }

            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();

                //set to imageview
                im_gambar1.setImageURI(image_uri);
            }
            else if (requestCode == IMAGE_PICK_CAMERA_CODE){
                im_gambar1.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
