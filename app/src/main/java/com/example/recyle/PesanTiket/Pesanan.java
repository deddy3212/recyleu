package com.example.recyle.PesanTiket;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Pesanan extends AppCompatActivity {
    private EditText et_namapemesan, et_notlp, et_email, et_rombongandari, et_dewasa, et_anakanak;
    TextView et_total_pengunjung, et_total_bayar_dewasa, et_total_bayar_anak, et_totalbayar, et_tanggal;
    Button btn1;
    Button Btambah;

    private DatabaseReference databasepesanan;
    public String id_namapemesan;
    String id_notlp;
    String id_email;
    String id_rombongan;
    String id_tanggal;
    String id_dewasa;
    String id_anakanak;
    String id_total_pengunjung;
    String id_total_bayar_dewasa;
    String id_total_bayar_anak;
    String id_totalbayar;

    int v1, v2, hasil, hasil_total_pengunjung;
    int c1,c2, hsl;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pesanan);
        databasepesanan = FirebaseDatabase.getInstance().getReference("Transaksi");
        et_namapemesan =findViewById(R.id.ed_namapemesan);
        et_notlp = findViewById(R.id.ed_notlp);
        et_email =  findViewById(R.id.ed_email);
        et_rombongandari =  findViewById(R.id.ed_rombongan);
        et_tanggal =findViewById(R.id.tv_tanggal);
        et_dewasa =findViewById(R.id.ed_dewasa);
        et_anakanak =  findViewById(R.id.ed_anakanak);
        et_totalbayar = findViewById(R.id.ed_totalbayar);
        //textview
        et_total_pengunjung = findViewById(R.id.tv_total_pengunjung);
        et_total_bayar_dewasa = findViewById(R.id.tv_total_bayar_dewasa);
        et_total_bayar_anak = findViewById(R.id.tv_total_bayar_anak);

        Btambah =findViewById(R.id.cek);
        Btambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_dewasa.getText().toString().length()==0){
                    //jika form Email belum di isi / masih kosong
                    et_dewasa.setError("Tidak boleh kosong!");
                }else if(et_anakanak.getText().toString().length()==0){
                    et_anakanak.setError("Tidak oleh Kosong!!");
                }else
                    total_pengunjung();
                dewasa();
                anak();
                proses_tambah();
            }
        });

        btn1 = findViewById(R.id.btn_next);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cek_pesanan();
            }
        });
        progressDialog = new ProgressDialog(this);
    }

    //disini adalah metode
    //methode jumlah total pengunjung
    public void total_pengunjung(){
        v1 = Integer.parseInt(et_dewasa.getText().toString());
        v2 = Integer.parseInt(et_anakanak.getText().toString());
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
        c2 = Integer.parseInt(et_anakanak.getText().toString());
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
        v2 = Integer.parseInt(et_anakanak.getText().toString());
        try {
            v2 = new Integer(et_anakanak.getText().toString());
        } catch (NumberFormatException e) {
            v2 = 0 ; // your default value
        }
        int dewa= 50000;
        int anak = 30000;
        hasil = (v1*dewa)+(v2*anak);
        //perhitungan
        et_totalbayar.setText(Integer.toString(hasil));
    }


    private void konfirInput() {
        new android.app.AlertDialog.Builder(com.example.recyle.PesanTiket.Pesanan.this)
                .setTitle("Warning !!!")
                .setMessage("Do you want Save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        com.example.recyle.PesanTiket.Pesanan.this.saving();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private void konfir() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Success !!!");
        builder.setMessage("Do you want to save data again?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                et_namapemesan.setText("");
                et_notlp.setText("");
                et_email.setText("");
                et_rombongandari.setText("");
                et_tanggal.setText("");
                et_dewasa.setText("");
                et_anakanak.setText("");
                et_total_pengunjung.setText("");
                et_total_bayar_dewasa.setText("");
                et_total_bayar_anak.setText("");
                et_totalbayar.setText("");
                et_namapemesan.requestFocus();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
                kembali();
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private void kembali() {
        Intent back = new Intent();
        setResult(RESULT_OK, back);
        finish();
    }


    private void cek_pesanan() {
        id_namapemesan = et_namapemesan.getText().toString().trim();
        id_notlp = et_notlp.getText().toString().trim();
        id_email = et_email.getText().toString().trim();
        id_rombongan = et_rombongandari.getText().toString().trim();
        id_tanggal = et_tanggal.getText().toString().trim();
        id_dewasa = et_dewasa.getText().toString().trim();
        id_anakanak = et_anakanak.getText().toString().trim();
        id_total_pengunjung=et_total_pengunjung.getText().toString().trim();
        id_total_bayar_dewasa = et_total_bayar_dewasa.getText().toString().trim();
        id_total_bayar_anak = et_total_bayar_anak.getText().toString().trim();
        id_totalbayar = et_totalbayar.getText().toString().trim();

        if (id_namapemesan.isEmpty()) {
            et_namapemesan.setError("Input Nama Pemesan");
            et_namapemesan.requestFocus();
            return;
        }
        if (id_notlp.isEmpty()) {
            et_notlp.setError(getString(R.string.input_error_nama));
            et_notlp.requestFocus();
            return;
        }
        if (id_email.isEmpty()) {
            et_email.setError("Input Email!");
            et_email.requestFocus();
            return;
        }
        if (id_rombongan.isEmpty()) {
            et_rombongandari.setError("Input Rombongan");
            et_rombongandari.requestFocus();
            return;
        }
        if (id_tanggal.isEmpty()){
            et_tanggal.setError("Pilih Tanggalnya!");
            et_tanggal.requestFocus();
            return;
        }

        if (id_dewasa.isEmpty()) {
            et_dewasa.setError("Input Angka");
            et_dewasa.requestFocus();
            return;
        }
        if (id_anakanak.isEmpty()) {
            et_anakanak.setError("Input Angka");
            et_anakanak.requestFocus();
            return;
        }

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Transaksi");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(et_namapemesan.getText().toString()).exists()) {
                    progressDialog.dismiss();
                    Toast.makeText(Pesanan.this, "Name already exists",
                            Toast.LENGTH_SHORT).show();
                    et_namapemesan.requestFocus();

                } else {
                    progressDialog.dismiss();
                    konfirInput();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void saving() {
        Transaksi upload = new Transaksi
                (id_namapemesan, id_notlp, id_email, id_rombongan, id_tanggal,
                        id_dewasa, id_anakanak,id_total_pengunjung,id_total_bayar_dewasa,
                        id_total_bayar_anak, id_totalbayar);
        databasepesanan.child(id_namapemesan).setValue(upload);
        konfir();

    }


}
