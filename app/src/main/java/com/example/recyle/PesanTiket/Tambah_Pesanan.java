package com.example.recyle.PesanTiket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.MainActivity;
import com.example.recyle.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Tambah_Pesanan extends AppCompatActivity {
    ActionBar actionBar;
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;

    int v1, v2, hasil, hasil_total_pengunjung;
    int c1,c2, hsl;

    //user info
    String name,email,uid,dp;
    DatabaseReference databaseReferenceuserDb;

    EditText et_nama_pemesan, et_no_tlp, et_email, et_rombongandari, et_dewasa,et_anak_anak;
    TextView et_total_pengunjung, et_total_bayar_dewasa, et_total_bayar_anak, et_totalbayar, TV_tanggal;

    Button upload_btn, Btambah, BTN_tanggal;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pesanan);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Pemesanan Tiket Kebun Binatang");
        //enable tombol kembali di action bar
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        pd = new ProgressDialog(this);
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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        et_nama_pemesan =findViewById(R.id.ed_namapemesan);
        et_no_tlp = findViewById(R.id.ed_notlp);
        et_email =  findViewById(R.id.ed_email);
        et_rombongandari =  findViewById(R.id.ed_rombongan);
        et_dewasa =findViewById(R.id.ed_dewasa);
        et_anak_anak =  findViewById(R.id.ed_anakanak);
        //textview
        et_total_pengunjung = findViewById(R.id.tv_total_pengunjung);
        et_total_bayar_dewasa = findViewById(R.id.tv_total_bayar_dewasa);
        et_total_bayar_anak = findViewById(R.id.tv_total_bayar_anak);
        et_totalbayar = findViewById(R.id.ed_totalbayar);

        //tanggal
        TV_tanggal = findViewById(R.id.tv_tanggal);
        BTN_tanggal = findViewById(R.id.btn_tanggal);

        Intent incomingIntent = getIntent();
        String date = incomingIntent.getStringExtra("date");
        TV_tanggal.setText(date);
        BTN_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Tambah_Pesanan.this, CalenderActivity.class);
                startActivity(intent);

            }
        });


        Btambah =findViewById(R.id.cek);
        Btambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check();
                if(et_dewasa.getText().toString().length()==0){
                    //jika form Email belum di isi / masih kosong
                    et_dewasa.setError("Tidak boleh kosong!");
                }else if(et_anak_anak.getText().toString().length()==0){
                    et_anak_anak.setError("Tidak oleh Kosong!!");
                }else
                    total_pengunjung();
                dewasa();
                anak();
                proses_tambah();
            }
        });



        // upload klik listener
        upload_btn = findViewById(R.id.btn_next);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nama_pemesan = et_nama_pemesan.getText().toString().trim();
                final String no_tlp = et_no_tlp.getText().toString().trim();
                final String e_mail = et_email.getText().toString().trim();
                final String rombongan_dari = et_rombongandari.getText().toString().trim();
                final String tanggal = TV_tanggal.getText().toString().trim();
                final String dewasa = et_dewasa.getText().toString().trim();
                final String anak_anak = et_anak_anak.getText().toString().trim();
                final String total_pengunjung = et_total_pengunjung.getText().toString().trim();
                final String total_bayar_dewasa = et_total_bayar_dewasa.getText().toString().trim();
                final String total_bayar_anak = et_total_bayar_anak.getText().toString().trim();
                final String total_bayar = et_totalbayar.getText().toString().trim();
                final String status = "Belum Bayar";

                if (TextUtils.isEmpty(nama_pemesan)){
                    Toast.makeText(Tambah_Pesanan.this, "Masukkan Nama Anda",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (no_tlp.length() != 12 ) {
                    Toast.makeText(Tambah_Pesanan.this, "Nomor HP Anda Tidak Valid",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (no_tlp.isEmpty()) {
                    Toast.makeText(Tambah_Pesanan.this, "Nomor HP Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(e_mail).matches()){
                    et_email.setError("Invalid Email");
                    et_email.setFocusable(true);
                    return;
                }
                if (TextUtils.isEmpty(rombongan_dari)){
                    Toast.makeText(Tambah_Pesanan.this, "Dari mana",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tanggal)){
                    Toast.makeText(Tambah_Pesanan.this, "Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(dewasa)){
                    Toast.makeText(Tambah_Pesanan.this, "Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(anak_anak)){
                    Toast.makeText(Tambah_Pesanan.this, "Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    new android.app.AlertDialog.Builder(com.example.recyle.PesanTiket.Tambah_Pesanan.this)
                            .setTitle("Warning !!!")
                            .setMessage("Do you want Save?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    uploadData(nama_pemesan, no_tlp, e_mail, rombongan_dari, tanggal, dewasa, anak_anak, total_bayar,
                                            total_bayar_anak, total_bayar_dewasa, total_pengunjung, status);
                                    Intent intent = new Intent(Tambah_Pesanan.this, UploadBukti.class);
                                    intent.putExtra("nama_pemesan", et_nama_pemesan.getText().toString() );
                                    intent.putExtra("no_tlp", et_no_tlp.getText().toString() );
                                    intent.putExtra("rombongan_dari", et_rombongandari.getText().toString() );
                                    intent.putExtra("total_pengunjung", et_total_pengunjung.getText().toString() );
                                    intent.putExtra("total_bayar", et_totalbayar.getText().toString() );

                                    startActivity(intent);
                                    finish();
                                    //com.example.recyle.PesanTiket.Tambah_Pesanan.this.saving();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();

                }

            }
        });

    }

    private void uploadData(final String nama_pemesan, final String no_tlpon, final String e_mail, final String rombongan_dari, final String tanggal,
                            final String anak, final String dewasa, final String total_bayar, final String total_bayar_anak,
                            final String total_bayar_dewasa, final String total_pengunjung, final String status){
        pd.setMessage("Pesananan Ditambahkan...");
        pd.show();

        final String timeStamp = String.valueOf(System.currentTimeMillis());
        HashMap<Object, String> hashMap = new HashMap<>();
        //put post info
        hashMap.put("uid", uid);
        hashMap.put("uName", name);
        hashMap.put("uEmail", email);
        hashMap.put("uDp", dp);
        hashMap.put("pId_Pesanan", timeStamp);
        hashMap.put("pNama_Pemesan", nama_pemesan);
        hashMap.put("pNotelpon", no_tlpon);
        hashMap.put("pEmail", e_mail);
        hashMap.put("pRombongan_dari", rombongan_dari);
        hashMap.put("pTanggal", tanggal);
        hashMap.put("pAnak_anak",anak );
        hashMap.put("pDewasa", dewasa);
        hashMap.put("pTime", timeStamp);
        hashMap.put("pTotal_bayar", total_bayar);
        hashMap.put("pTotal_bayar_anak", total_bayar_anak);
        hashMap.put("pTotal_bayar_dewasa", total_bayar_dewasa);
        hashMap.put("pTotal_pengunjung", total_pengunjung);
        hashMap.put("status", status);
        //path to store post data

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Pesanan Tiket");
        pd.dismiss();
        //put data in this refe
        databaseReference.child(timeStamp).setValue(hashMap)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //add to database
                        pd.dismiss();
                        //reset views
                        et_nama_pemesan.setText("");
                        et_no_tlp.setText("");
                        et_email.setText("");
                        et_rombongandari.setText("");
                        TV_tanggal.setText("");
                        et_anak_anak.setText("");
                        et_dewasa.setText("");
                        et_total_pengunjung.setText("");
                        et_total_bayar_dewasa.setText("");
                        et_total_bayar_anak.setText("");
                        et_totalbayar.setText("");
                                           }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failde add
                        pd.dismiss();
                        Toast.makeText(Tambah_Pesanan.this, "" +e.getMessage(),
                                Toast.LENGTH_SHORT).show();

                    }
                });
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


    //disini adalah metode
    //methode jumlah total pengunjung
    public void total_pengunjung(){
        v1 = Integer.parseInt(et_dewasa.getText().toString());
        v2 = Integer.parseInt(et_anak_anak.getText().toString());
        hasil_total_pengunjung=v1+v2;
        et_total_pengunjung.setText(Integer.toString(hasil_total_pengunjung));
    }
    //metode
    //metode jumlah total bayar dewasa
    public void dewasa(){
        c1 = Integer.parseInt(et_dewasa.getText().toString());
        int dwsa = 50000;
        hsl = c1*dwsa;
        //hasilnya
        et_total_bayar_dewasa.setText(Integer.toString(hsl));
    }
    //metode
    //ini metode untuk menghitung total bayar anak
    public void anak(){
        c2 = Integer.parseInt(et_anak_anak.getText().toString());
        int ank= 30000;
        hsl = c2*ank;
        //hasil
        et_total_bayar_anak.setText(Integer.toString(hsl));

    }

    public void proses_tambah(){
        v1 = Integer.parseInt(et_dewasa.getText().toString());
        try {
            v1 = new Integer(et_dewasa.getText().toString());
        } catch (NumberFormatException e) {
            v1 = 0 ; // your default value
        }
        v2 = Integer.parseInt(et_anak_anak.getText().toString());
        try {
            v2 = new Integer(et_anak_anak.getText().toString());
        } catch (NumberFormatException e) {
            v2 = 0 ; // your default value
        }
        int dewa= 50000;
        int anak = 30000;
        hasil = (v1*dewa)+(v2*anak);
        //perhitungan
        et_totalbayar.setText(Integer.toString(hasil));
    }


}
