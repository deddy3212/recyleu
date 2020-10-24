package com.example.recyle.PesanTiket;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recyle.R;

public class TiketTerbit extends AppCompatActivity {
    ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tiket_terbit);

        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Berhasil");

    }
}
