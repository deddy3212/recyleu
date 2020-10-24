package com.example.recyle.Admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.BuktiPembayaran.Bukti_bayar;
import com.example.recyle.KelolaUpdateDelete.Coba;
import com.example.recyle.Konfirmasi.Konfirmasilah;
import com.example.recyle.Laporan.Pendapatan;
import com.example.recyle.R;
import com.example.recyle.TambahData.TambahDataFasilitas;
import com.example.recyle.TambahData.TambahDataKonservasi;
import com.example.recyle.TambahData.TambahDataRekreasi;
import com.example.recyle.TambahData.TambahInteraksi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import static android.content.DialogInterface.BUTTON_NEGATIVE;

public class Dashboard_Admin extends AppCompatActivity implements ModalBottomSheet.ActionListener{


    ImageView  tambahadminIV, mAdd_dataTV, bukti, lapor, tes, tes2,ScaneCmera, kelolah;

    TextView tvemail, nameTV, phonetv;
    ProgressDialog pd;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_admin);

        String t = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        setTitle(" Administrator");
        TextView user = findViewById(R.id.emailTV);
        user.setText(t);


        tes2 = findViewById(R.id.teslah2);
        tes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konfir();
            }
        });

        bukti = findViewById(R.id.laporan_bukti);
        bukti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard_Admin.this, Bukti_bayar.class));

            }
        });


        kelolah = findViewById(R.id.kelola);
        kelolah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard_Admin.this, Coba.class));
            }
        });

        tes = findViewById(R.id.teslah);
        tes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard_Admin.this, Konfirmasilah.class));

            }
        });



        lapor = findViewById(R.id.laporan);
        lapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard_Admin.this, Pendapatan.class));
            }
        });

        ScaneCmera = findViewById(R.id.scene);
        ScaneCmera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              sceneQR();
            }
        });


        //
        mAdd_dataTV = findViewById(R.id.add_dataTV);
        mAdd_dataTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModalBottomSheet modalBottomSheet = new ModalBottomSheet();
                modalBottomSheet.setActionListener(Dashboard_Admin.this);

                modalBottomSheet.show(getSupportFragmentManager(),"modalmenu`````");
            }
        });



        tambahadminIV = findViewById(R.id.tambhadminTV);
        tambahadminIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard_Admin.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.logout);

    }


    private void sceneQR() {
        Intent intent = new Intent(Dashboard_Admin.this, Scan.class);
        startActivity(intent);
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

    @Override
    public void onBackPressed() {
        konfir();
    }

    private void logout(){
        Toast.makeText(Dashboard_Admin.this, "Logout Berhasil.", Toast.LENGTH_LONG).show();
        Intent logout = new Intent();
        setResult(RESULT_OK, logout);
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            konfir();
            return true;
        }
        if (id== R.id.action_add){
            startActivity(new Intent(getApplicationContext(), Admin_tambah_postingan.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onButtonClick(int id) {
        if (id == R.id.LayoutLinier1)
            startActivity(new Intent (Dashboard_Admin.super.getApplication() , TambahDataFasilitas.class));

        else if (id == R.id.LayoutLinier2)
            startActivity(new Intent(Dashboard_Admin.super.getApplication(), TambahInteraksi.class));

        else if (id == R.id.LayoutLinier3)
            startActivity(new Intent(Dashboard_Admin.super.getApplication(), TambahDataRekreasi.class));

        else if (id == R.id.LayoutLinier4)
            startActivity(new Intent(Dashboard_Admin.super.getApplicationContext(), TambahDataKonservasi.class));
    }
}