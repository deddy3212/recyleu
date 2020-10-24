package com.example.recyle.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    String editstat, editnama, editjmlh,editrombong, editwaktu;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    @Override
    public void handleResult(final Result rawResult) {
        // Do something with the result here
        // Log.v("tag", rawResult.getText()); // Prints scan results
        // Log.v("tag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

      //  AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
       // builder1.setMessage(rawResult.getText()).show();
        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pesanan Tiket");

        //get detail
        Query fquery =  reference.orderByChild("pId_Pesanan").equalTo(rawResult.getText());
        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //get data
                    editstat = "" + ds.child("status").getValue();
                    editnama = "" + ds.child("pNama_Pemesan").getValue();
                    editrombong = "" + ds.child("pRombongan_dari").getValue();
                    editjmlh = "" + ds.child("pTotal_pengunjung").getValue();
                    editwaktu = "" + ds.child("pTime").getValue();
                    //set data view
                }

                if (editstat.equals("Sudah Bayar")) {
                    builder1.setMessage("Atas Nama " + editnama + "\n" + "Rombongan Dari " + editrombong +
                            "\n " + "Jumlah Rombongan " + editjmlh)
                            .setCancelable(false)
                            .setPositiveButton("Silahkan Masuk ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
                                    dialogInterface.cancel();
                                    mScannerView.stopCamera();
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = builder1.create();
                    alertDialog.show();

                }
                else if (editstat.equals("Kadaluwarsa")) {
                    builder1.setMessage("Tiket Anda Tidak Bisa Terpakai Lagi. Silahkan Beli lagi. Terima kasih..")
                            .setCancelable(false)
                            .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
                                    dialogInterface.cancel();
                                    finish();
                                    mScannerView.stopCamera();

                                }
                            });
                    AlertDialog alertDialog = builder1.create();
                    alertDialog.show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
//        startActivity(new Intent(Scan.this, Dashboard_Admin.class));
    finish();
    }*/
}