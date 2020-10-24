package com.example.recyle.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recyle.Model.ModelKonservasi;
import com.example.recyle.Model.ModelRekreasi;
import com.example.recyle.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterKonservasi extends RecyclerView.Adapter<AdapterKonservasi.MyHolder> {

    Context context;
    List<ModelKonservasi> konservasiList;

    public AdapterKonservasi(Context context, List<ModelKonservasi> konservasiList) {
        this.context = context;
        this.konservasiList = konservasiList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_post.xml
        ViewGroup viewGroup = null;
        View view = LayoutInflater.from(context).inflate(R.layout.row_konservasi, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        //get data
        String uid = konservasiList.get(i).getUid();
        String uEmail = konservasiList.get(i).getuEmail();
        String uName = konservasiList.get(i).getuEmail();
        String uDp = konservasiList.get(i).getuDp();
        String pId = konservasiList.get(i).getpId();
        String pJudul_Konservasi = konservasiList.get(i).getpJudul_Konservasi();
        String pIsi_Konservasi = konservasiList.get(i).getpIsi_Konservasi();
        String pImage = konservasiList.get(i).getpImage();
        String pTimeStamp = konservasiList.get(i).getpTime();


        //convert timstamp todd/mm/yy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //ser data
        myHolder.uNameTV.setText(uName);
        myHolder.pTimeTV.setText(pTime);
        myHolder.pJudulTV.setText(pJudul_Konservasi);
        myHolder.pIsiPostinganTV.setText(pIsi_Konservasi);


        //set user dp
        //set post image
        if (pImage.equals("noImage")) {
            //hide
            myHolder.pImaglv.setVisibility(View.GONE);

        } else {

        }
        try {
            Picasso.get().load(pImage).into(myHolder.pImaglv);
        } catch (Exception e) {

        }


    }

    @Override
    public int getItemCount() {


        return konservasiList.size();
    }


    //views holder class
    class MyHolder extends RecyclerView.ViewHolder {

        //views from row_post.xml
        ImageView uPicturelv, pImaglv;
        TextView uNameTV, pTimeTV, pJudulTV, pIsiPostinganTV;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init Views
            uPicturelv = itemView.findViewById(R.id.uPictureTV);
            pImaglv = itemView.findViewById(R.id.pImageTV);
            uNameTV = itemView.findViewById(R.id.uNameTV);
            pTimeTV = itemView.findViewById(R.id.pTimeTV);
            pJudulTV = itemView.findViewById(R.id.pJudulTV);
            pIsiPostinganTV = itemView.findViewById(R.id.pIsiPostinganTV);

        }
    }
}