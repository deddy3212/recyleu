package com.example.recyle.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class TampilQr extends AppCompatActivity {
    String no_tlpon;
    Button back;
    TextView etnim;
    ImageView qrImage;
    String TAG = "GenerateQRCode";
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_qrcode);
        qrImage = findViewById(R.id.QR_Image);
        etnim = findViewById(R.id.nimET);

        Intent in = getIntent();
        Bundle bun = this.getIntent().getExtras();
        this.setTitle("QR Code");
        if (bun != null)
        {
            etnim.setText(" "+bun.getString("no_tlpon"));
            no_tlpon = bun.getString("no_tlpon");
        }

        QRGEncoder qrgEncoder = new QRGEncoder(no_tlpon, null, QRGContents.Type.TEXT, 700);
        try {
            bitmap = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }

        back = findViewById(R.id.button_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
