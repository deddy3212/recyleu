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

import com.example.recyle.Model.ModelInteraksi;
import com.example.recyle.Model.ModelRekreasi;
import com.example.recyle.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterRekreasi extends RecyclerView.Adapter<AdapterRekreasi.MyHolder> {

    Context context;
    List<ModelRekreasi> rekreasiList;

    public AdapterRekreasi(Context context, List<ModelRekreasi> rekreasiList) {
        this.context = context;
        this.rekreasiList = rekreasiList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout row_post.xml
        ViewGroup viewGroup = null;
        View view = LayoutInflater.from(context).inflate(R.layout.row_rekreasi, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        //get data
        String uid = rekreasiList.get(i).getUid();
        String uEmail = rekreasiList.get(i).getuEmail();
        String uName = rekreasiList.get(i).getuEmail();
        String uDp = rekreasiList.get(i).getuDp();
        String pId = rekreasiList.get(i).getpId();
        String pJudul_Rekreasi = rekreasiList.get(i).getpJudul_Rekreasi();
        String pIsi_Rekreasii = rekreasiList.get(i).getpIsi_Rekreasii();
        String pImage = rekreasiList.get(i).getpImage();
        String pTimeStamp = rekreasiList.get(i).getpTime();



        //convert timstamp todd/mm/yy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        //set data
        myHolder.uNameTV.setText(uName);
        myHolder.pTimeTV.setText(pTime);
        myHolder.pJudulTV.setText(pJudul_Rekreasi);
        myHolder.pIsiPostinganTV.setText(pIsi_Rekreasii);



        //set user dp
        //set post image
        if (pImage.equals("noImage")){
            //hide
            myHolder.pImaglv.setVisibility(View.GONE);

        }else {

        }try {
            Picasso.get().load(pImage).into(myHolder.pImaglv);
        }
        catch (Exception e){

        }



    }

    @Override
    public int getItemCount() {


        return rekreasiList.size();
    }




    //views holder class
    class MyHolder extends RecyclerView.ViewHolder{

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