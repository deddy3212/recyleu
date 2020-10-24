package com.example.recyle.BottomSheetbawah;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recyle.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModalBottomSheetKelola extends BottomSheetDialogFragment {

    private ActionListenerKelola mActionListenerKelola;
    private ActionListenerKelola getmActionListenerKelola;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.bottom_sheet_kelola, container, false);

        LinearLayout DFpostingan = v.findViewById(R.id.daftar_post);
        LinearLayout DFfasilitas = v.findViewById(R.id.daftar_fasilitas);
        LinearLayout DFkunjungan = v.findViewById(R.id.daftar_kunjungan);
        LinearLayout DFrekreasi = v.findViewById(R.id.daftar_rekreasi);
        LinearLayout DFkonservasi = v.findViewById(R.id.daftar_konservasi);


        DFpostingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListenerKelola !=null)
                    mActionListenerKelola.onButtonClick(R.id.daftar_post);
            }
        });

        DFfasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListenerKelola !=null)
                    mActionListenerKelola.onButtonClick(R.id.daftar_fasilitas);
            }
        });

        DFkunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListenerKelola != null)
                    mActionListenerKelola.onButtonClick(R.id.daftar_kunjungan);
            }
        });

        DFrekreasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListenerKelola !=null)
                    mActionListenerKelola.onButtonClick(R.id.daftar_rekreasi);
            }
        });

        DFkonservasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListenerKelola !=null)
                    mActionListenerKelola.onButtonClick(R.id.daftar_konservasi);
            }
        });
        return v;

    }


    public void setmActionListenerKelola(ModalBottomSheetKelola.ActionListenerKelola actionListener) {
        this.mActionListenerKelola = actionListener;
    }

    public interface ActionListenerKelola{
        void  onButtonClick(int id);
    }
}