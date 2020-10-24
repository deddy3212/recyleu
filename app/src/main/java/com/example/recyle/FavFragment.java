package com.example.recyle;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.recyle.LoginUser.LoginUser_Activity;
import com.example.recyle.PesanTiket.Pesanan;
import com.example.recyle.PesanTiket.Tambah_Pesanan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FavFragment extends Fragment {

    CardView tes3;

    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notifikasi,container,false);
        auth = FirebaseAuth.getInstance();

        tes3 = v.findViewById(R.id.E_tiket);
        tes3.setOnClickListener(new View.OnClickListener() {
            FirebaseUser user = auth.getCurrentUser();
            @Override
            public void onClick(View view) {
                if (user!= null){
                    startActivity(new Intent (FavFragment.super.getContext() , Tambah_Pesanan.class));
                }
                else {
                    startActivity(new Intent (FavFragment.super.getContext() , LoginUser_Activity.class));
                }

            }
        });

        return v;
    }
}
