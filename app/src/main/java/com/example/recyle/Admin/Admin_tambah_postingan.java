package com.example.recyle.Admin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class Admin_tambah_postingan extends AppCompatActivity {

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

    EditText ed_Judulpost, ed_isipostingan;
    ImageView im_gambar1;
    Button upload_btn;

    //user info
    String name,email,uid,dp;

    Uri image_uri = null;

    //progres bar
    ProgressDialog pd;

    //info edit
    String edittitle, editisipost, editimage;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_post);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Post Terbaru");
        //enable tombol kembali di action bar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        pd = new ProgressDialog(this);
                //ini permissions array
        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        firebaseAuth = FirebaseAuth.getInstance();
         checkuserStatus();

        //init viewes
        ed_Judulpost = findViewById(R.id.tv_judulPost);
        ed_isipostingan = findViewById(R.id.tv_diskripsi_postingan);

        im_gambar1 = findViewById(R.id.tv_imagePostingan);
        upload_btn = findViewById(R.id.btn_upload_Postingan);
        Intent intent = getIntent();
         final String isUpdateKey = "" +intent.getStringExtra("key");
        final String editPostId = "" +intent.getStringExtra("editPostId");
        //validasi
        if (isUpdateKey.equals("editPost")){
            actionBar.setTitle("Update Post");
            upload_btn.setText("Update");
            loadPostData(editPostId);

        } else {
            //add
            actionBar.setTitle("Add New Post");
            upload_btn.setText("Upload");

        }
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

        //ambil camera dan galeri dari image view
        im_gambar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //tampilkan image pick dialog
                showImagePickDialog();

            }
        });

        // upload klik listener

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String judul_post = ed_Judulpost.getText().toString().trim();
                String isi_postingan = ed_isipostingan.getText().toString().trim();

                if (TextUtils.isEmpty(judul_post)){
                    Toast.makeText(Admin_tambah_postingan.this, "Masukkan Judul",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(isi_postingan)){
                    Toast.makeText(Admin_tambah_postingan.this, "Isi jangan Kosong",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isUpdateKey.equals("editPost")){
                    beginUpdate(judul_post, isi_postingan, editPostId);
                } else {
                    uploadData(judul_post, isi_postingan);
                }
            }
        });


    }

    private void beginUpdate(String judul_post, String isi_postingan, String editPostId) {
        pd.setMessage("Update Post");
        pd.show();

        if (!editimage.equals("noImage")){
            //with image
            updateWasWithImage(judul_post, isi_postingan, editPostId);

        }
        else if (im_gambar1.getDrawable() !=null){
            updateWithNowImage(judul_post, isi_postingan, editPostId);
        }

        else {
            //withou image
            updateWithoutImage(judul_post, isi_postingan, editPostId);
        }

    }

    private void updateWithoutImage(String judul_post, String isi_postingan, String editPostId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        //put info
        hashMap.put("uid", uid);
        hashMap.put("uName", name);
        hashMap.put("uEmail", email);
        hashMap.put("uDp", dp);
        hashMap.put("pJudul", judul_post);
        hashMap.put("pIsiPost", isi_postingan);
        hashMap.put("pImage", "noImage");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        ref.child(editPostId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Toast.makeText(Admin_tambah_postingan.this, "Update....",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Admin_tambah_postingan.this, ""+e.getMessage(),
                                Toast.LENGTH_SHORT).show();


                    }
                });


    }

    private void updateWithNowImage(final String judul_post, final String isi_postingan, final String editPostId) {

        String timeStamp  = String.valueOf(System.currentTimeMillis());
        String filePathAndName = "Posts/" + "post_" +timeStamp;

        Bitmap bitmap = ((BitmapDrawable)im_gambar1.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //image compress
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        ref.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //upload image ambil url nya
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());

                        String downloadUri = uriTask.getResult().toString();
                        if (uriTask.isSuccessful()) {
                            //uri is recived

                            HashMap<String, Object> hashMap = new HashMap<>();
                            //put info
                            hashMap.put("uid", uid);
                            hashMap.put("uName", name);
                            hashMap.put("uEmail", email);
                            hashMap.put("uDp", dp);
                            hashMap.put("pJudul", judul_post);
                            hashMap.put("pIsiPost", isi_postingan);
                            hashMap.put("pImage", downloadUri);

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                            ref.child(editPostId)
                                    .updateChildren(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                            Toast.makeText(Admin_tambah_postingan.this, "Update....",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(Admin_tambah_postingan.this, ""+e.getMessage(),
                                                    Toast.LENGTH_SHORT).show();


                                        }
                                    });
                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //gambar tidak terupload
                        pd.dismiss();
                        Toast.makeText(Admin_tambah_postingan.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void updateWasWithImage(final String judul_post, final String isi_postingan, final String editPostId) {
        StorageReference mPictuterRef = FirebaseStorage.getInstance().getReferenceFromUrl(editimage);
        mPictuterRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //imae deleted
                        String timeStamp  = String.valueOf(System.currentTimeMillis());
                        String filePathAndName = "Posts/" + "post_" +timeStamp;

                        Bitmap bitmap = ((BitmapDrawable)im_gambar1.getDrawable()).getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        //image compress
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        StorageReference ref = FirebaseStorage.getInstance().getReference().child(filePathAndName);
                        ref.putBytes(data)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //upload image ambil url nya
                                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                        while (!uriTask.isSuccessful());

                                        String downloadUri = uriTask.getResult().toString();
                                        if (uriTask.isSuccessful()) {
                                            //uri is recived

                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            //put info
                                            hashMap.put("uid", uid);
                                            hashMap.put("uName", name);
                                            hashMap.put("uEmail", email);
                                            hashMap.put("uDp", dp);
                                            hashMap.put("pJudul", judul_post);
                                            hashMap.put("pIsiPost", isi_postingan);
                                            hashMap.put("pImage", downloadUri);

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                            ref.child(editPostId)
                                                    .updateChildren(hashMap)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            pd.dismiss();
                                                            Toast.makeText(Admin_tambah_postingan.this, "Update....",
                                                                    Toast.LENGTH_SHORT).show();

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            pd.dismiss();
                                                            Toast.makeText(Admin_tambah_postingan.this, ""+e.getMessage(),
                                                                    Toast.LENGTH_SHORT).show();


                                                        }
                                                    });
                                        }


                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                       //gambar tidak terupload
                                        pd.dismiss();
                                        Toast.makeText(Admin_tambah_postingan.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(Admin_tambah_postingan.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void loadPostData(String editPostId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        //get detail
        Query fquery =  reference.orderByChild("pId").equalTo(editPostId);
        fquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //get data
                    edittitle = ""+ds.child("pJudul").getValue();
                    editisipost = ""+ds.child("pIsiPost").getValue();
                    editimage = ""+ds.child("pImage").getValue();

                    //set data view
                    ed_Judulpost.setText(edittitle);
                    ed_isipostingan.setText(editisipost);

                    //set image
                    if (!editimage.equals("noImage")){
                        try{
                            Picasso.get().load(editimage).into(im_gambar1);

                        }
                        catch (Exception e){

                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void uploadData(final String judul_post, final String isi_postingan) {
        pd.setMessage("Publikasi Postingan...");
        pd.show();

        final String timeStamp = String.valueOf(System.currentTimeMillis());
        String filpathAndName = "Posts/" + "post" + timeStamp;


        if (im_gambar1.getDrawable() !=null){
            //get image
            Bitmap bitmap = ((BitmapDrawable)im_gambar1.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //image compress
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data = baos.toByteArray();
            //postingan dengan gambar
            StorageReference ref = FirebaseStorage.getInstance().getReference().child(filpathAndName);
            ref.putBytes(data)
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
                                hashMap.put("pJudul", judul_post);
                                hashMap.put("pIsiPost", isi_postingan);
                                hashMap.put("pImage", downloadUri);
                                hashMap.put("pTime", timeStamp);

                                pd.dismiss();
                                //path to store post data
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
                                //put data in this refe
                                databaseReference.child(timeStamp).setValue(hashMap)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //add to database
                                                pd.dismiss();
                                                Toast.makeText(Admin_tambah_postingan.this, "Postingan dipublikasi.." ,
                                                        Toast.LENGTH_SHORT).show();
                                                //reset views
                                                ed_Judulpost.setText("");
                                                ed_isipostingan.setText("");
                                                im_gambar1.setImageURI(null);
                                                image_uri = null;


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //failde add
                                                pd.dismiss();
                                                Toast.makeText(Admin_tambah_postingan.this, "" +e.getMessage(),
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
                                Toast.makeText(Admin_tambah_postingan.this, ""+e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        else{
            //postingan tidak dengan gambar

            HashMap<Object, String> hashMap = new HashMap<>();
            //put post info
            hashMap.put("uid", uid);
            hashMap.put("uName", name);
            hashMap.put("uEmail", email);
            hashMap.put("uDp", dp);
            hashMap.put("pId", timeStamp);
            hashMap.put("pJudul", judul_post);
            hashMap.put("pIsiPost", isi_postingan);
            hashMap.put("pImage", "noImage");
            hashMap.put("pTime", timeStamp);

            //path to store post data
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
            //put data in this refe
            databaseReference.child(timeStamp).setValue(hashMap)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //add to database
                            pd.dismiss();
                            Toast.makeText(Admin_tambah_postingan.this, "Postingan dipublikasi..",
                                    Toast.LENGTH_SHORT).show();
                            //reset views
                            ed_Judulpost.setText("");
                            ed_isipostingan.setText("");
                            im_gambar1.setImageURI(null);
                            image_uri = null;


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failde add
                            pd.dismiss();
                            Toast.makeText(Admin_tambah_postingan.this, "" + e.getMessage(),
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

