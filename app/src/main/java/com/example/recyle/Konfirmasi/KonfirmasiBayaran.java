package com.example.recyle.Konfirmasi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Adapter.AdapterBuktiPembayaran;
import com.example.recyle.Model.ModelBuktiPembayaran;
import com.example.recyle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KonfirmasiBayaran extends AppCompatActivity {
    ActionBar actionBar;
    ProgressDialog pd;

    TextView TVstatussekarang;
    Button upload_btn, sudah_bayar, belum_bayar, kadalu;
    Spinner tes1;

    //info edit
    String editstat, atasnama;

    String name,email,uid,dp;


    RecyclerView recyclerView;

    List<ModelBuktiPembayaran> buktiPembayaranList;
    AdapterBuktiPembayaran adapterBuktiPembayaran;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_konfirbayar);


        actionBar = getSupportActionBar();
        actionBar.setTitle("Post Terbaru");
        //enable tombol kembali di action bar

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        pd = new ProgressDialog(this);
        //init viewes
        TVstatussekarang = findViewById(R.id.statsusnya);
        //initbutton
        upload_btn = findViewById(R.id.btn_upload_konfir);
        sudah_bayar = findViewById(R.id.sdhbyar);
        belum_bayar = findViewById(R.id.blmbayar);
        kadalu = findViewById(R.id.kadaluwarsa);

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

        checkstatus();

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stat1 = TVstatussekarang.getText().toString().trim();

                if (TextUtils.isEmpty(stat1)){
                    Toast.makeText(KonfirmasiBayaran.this, "Konfirmasi",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isUpdateKey.equals("editPost")){
                    beginUpdate(stat1, editPostId);
                } else {
                    uploadData(stat1);
                }
            }
        });
        recyclerView = findViewById(R.id.postsRecyclerview);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getApplicationContext());
        //show newest post first, for this load  from last
        LayoutManager.setStackFromEnd(true);
        LayoutManager.setReverseLayout(true);
        //set layout to recyclerviews
        recyclerView.setLayoutManager(LayoutManager);

        //init post list
        buktiPembayaranList = new ArrayList<>();
        loadPosts();
        return;
    }


    private void checkstatus(){

        belum_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = findViewById(R.id.statsusnya);

                tv.setText("Belum Bayar");
                tv.setTextColor(Color.RED);
            }
        });

        sudah_bayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = findViewById(R.id.statsusnya);

                tv.setText("Sudah Bayar");
                tv.setTextColor(Color.GREEN);
            }
        });

        kadalu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = findViewById(R.id.statsusnya);

                tv.setText("Kadaluwarsa");
                tv.setTextColor(Color.YELLOW);
            }
        });
    }

    private void loadPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Bukti Pembayaran");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                buktiPembayaranList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelBuktiPembayaran modelBuktiPembayaran = ds.getValue(ModelBuktiPembayaran.class);

                    buktiPembayaranList.add(modelBuktiPembayaran);

                    //adapter
                    adapterBuktiPembayaran = new AdapterBuktiPembayaran(getApplication(), buktiPembayaranList);
                    recyclerView.setAdapter(adapterBuktiPembayaran);
                    //set adapter to recyclerview
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplication(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void beginUpdate(String stat1, String editPostId) {
        pd.setMessage("Update Post");
        pd.show();
            updateWithoutImage(stat1, editPostId);
    }


    private void updateWithoutImage(String stat1,  String editPostId) {
        HashMap<String, Object> hashMap = new HashMap<>();
        //put info
        hashMap.put("status", stat1);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Pesanan Tiket");
        ref.child(editPostId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pd.dismiss();
                        Toast.makeText(KonfirmasiBayaran.this, "Update....",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(KonfirmasiBayaran.this, ""+e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void loadPostData(String editPostId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pesanan Tiket");
        //get detail
        Query fquery =  reference.orderByChild("pId_Pesanan").equalTo(editPostId);
        fquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    //get data
                    editstat = ""+ds.child("status").getValue();

                    //set data view
                    TVstatussekarang.setText(editstat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void uploadData(final String stat1) {
        pd.setMessage("Publikasi Postingan...");
        pd.show();

            HashMap<Object, String> hashMap = new HashMap<>();
            //put post inf
            hashMap.put("status", stat1);

            //path to store post data
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pesanan Tiket");
            //put data in this refe
            databaseReference.child(stat1).setValue(hashMap)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //add to database
                            pd.dismiss();
                            Toast.makeText(KonfirmasiBayaran.this, " dipublikasi..",
                                    Toast.LENGTH_SHORT).show();
                            //reset views
                            TVstatussekarang.setText("");


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failde add
                            pd.dismiss();
                            Toast.makeText(KonfirmasiBayaran.this, "" + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
        }

}
