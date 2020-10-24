package com.example.recyle.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recyle.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModalBottomSheet extends BottomSheetDialogFragment {

    private ActionListener mActionListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View v = inflater.inflate(R.layout.bottom_sheet, container, false);

        LinearLayout fasilitas = v.findViewById(R.id.LayoutLinier1);
        LinearLayout kunjungan = v.findViewById(R.id.LayoutLinier2);
        LinearLayout rekreasi = v.findViewById(R.id.LayoutLinier3);
        LinearLayout gambarlah = v.findViewById(R.id.LayoutLinier4);


        fasilitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener !=null)
                    mActionListener.onButtonClick(R.id.LayoutLinier1);
            }
        });

        kunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener !=null)
                    mActionListener.onButtonClick(R.id.LayoutLinier2);
            }
        });

        rekreasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener != null)
                    mActionListener.onButtonClick(R.id.LayoutLinier3);
            }
        });

        gambarlah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActionListener !=null)
                    mActionListener.onButtonClick(R.id.LayoutLinier4);
            }
        });

       return v;

    }

    public void setActionListener(ActionListener actionListener) {
        this.mActionListener = actionListener;
    }

    interface ActionListener{
        void  onButtonClick(int id);
    }
}
