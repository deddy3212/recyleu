package com.example.recyle.PesanTiket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.R;

public class UploadBukti extends AppCompatActivity {
    ActionBar actionBar;
    TextView nama, nohp, rombongan, jml_pengunjung, total_bayar;
    Button byr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bukti_pembayaran);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Check");
        //enable tombol kembali di action bar


        nama = findViewById(R.id.edt_nama_pemesan);
        nohp = findViewById(R.id.TV_nomor);
        rombongan = findViewById(R.id.TV_rombong);
        jml_pengunjung = findViewById(R.id.TV_jml_pengunjung);
        total_bayar = findViewById(R.id.TV_total);
        //set data
        nama.setText(getIntent().getStringExtra("nama_pemesan"));
        nohp.setText(getIntent().getStringExtra("no_tlp"));
        rombongan.setText(getIntent().getStringExtra("rombongan_dari"));
        jml_pengunjung.setText(getIntent().getStringExtra("total_pengunjung"));
        total_bayar.setText(getIntent().getStringExtra("total_bayar"));


        byr = findViewById(R.id.bayar);
        byr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadBukti.this, Pembayaran.class);
                startActivity(intent);
                finish();

            }
        });


    }
}
