package com.example.recyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recyle.MenuTerkait.Fasilitasnya;
import com.example.recyle.MenuTerkait.Interaksinya;
import com.example.recyle.MenuTerkait.Konservasinya;
import com.example.recyle.MenuTerkait.KontakKami;
import com.example.recyle.MenuTerkait.Rekreasinya;
import com.example.recyle.MenuTerkait.Tentang;

public class MenuFrag extends Fragment {

    ImageView fasilitas, peta, kontak, rekreasi, konservasi, tentang;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_menu_fragment,container,false);

        fasilitas = v.findViewById(R.id.image_fasilitas);
        fasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFrag.super.getContext() , Fasilitasnya.class));
            }
        });


        peta = v.findViewById(R.id.image_peta);
        peta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFrag.super.getContext(), Interaksinya.class));
            }
        });


        kontak = v.findViewById(R.id.image_kontak);
        kontak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFrag.super.getContext(), KontakKami.class));
            }
        });

        rekreasi = v.findViewById(R.id.image_rekreasi);
        rekreasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFrag.super.getContext(), Rekreasinya.class));
            }
        });

        konservasi = v.findViewById(R.id.image_konservasi);
        konservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFrag.super.getContext(), Konservasinya.class));
            }
        });

        tentang = v.findViewById(R.id.image_tentang);
        tentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuFrag.super.getContext(), Tentang.class));
            }
        });

        return v;


    }
}
