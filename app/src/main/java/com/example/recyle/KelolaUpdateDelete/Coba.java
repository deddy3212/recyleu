package com.example.recyle.KelolaUpdateDelete;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.BottomSheetbawah.ModalBottomSheetKelola;
import com.example.recyle.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Coba extends AppCompatActivity implements ModalBottomSheetKelola.ActionListenerKelola {

    private BottomSheetDialog bottomSheetDialog;
    Button coc;
    ImageView coc1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coba);

        coc1 = findViewById(R.id.coba);
        coc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModalBottomSheetKelola modalBottomSheet = new ModalBottomSheetKelola();
                modalBottomSheet.setmActionListenerKelola(Coba.this);
                modalBottomSheet.show(getSupportFragmentManager(),"modalmenu`````");
            }
        });


    }

    @Override
    public void onButtonClick(int id) {
        if (id == R.id.daftar_post)
            startActivity(new Intent(Coba.this, DaftarDaftar.class));

        else if (id == R.id.daftar_fasilitas)
            startActivity(new Intent(Coba.super.getApplication(), Kelola_Fasilitas.class));
    }
}
